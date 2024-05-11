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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.prj02_healthy_plan.R
import java.net.URL


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailRecipeScreen(nav: NavHostController) {
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
            DetailRecipeContent(recipe = Recipe(
                name = "Tuna Salad",
                description = "This is a healthy and delicious tuna salad recipe. It's perfect for a light lunch or dinner. Enjoy!",
                totalCalorie = 430f,
                image = "",
                ingredients = listOf(
                    Ingredient("Tuna", 1f, "can"),
                    Ingredient("Salad", 1f, "bowl"),
                    Ingredient("Mayo", 1f, "tbsp"),
                    Ingredient("Something", 1f, "tbsp"),
                    Ingredient("Something", 1f, "tbsp"),
                    Ingredient("Something", 1f, "tbsp")
                ),
                instructions = "1.Mix all ingredients\n 2.Do something\n 3.Do something else",
                nutrition = listOf(100f, 200f, 300f, 400f, 500f, 600f),
                unit = "bowl"
            ))
        }
    }
}

@Composable
fun DetailRecipeContent(
    recipe: Recipe,
) {
    var checkedIngredients by remember { mutableStateOf(List(recipe.ingredients.size) { false }) }
    val detailRecipeContentScrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(245, 250, 255))
            .verticalScroll(detailRecipeContentScrollState)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            // Image
            Image(
                painter = painterResource(id = R.drawable.tunasaladfood),
                contentDescription = "Recipe Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
            ) {
                // Name of food
                Text(
                    text = recipe.name,
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                // Calories
                Text(
                    text = "1 ${recipe.unit} - ${recipe.totalCalorie} Cal",
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 15.sp
                )
            }

        }
        Spacer(modifier = Modifier.height(10.dp))

        // Description
        Text(
            text = recipe.description,
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .wrapContentHeight(),
            color = Color.Gray,
            fontSize = 16.sp
        )
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
        ){
            recipe.ingredients.map { "${it.name} - ${it.quantity} ${it.unit}" }.toMutableList(
            ).forEachIndexed { index, ingredient ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 5.dp)
                ) {
                    Checkbox(
                        checked = checkedIngredients[index],
                        onCheckedChange = {
                            checkedIngredients =
                                checkedIngredients.toMutableList().apply { set(index, it) }
                        }
                    )
                    Text(
                        text = ingredient,
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 15.sp
                    )
                }
            }
        }
        // Title: Instructions
        Text(
            text = "Instructions",
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 5.dp),
            style = MaterialTheme.typography.titleMedium,
            fontSize = 15.sp
        )

        // Instructions List
        Column (
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .wrapContentHeight()
                .fillMaxWidth()
                .background(Color.White),
        ) {
            Text(
                text = recipe.instructions,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 15.sp
            )
        }
    }
}

data class Recipe(
    val name: String,
    val description: String,
    val totalCalorie: Float = 0f,
    val image: String,
    val ingredients: List<Ingredient>,
    val instructions: String,
    val nutrition: List<Float>,
    val unit: String
)

data class Ingredient(
    val name: String,
    val quantity: Float,
    val unit: String
)

@Preview
@Composable
fun PreviewDetailRecipeScreen() {
    DetailRecipeScreen(nav = rememberNavController())
}


