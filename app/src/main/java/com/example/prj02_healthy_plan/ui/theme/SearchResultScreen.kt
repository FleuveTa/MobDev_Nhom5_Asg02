package com.example.prj02_healthy_plan.ui.theme

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.prj02_healthy_plan.RecipeFirebase
import com.example.prj02_healthy_plan.uiModel.RecipeViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchResultScreen(
    nav: NavHostController,
    searchInfo: MutableState<String>,
    recipeViewModel: RecipeViewModel
) {
    var searchQuery by remember { mutableStateOf(searchInfo.value) }
    val recipeList = recipeViewModel.recipeList.collectAsState().value

    LaunchedEffect(key1 = Unit) {
        recipeViewModel.fetchRecipes()
    }
    val recipeResultSearchList = recipeList.filter { recipe ->
        recipe.name?.contains(searchQuery, ignoreCase = true) == true
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
                    Text(
                        "Explore",
                        style = TextStyle(
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { nav.navigate("explore")
                    searchInfo.value = ""}) {
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
            SearchBar(nav = nav, initialQuery = searchQuery) { newQuery ->
                searchQuery = newQuery
            }

            Spacer(modifier = Modifier.height(5.dp))

            ResultScreen(scrollState = ScrollState(0), nav = nav, recipeResultsList = recipeResultSearchList, recipeViewModel = recipeViewModel)
        }
    }
}

@Composable
fun ResultScreen(scrollState: ScrollState, nav: NavHostController, recipeResultsList: List<RecipeFirebase>, recipeViewModel: RecipeViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp)
            .verticalScroll(scrollState)
    ) {
        recipeResultsList.forEach { recipe ->
            MyRecipeResult(
                recipeId = recipe.id ?: "",
                url = recipe.imageUrl ?: "",
                name = recipe.name ?: "",
                description = recipe.description ?: "",
                nav = nav,
                recipeViewModel =  recipeViewModel
            )
        }
    }
}

@Composable
fun MyRecipeResult(recipeId: String, url: String, name: String, description: String, nav: NavHostController, recipeViewModel: RecipeViewModel) {
    val auth: FirebaseAuth = Firebase.auth
    val uId = auth.currentUser?.uid
    val myRecipeList by recipeViewModel.myRecipeList.collectAsState()
    val isFavorite = myRecipeList.any { recipe ->
        recipe.userId == uId && recipe.recipes?.any { it.id == recipeId } == true
    }
    LaunchedEffect(key1 = Unit) {
        snapshotFlow { myRecipeList }.collect {
            // Handle any additional logic if needed
        }
    }
    Row(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
            .height(100.dp)
            .background(Color.White)
            .clickable {
                nav.navigate("detailRecipe")
                recipeViewModel.selectedRecipeName.value = name
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(160.dp)
        ) {
            LoadImage(url = url)
        }
        Column(
            modifier = Modifier
                .padding(start = 16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                text = name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = description,
                fontSize = 14.sp,
                fontWeight = FontWeight.Light
            )
            FavoriteIcon(isFavorite = isFavorite) {
                recipeViewModel.toggleMyRecipe(uId, recipeId)
            }
        }
    }
}





