package com.example.prj02_healthy_plan.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.prj02_healthy_plan.IngredientInRecipe
import com.example.prj02_healthy_plan.R
import com.example.prj02_healthy_plan.RecipeFirebase
import com.example.prj02_healthy_plan.uiModel.RecipeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.IOException
import java.net.URL


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailRecipeScreen(nav: NavHostController, selectedRecipeName: MutableState<String>) {
    val viewRecipeModel: RecipeViewModel = viewModel()
    val recipeList by viewRecipeModel.recipeList.collectAsState()
    LaunchedEffect(key1 = Unit) {
        viewRecipeModel.fetchRecipes()
    }
    val recipe = recipeList.find { it.name == selectedRecipeName.value } ?: return

    Scaffold(
        topBar = {
            TopAppBar(colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(
                    245,
                    250,
                    255
                )
            ),
                title = {
                    Text(
                        "Recipe",
                        style = TextStyle(
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                })
        }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(245, 250, 255))
        ) {
            DetailRecipeContent(recipe = recipe)
        }
    }
}

@Composable
fun DetailRecipeContent(
    recipe: RecipeFirebase
) {
    val sizeOfIngredients = recipe.ingredients?.size ?: 0
    var checkedIngredients: List<Boolean> by remember { mutableStateOf(List(sizeOfIngredients) { false }) }
    val detailRecipeContentScrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(245, 250, 255))
            .verticalScroll(detailRecipeContentScrollState)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            // Image
            val painter = rememberAsyncImagePainter(model = recipe.imageUrl)
            Image(
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
            ) {
                // Name of food
                recipe.name?.let {
                    Text(
                        text = it,
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
                // Calories
                Text(
                    text = "100 Cal",
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 15.sp
                )
            }

        }
        Spacer(modifier = Modifier.height(10.dp))

        // Description
        recipe.description?.let {
            Text(
                text = it,
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .wrapContentHeight(),
                color = Color.Gray,
                fontSize = 16.sp
            )
        }
        Spacer(modifier = Modifier.height(5.dp))

        // Title: Ingredients
        Text(
            text = "Ingredients",
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 5.dp),
            style = MaterialTheme.typography.titleMedium
        )
        // Ingredients List
        Column (
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .wrapContentHeight()
                .fillMaxWidth()
                .background(Color.White),
        ) {
            recipe.ingredients?.forEachIndexed { index, ingredient ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = checkedIngredients[index],
                        onCheckedChange = {
                            checkedIngredients = checkedIngredients.toMutableList().also {
                                it[index] = !it[index]
                            }
                        }
                    )
                    Text(
                        // Quantity x first number in String unit (e.g. 1 x 100 g) + name
                        text = calculateTotalQuantity(ingredient),
                        modifier = Modifier.padding(start = 5.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 15.sp
                    )}
                }
            }
        // Title: Instructions
        Text(
            text = "Instructions",
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 5.dp),
            style = MaterialTheme.typography.titleMedium,
            fontSize = 15.sp
        )
         //Instructions
        val instructionText = rememberUrlText(recipe.instructionUrl ?: "")
        Text(
            text = instructionText,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 15.sp
        )
    }
}

@Composable
fun rememberUrlText(url: String): String {
    var text by remember { mutableStateOf("") }
    val httpClient = remember { OkHttpClient() }

    DisposableEffect(url) {
        val request = Request.Builder().url(url).build()
        val call = httpClient.newCall(request)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = call.execute()
                if (response.isSuccessful) {
                    text = response.body?.string() ?: ""
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        onDispose {
            call.cancel()
        }
    }

    return text
}

fun calculateTotalQuantity(ingredient: IngredientInRecipe): String {
    ingredient.unit?.let { unit ->
        ingredient.quantity?.let { quantity ->
            // Tách số từ đơn vị
            val quantityRegex = "\\d+".toRegex()
            val matchResult = quantityRegex.find(unit)
            val unitNumber = matchResult?.value?.toDoubleOrNull()
                ?: 1.0 // Số đầu tiên trong đơn vị hoặc mặc định là 1 nếu không tìm thấy

            // Nhân số lượng với số đầu tiên trong đơn vị
            var totalQuantity = quantity * unitNumber

            // Kiểm tra xem totalQuantity có phải là số nguyên không
            val isInteger = totalQuantity % 1 == 0.0

            // Tạo chuỗi kết quả
            return if (!isInteger) {
                "$totalQuantity ${unit.substringAfter(matchResult?.value ?: "")} ${ingredient.name}"
            } else {
                "${totalQuantity.toInt()} ${unit.substringAfter(matchResult?.value ?: "")} ${ingredient.name}"
            }
        }
    }

    // Trả về chuỗi rỗng nếu không có đủ thông tin
    return ""
}