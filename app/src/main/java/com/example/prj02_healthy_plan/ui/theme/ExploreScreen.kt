package com.example.prj02_healthy_plan.ui.theme

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FoodBank
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.prj02_healthy_plan.MyRecipeFirebase
import com.example.prj02_healthy_plan.User
import com.example.prj02_healthy_plan.uiModel.RecipeViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChienTa(nav: NavHostController, searchInfo: MutableState<String>, selectedRecipeName: MutableState<String>) {
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
            SearchBar(nav, initialQuery = searchInfo.value, onSearch = { searchInfo.value = it })

            Spacer(modifier = Modifier.height(5.dp))

            ExploreTabScreen(nav, selectedRecipeName = selectedRecipeName)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    nav: NavHostController,
    initialQuery: String = "",
    onSearch : (String) -> Unit = {}
) {
    var text by remember { mutableStateOf(initialQuery) }
    var active by remember { mutableStateOf(false) }
    var items = remember { mutableStateListOf<String>(
        initialQuery
    ) }

    SearchBar(
        modifier = Modifier
            .fillMaxWidth(),
        query = text,
        onQueryChange = {
            text = it
        },
        onSearch = {
            items.add(text)
            onSearch(text)
            // if current screen is SearchResultScreen, then not navigate
            if (nav.currentBackStackEntry?.destination?.route != "searchResult") {
                nav.navigate("searchResult")
            }
            active = false
        },
        active = active,
        onActiveChange = {
            active = it
        },
        placeholder = {
            Text(text = "Search")
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon"
            )
        },
        trailingIcon = {
            if (active) {
                Icon(
                    modifier = Modifier.clickable {
                        if (text.isNotEmpty()) {
                            text = ""
                        } else {
                            active = false
                        }
                    },
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close Icon"
                )
            }
        }
    ) {
        items.forEach {
            Row(
                modifier = Modifier.padding(all = 14.dp)
            ) {
                Icon(
                    modifier = Modifier.padding(end = 10.dp),
                    imageVector = Icons.Default.History,
                    contentDescription = "History Icon"
                )
                Text(text = it)
            }
        }
    }
}

@Composable
fun ExploreTabScreen(nav: NavHostController, selectedRecipeName: MutableState<String>) {
    var tabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Recommended", "My Recipes")
    val exploreTabScreenScrollState = rememberScrollState()

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
            0 -> RecommendedScreen(scrollState = exploreTabScreenScrollState, nav = nav, selectedRecipeName = selectedRecipeName)
            1 -> MyRecipesScreen(scrollState = exploreTabScreenScrollState, nav = nav)
        }
    }
}

@Composable
fun RecommendedScreen(scrollState: ScrollState, nav: NavHostController, selectedRecipeName: MutableState<String>) {
    val recommendedFoodScrollState = rememberScrollState()
    val viewRecipeModel: RecipeViewModel = viewModel()
    val recipeList by viewRecipeModel.recipeList.collectAsState()

    LaunchedEffect(key1 = Unit) {
        viewRecipeModel.fetchRecipes()
    }

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

                    IconButton(onClick = { nav.navigate("searchChoice") }) {
                        Icon(
                            imageVector = Icons.Default.AddCircle,
                            contentDescription = "Add Icon",
                            tint = Color.White,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }

                Ingredients(name = "Butter", amount = 100)
                Ingredients(name = "Shrimps, boiled", amount = 200)
                Ingredients(name = "Garlics", amount = 2)
            }
        }

        Spacer(modifier = Modifier.height(5.dp))

        Button(
            onClick = { nav.navigate("viewRecipeResult")},
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.White,
                containerColor = GreenMain
            ),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .height(45.dp)
        ) {
            Text(
                text = "View 140+ results",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }

        Text(
            text = "Recommended for you:",
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
                .horizontalScroll(recommendedFoodScrollState)
        ) {
            for (recipe in recipeList) {
                RecommendedFoods(
                    url = recipe.imageUrl ?: "",
                    title = recipe.name ?: "",
                    cal = recipe.nutrition?.get(0) ?: 0.0,
                    nav = nav,
                    selectedRecipeName = selectedRecipeName
                )
            }
        }
    }
}

@Composable
fun LoadImage(url: String) {
    val painter = rememberImagePainter(data = url)
    Image(
        painter = painter,
        contentDescription = "Food Image",
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
fun RecommendedFoods(url: String, title: String, cal: Double, nav: NavHostController, selectedRecipeName: MutableState<String>) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(160.dp)
            .padding(end = 5.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clickable {
                    nav.navigate("detailRecipe")
                    selectedRecipeName.value = title
                }
        ) {
            LoadImage(url = url)
        }

        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(5.dp)
        )

        Row(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$cal CALS",
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp
            )

            IconButton(
                onClick = { /*TODO*/ }
            ) {
                Icon(
                    imageVector = Icons.Default.FavoriteBorder,
                    contentDescription = "Heart Icon"
                )
            }
        }
    }
}

@Composable
fun Ingredients(name: String, amount: Number) {
    Row(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth()
            .height(60.dp)
            .background(Color.White),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = name,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )
            Text(
                text = amount.toString() + "g",
                fontWeight = FontWeight.Light
            )
        }
        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                imageVector = Icons.Filled.RemoveCircle,
                contentDescription = "Remove Icon",
                tint = Color.Red
            )
        }
    }
}

@Composable
fun MyRecipesScreen(scrollState: ScrollState, nav: NavHostController) {
    val viewRecipeModel: RecipeViewModel = viewModel()
    val auth: FirebaseAuth = Firebase.auth
    val uId = auth.currentUser?.uid
    val myRecipeList by viewRecipeModel.myRecipeList.collectAsState()

    LaunchedEffect(key1 = Unit) {
      viewRecipeModel.fetchMyRecipes()
    }
    val myRecipe = myRecipeList.firstOrNull{ it.userId == uId }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp)
            .verticalScroll(scrollState)
    ) {
        for (recipe in myRecipe?.recipes ?: listOf()) {
            MyRecipes(
                title = recipe.name ?: "",
                description = recipe.description ?: "",
                imageUrl = recipe.imageUrl ?: "",
                nav = nav
            )
        }
    }
}

@Composable
fun MyRecipes( title: String, description: String, imageUrl: String, nav: NavHostController) {
    // i want this row can be clicked
    Row(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
            .height(100.dp)
            .background(Color.White)
            .clickable {
                nav.navigate("detailRecipe")
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(160.dp)
        ) {
            LoadImage(url = imageUrl)
        }
        Column(
            modifier = Modifier
                .padding(start = 16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = description,
                fontSize = 14.sp,
                fontWeight = FontWeight.Light
            )
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Heart Icon",
                tint = Color.Red,
            )
        }
    }
}
