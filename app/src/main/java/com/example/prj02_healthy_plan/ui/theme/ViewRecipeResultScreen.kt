package com.example.prj02_healthy_plan.ui.theme

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.FoodBank
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.prj02_healthy_plan.R
import com.example.prj02_healthy_plan.uiModel.IngredientViewModel
import com.example.prj02_healthy_plan.uiModel.RecipeViewModel
import kotlinx.coroutines.flow.filter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewRecipeResultScreen(nav: NavHostController, recipeViewModel: RecipeViewModel, ingredientViewModel: IngredientViewModel) {
    val scrollState = rememberScrollState()
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
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                })
        }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(245, 250, 255))
        ) {
            RecipeResultScreen(scrollState = scrollState, nav = nav, recipeViewModel = recipeViewModel, ingredientViewModel = ingredientViewModel)
        }
    }
}

@Composable
fun RecipeResultScreen(scrollState: ScrollState, nav: NavHostController, recipeViewModel: RecipeViewModel, ingredientViewModel: IngredientViewModel) {
    var showDialog by remember { mutableStateOf(false) }
    val resultScreenScrollState = rememberScrollState()
    val userIngredients = ingredientViewModel.userIngredients
    val recipeList by recipeViewModel.recipeList.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp)
            .verticalScroll(scrollState)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(
                    Color.White,
                    RoundedCornerShape(20.dp)
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .background(GreenMain)
                        .padding(start = 8.dp, end = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.FoodBank,
                        contentDescription = "Ingredient Icon",
                        tint = Color.White,
                        modifier = Modifier.size(42.dp)
                    )
                    Text(
                        text = "Ingredient Search",
                        fontSize = 18.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )

                    IconButton(onClick = { showDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.AddCircle,
                            contentDescription = "Add Icon",
                            tint = Color.White,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
                if (showDialog) {
                    ShowSearchChoiceDialog(nav = nav, onDismiss = { showDialog = false })
                }
                if (userIngredients.isEmpty()) {
                    Text(
                        text = "No ingredient added",
                        modifier = Modifier.padding(5.dp)
                    )
                }
                else {
                    for (ingredient in userIngredients) {
                        IngredientCanAdd(
                            name = ingredient.name ?: "",
                            unit = ingredient.unit ?: "",
                            cal = ingredient.nutrition?.get(0) ?: 0.0,
                            isAdded = true,
                            isReadOnly = true,
                        ) {
                            ingredientViewModel.toggleIngredient(ingredient)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = "Corresponding results:",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.padding(5.dp)
        )
        Row(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxHeight()
                .fillMaxWidth()
                .background(Color.White)
                .horizontalScroll(resultScreenScrollState)
        ) {
            // Find the corresponding recipes with the user's ingredients
            val matchedRecipes = recipeList.filter { recipe ->
                recipe.ingredients?.any { ingredient ->
                    userIngredients.any { userIngredient ->
                        ingredient.name == userIngredient.name
                    }
                } ?: false
            }
            if (matchedRecipes.isEmpty()) {
                Text(
                    text = "No corresponding recipe found",
                    modifier = Modifier.padding(5.dp)
                )
            }
            else {
                for (recipe in matchedRecipes) {
                    RecommendedFoods(
                        recipeId = recipe.id ?: "",
                        url = recipe.imageUrl ?: "",
                        title = recipe.name ?: "",
                        cal = recipe.nutrition?.get(0) ?: 0.0,
                        nav = nav,
                        recipeViewModel = recipeViewModel
                    )
                }
            }
        }
    }
}



