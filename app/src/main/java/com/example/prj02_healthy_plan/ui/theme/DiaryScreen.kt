package com.example.prj02_healthy_plan.ui.theme

import android.health.connect.datatypes.HeartRateRecord
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Giang(nav: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(245, 250, 255)),
                title = {
                    Text("Diary", style = TextStyle(fontSize = 25.sp, fontWeight = FontWeight.Bold))
                },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Handle filter click here */ }) {
                        Icon(Icons.Default.List, contentDescription = "Filter")
                    }
                }
            )
        }
    ) {innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color(245, 250, 255))) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding)
                    .background(Color(245, 250, 255)),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { /* Handle filter click here */ }) {
                    Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Filter")
                }

                Text(text = "Today")

                IconButton(onClick = { /* Handle filter click here */ }) {
                    Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Filter")
                }
            }
            Column(
                modifier = Modifier
                    .padding(top = 120.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                RoundedBox {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Calories consumed", style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black))
                            Text("60%", style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black))
                        }
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 15.dp)
                            .height(30.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(GreenMain)
                        ) {
                            LinearProgressIndicator(
                                color = GreenMain,
                                progress = 0.6f,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(30.dp)
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 5.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("2000 Goal", style = TextStyle(fontSize = 15.sp, color = Color.Black))
                            Text("-", style = TextStyle(fontSize = 15.sp, color = Color.Black))
                            Text("1800 Food", style = TextStyle(fontSize = 15.sp, color = Color.Black))
                            Text("=", style = TextStyle(fontSize = 15.sp, color = Color.Black))
                            Text("200 Remaining", style = TextStyle(fontSize = 15.sp, color = Color.Black))
                        }
                    }
                }
                HeaderRoundedBox (heading = "Breakfast") {
                    FoodDescriptionDiary()
                    FoodDescriptionDiary()
                    FoodDescriptionDiary()
                }

                HeaderRoundedBox (heading = "Lunch") {
                    FoodDescriptionDiary()
                    FoodDescriptionDiary()
                    FoodDescriptionDiary()
                }

                HeaderRoundedBox(heading = "Dinner") {
                    FoodDescriptionDiary()
                    FoodDescriptionDiary()
                    FoodDescriptionDiary()
                }

                HeaderRoundedBox(heading = "Snack") {
                    FoodDescriptionDiary()
                    FoodDescriptionDiary()
                    FoodDescriptionDiary()
                }

            }
        }
    }
}

@Composable
fun RoundedBox(content: @Composable () -> Unit) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = Color.White,
        modifier = Modifier.padding(10.dp)
    ) {
        Box(modifier = Modifier.padding(10.dp)) {
            content()
        }
    }
}

@Composable
fun HeaderRoundedBox(heading: String, content: @Composable () -> Unit) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = Color.White,
        modifier = Modifier.padding(10.dp)
    ) {
        Column {
            Row (
                Modifier
                    .fillMaxWidth()
                    .background(GreenMain)
                    .padding(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Outlined.ShoppingCart, contentDescription = null, tint = Color.White, modifier = Modifier.fillMaxHeight())
                Column (
                    modifier = Modifier.padding(start = 10.dp)

                ) {
                    Text(text = heading, style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White))
                    Text(text = "2 tỷ calories on 200 calories", style = TextStyle(fontSize = 15.sp, color = Color.White))
                }
            }
            Column(modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, top = 20.dp)
                .fillMaxWidth()) {
                content()
            }
        }

    }
}

@Composable
fun FoodDescriptionDiary() {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 30.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text("Voi chín ngà", style = TextStyle(fontSize = 15.sp, color = Color.Black))
            Text(text = "1 con")
        }
        Row {
            Text("200 cal")
        }
    }
}

