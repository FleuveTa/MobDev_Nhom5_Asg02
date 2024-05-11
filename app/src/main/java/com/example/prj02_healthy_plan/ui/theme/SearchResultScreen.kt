package com.example.prj02_healthy_plan.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
fun SearchResultScreen(nav: NavHostController) {
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
            SearchBar(nav)

            Spacer(modifier = Modifier.height(5.dp))

            ResultScreen(scrollState = ScrollState(0), nav = nav)
        }
    }
}

@Composable
fun ResultScreen(scrollState: ScrollState, nav: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp)
            .verticalScroll(scrollState)
    ) {
        MyRecipeResult(
            image = painterResource(R.drawable.tunasaladfood),
            title = "Tuna Salad",
            description = "This is Tuna Salad.",
            nav = nav
        )
        MyRecipeResult(
            image = painterResource(R.drawable.tunasaladfood),
            title = "Tuna Salad",
            description = "This is Tuna Salad.",
            nav = nav
        )
        MyRecipeResult(
            image = painterResource(R.drawable.tunasaladfood),
            title = "Tuna Salad",
            description = "This is Tuna Salad.",
            nav = nav
        )
        MyRecipeResult(
            image = painterResource(R.drawable.tunasaladfood),
            title = "Tuna Salad",
            description = "This is Tuna Salad.",
            nav = nav
        )
        MyRecipeResult(
            image = painterResource(R.drawable.tunasaladfood),
            title = "Tuna Salad",
            description = "This is Tuna Salad.",
            nav = nav
        )
        MyRecipeResult(
            image = painterResource(R.drawable.tunasaladfood),
            title = "Tuna Salad",
            description = "This is Tuna Salad.",
            nav = nav
        )
        MyRecipeResult(
            image = painterResource(R.drawable.tunasaladfood),
            title = "Tuna Salad",
            description = "This is Tuna Salad.",
            nav = nav
        )
    }
}

@Composable
fun MyRecipeResult(image: Painter, title: String, description: String, nav: NavHostController) {
    Row(
        modifier = Modifier
            .padding(5.dp)
            .background(Color.White)
            .fillMaxWidth()
            .height(100.dp),
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
                IconButton(onClick = { nav.navigate("detailRecipe")  }) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = "Info Icon",
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Outlined.FavoriteBorder,
                        contentDescription = "Heart Icon",
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun PreviewSearchResultScreen() {
    SearchResultScreen(nav = rememberNavController())
}


