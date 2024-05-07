package com.example.prj02_healthy_plan.ui.theme

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.ArrowDropDown
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserAddFoodScreen(nav: NavHostController) {
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
                            text = "Breakfast",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "DropDown Icon",
                            tint = Color.Black,
                            modifier = Modifier
                                .size(24.dp)
                        )
                    }
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

            UserAddFoodTabScreen()
        }
    }
}

@Composable
fun UserAddFoodTabScreen() {
    var tabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("All", "History")
    val userAddFoodTabScreenScrollState = rememberScrollState()

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
            0 -> AllFoodScreen(scrollState = userAddFoodTabScreenScrollState)
            1 -> HistoryAddScreen(scrollState = userAddFoodTabScreenScrollState)
        }
    }
}

@Composable
fun AllFoodScreen(scrollState: ScrollState) {
    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
    ) {
        FoodCanAdd(name = "Large Size Egg", amount = "3 eggs", cal = 273)
        FoodCanAdd(name = "Large Size Egg", amount = "3 eggs", cal = 273)
        FoodCanAdd(name = "Large Size Egg", amount = "3 eggs", cal = 273)
        FoodCanAdd(name = "Large Size Egg", amount = "3 eggs", cal = 273)
        FoodCanAdd(name = "Large Size Egg", amount = "3 eggs", cal = 273)
        FoodCanAdd(name = "Large Size Egg", amount = "3 eggs", cal = 273)
        FoodCanAdd(name = "Large Size Egg", amount = "3 eggs", cal = 273)
        FoodCanAdd(name = "Large Size Egg", amount = "3 eggs", cal = 273)
        FoodCanAdd(name = "Large Size Egg", amount = "3 eggs", cal = 273)
        FoodCanAdd(name = "Large Size Egg", amount = "3 eggs", cal = 273)
        FoodCanAdd(name = "Large Size Egg", amount = "3 eggs", cal = 273)
        FoodCanAdd(name = "Large Size Egg", amount = "3 eggs", cal = 273)
    }
}

@Composable
fun HistoryAddScreen(scrollState: ScrollState) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "There is no history add!",
            fontSize = 26.sp,
            fontWeight = FontWeight.Light
        )
    }
}

@Composable
fun FoodCanAdd(name: String, amount: String, cal: Number) {
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
                text = amount,
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
                tint = Color.Green,
                modifier = Modifier.size(30.dp)
            )
        }
    }
}

@Preview
@Composable
fun Test() {
    val navigationController = rememberNavController()
    UserAddFoodScreen(navigationController)
}