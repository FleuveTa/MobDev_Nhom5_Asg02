package com.example.prj02_healthy_plan.ui.theme

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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FoodBank
import androidx.compose.material.icons.outlined.HeartBroken
import androidx.compose.material.icons.outlined.Info
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.prj02_healthy_plan.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChienTa(nav: NavHostController) {
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
            SearchBar()

            Spacer(modifier = Modifier.height(5.dp))

            ExploreTabScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar() {
    var text by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    var items = remember {
        mutableStateListOf(
            "Ha Tung Anh",
            "Ta Hoang Giang",
            "Ta Quang Chien",
            "Doan Minh Hoang",
            "Dong Van Duong"
        )
    }

    SearchBar(
        modifier = Modifier
            .fillMaxWidth(),
        query = text,
        onQueryChange = {
            text = it
        },
        onSearch = {
            items.add(text)
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
fun ExploreTabScreen() {
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
            0 -> RecommendedScreen(scrollState = exploreTabScreenScrollState)
            1 -> MyRecipesScreen(scrollState = exploreTabScreenScrollState)
        }
    }
}

@Composable
fun RecommendedScreen(scrollState: ScrollState) {
    val recommendedFoodScrollState = rememberScrollState()

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
                        .background(Color.Green)
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

                    IconButton(onClick = { /*TODO*/ }) {
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
            onClick = { /*TODO*/ },
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.White,
                containerColor = Color.Green
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
            RecommendedFoods(
                image = painterResource(id = R.drawable.tunasaladfood),
                title = "Tuna Salad",
                cal = 443
            )
            RecommendedFoods(
                image = painterResource(id = R.drawable.tunasaladfood),
                title = "Tuna Salad",
                cal = 443
            )
            RecommendedFoods(
                image = painterResource(id = R.drawable.tunasaladfood),
                title = "Tuna Salad",
                cal = 443
            )
            RecommendedFoods(
                image = painterResource(id = R.drawable.tunasaladfood),
                title = "Tuna Salad",
                cal = 443
            )
            RecommendedFoods(
                image = painterResource(id = R.drawable.tunasaladfood),
                title = "Tuna Salad",
                cal = 443
            )
        }
    }
}

@Composable
fun RecommendedFoods(image: Painter, title: String, cal: Number) {
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
        ) {
            Image(
                painter = image,
                contentDescription = "Food Image",
                contentScale = ContentScale.Crop
            )
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
                    imageVector = Icons.Outlined.HeartBroken,
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

        Icon(
            imageVector = Icons.Default.List,
            contentDescription = "List Icon"
        )
    }
}

@Composable
fun MyRecipesScreen(scrollState: ScrollState) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp)
            .verticalScroll(scrollState)
    ) {
        MyRecipes(
            image = painterResource(R.drawable.tunasaladfood),
            title = "Tuna Salad",
            description = "This is Tuna Salad."
        )
        MyRecipes(
            image = painterResource(R.drawable.tunasaladfood),
            title = "Tuna Salad",
            description = "This is Tuna Salad."
        )
        MyRecipes(
            image = painterResource(R.drawable.tunasaladfood),
            title = "Tuna Salad",
            description = "This is Tuna Salad."
        )
        MyRecipes(
            image = painterResource(R.drawable.tunasaladfood),
            title = "Tuna Salad",
            description = "This is Tuna Salad."
        )
        MyRecipes(
            image = painterResource(R.drawable.tunasaladfood),
            title = "Tuna Salad",
            description = "This is Tuna Salad."
        )
        MyRecipes(
            image = painterResource(R.drawable.tunasaladfood),
            title = "Tuna Salad",
            description = "This is Tuna Salad."
        )
        MyRecipes(
            image = painterResource(R.drawable.tunasaladfood),
            title = "Tuna Salad",
            description = "This is Tuna Salad."
        )
    }
}

@Composable
fun MyRecipes(image: Painter, title: String, description: String) {
    Row(
        modifier = Modifier
            .padding(5.dp)
            .background(Color.White)
            .fillMaxWidth()
            .height(150.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(160.dp)
        ) {
            Image(
                painter = image,
                contentDescription = "Food Image",
                contentScale = ContentScale.Crop
            )
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

            Row {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = "Info Icon"
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Outlined.HeartBroken,
                        contentDescription = "Heart Icon"
                    )
                }
            }


        }
    }
}

@Preview
@Composable
fun PreviewExploreScreen() {
    val navigationController = rememberNavController()

    ChienTa(nav = navigationController)
}