package com.example.prj02_healthy_plan.ui.theme

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.prj02_healthy_plan.Ingredient
import com.example.prj02_healthy_plan.uiModel.RecipeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserAddIngredientScreen(nav: NavHostController){
    val viewRecipeModel: RecipeViewModel = viewModel()
    val allIngredientList by viewRecipeModel.ingredientList.collectAsState()
    val (filteredIngredientList, setFilteredIngredientList) = remember { mutableStateOf(allIngredientList) }
    val (isSearching, setIsSearching) = remember { mutableStateOf(false) }
    LaunchedEffect(key1 = Unit) {
        viewRecipeModel.fetchIngredients()
    }
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
                    TextButton(
                        onClick = { /*TODO*/ },
                        modifier = Modifier
                    ) {
                        Text(
                            "Add Ingredient",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { nav.navigate("explore") }) {
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
            SearchBar(nav = nav) { query ->
                if (query.isNotEmpty()) {
                    setIsSearching(true) // Đánh dấu là đang tìm kiếm
                    setFilteredIngredientList(allIngredientList.filter { it.name?.contains(query, ignoreCase = true) == true })
                } else {
                    setIsSearching(false) // Đánh dấu là không tìm kiếm
                    setFilteredIngredientList(allIngredientList)
                }
            }

            Spacer(modifier = Modifier.height(5.dp))

            // Hiển thị tất cả các mục khi không tìm kiếm hoặc khi kết quả tìm kiếm rỗng
            if (!isSearching || filteredIngredientList.isEmpty()) {
                UserAddIngredientTabScreen(ingredientList = allIngredientList)
            } else {
                // Hiển thị kết quả tìm kiếm
                AllIngredientScreen(scrollState = rememberScrollState(), ingredientList = filteredIngredientList)
            }
        }
    }
}


@Composable
fun UserAddIngredientTabScreen(ingredientList: List<Ingredient>) {
    var tabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("All", "History")
    val userAddIngredientTabScreenScrollState = rememberScrollState()

    Column(modifier = Modifier.fillMaxWidth()) {
        TabRow(selectedTabIndex = tabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(text = { Text(title) },
                    selected = tabIndex == index,
                    onClick = { tabIndex = index }
                )
            }
        }
        when (tabIndex) {
            0 -> AllIngredientScreen(scrollState = userAddIngredientTabScreenScrollState, ingredientList = ingredientList)
            1 -> HistoryAddScreen(scrollState = userAddIngredientTabScreenScrollState)
        }
    }
}

@Composable
fun AllIngredientScreen(scrollState: ScrollState, ingredientList: List<Ingredient>) {
    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
    ) {
        ingredientList.forEach { ingredient->
            IngredientCanAdd(
                name = ingredient.name ?: "",
                unit = ingredient.unit ?: "",
                cal = ingredient.nutrition?.get(0) ?: 0.0
            )
        }
    }
}

@Composable
fun IngredientCanAdd(name: String, unit: String, cal: Number) {
    Row(
        modifier = Modifier
            .padding(5.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(
                Color.White,
                RoundedCornerShape(10.dp)
            )
            .fillMaxWidth()
            .height(70.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .padding(5.dp)
                .padding(start = 16.dp)
        ) {
            Text(
                text = name,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )

            Text(
                text = unit,
                fontSize = 14.sp,
            )
        }

        Row(
            modifier = Modifier
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$cal cals",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = Icons.Default.AddCircle,
                contentDescription = "Add Icon",
                tint = GreenMain,
                modifier = Modifier.size(30.dp)
            )
        }
    }
}