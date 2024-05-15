package com.example.prj02_healthy_plan.ui.theme

import android.health.connect.datatypes.HeartRateRecord
import android.util.Log
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
import androidx.compose.foundation.layout.width
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
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.rounded.Add
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.prj02_healthy_plan.RecipeFirebase
import com.example.prj02_healthy_plan.RecipeInDaily
import com.example.prj02_healthy_plan.uiModel.DailyDataViewModel
import com.example.prj02_healthy_plan.uiModel.UserViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Giang(nav: NavHostController) {
    val currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
    val selectedDateFormattedLabel = remember { mutableStateOf(currentDate) }

    val dailyDataViewModel: DailyDataViewModel = viewModel()
    val dailyData by dailyDataViewModel.dailyData.collectAsState()

    LaunchedEffect(Unit) {
        dailyDataViewModel.fetchDailyData(selectedDateFormattedLabel.value)
    }

    val userViewModel: UserViewModel = viewModel()
    val user = userViewModel.state.value

    val breakfastRecipes = remember { mutableStateListOf<RecipeFirebase>() }
    val lunchRecipes = remember { mutableStateListOf<RecipeFirebase>() }
    val dinnerRecipes = remember { mutableStateListOf<RecipeFirebase>() }
    val snacksRecipes = remember { mutableStateListOf<RecipeFirebase>() }

    val totalBreakfast = remember { mutableDoubleStateOf(0.0) }
    val totalLunch = remember { mutableDoubleStateOf(0.0) }
    val totalDinner = remember { mutableDoubleStateOf(0.0) }
    val totalSnacks = remember { mutableDoubleStateOf(0.0) }

    LaunchedEffect(dailyData) {
        breakfastRecipes.clear()
        lunchRecipes.clear()
        dinnerRecipes.clear()
        snacksRecipes.clear()

        breakfastRecipes.addAll(fetchRecipes(dailyData.breakfast))
        lunchRecipes.addAll(fetchRecipes(dailyData.lunch))
        dinnerRecipes.addAll(fetchRecipes(dailyData.dinner))
        snacksRecipes.addAll(fetchRecipes(dailyData.snacks))

        totalBreakfast.doubleValue = calculateTotalCalories(breakfastRecipes, dailyData.breakfast)
        totalLunch.doubleValue = calculateTotalCalories(lunchRecipes, dailyData.lunch)
        totalDinner.doubleValue = calculateTotalCalories(dinnerRecipes, dailyData.dinner)
        totalSnacks.doubleValue = calculateTotalCalories(snacksRecipes, dailyData.snacks)
    }



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

            val percent = (dailyData.intake?.get(0) ?: 0.0) * 100 / (user.caloriesGoal ?: 1)
            val formattedPercent = String.format("%.2f", percent).toFloat()
            val remaining = (user.caloriesGoal ?: 0) -  (dailyData.intake?.get(0)?.toInt() ?: 0)

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
                            Text("${formattedPercent}%", style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black))
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
                                progress = (percent/100).toFloat(),
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
                            Text((user.caloriesGoal?.toString() ?: "Fetching"), style = TextStyle(fontSize = 15.sp, color = Color.Black))
                            Text("-", style = TextStyle(fontSize = 15.sp, color = Color.Black))
                            Text((dailyData.intake?.get(0)?.toInt().toString()), style = TextStyle(fontSize = 15.sp, color = Color.Black))
                            Text("=", style = TextStyle(fontSize = 15.sp, color = Color.Black))
                            Text("$remaining Remaining", style = TextStyle(fontSize = 15.sp, color = Color.Black))
                        }
                    }
                }

                HeaderRoundedBox (heading = "Breakfast", nav = nav, totalBreakfast.doubleValue) {
                    for ((index, recipe) in breakfastRecipes.withIndex()) {
                        Log.d("Recipe", recipe.toString())
                        dailyData.breakfast?.get(index)
                            ?.let { it.quantity?.let { it1 ->
                                val totalCalories = it1 * (recipe.nutrition?.get(0) ?: 0.0)
                                FoodDescriptionDiary(recipe.name ?: "Nun",
                                    it1, totalCalories)
                            } }
                    }
                }

                HeaderRoundedBox (heading = "Lunch", nav = nav, totalLunch.doubleValue) {
                    for ((index, recipe) in lunchRecipes.withIndex()) {
                        Log.d("Recipe", recipe.toString())
                        dailyData.lunch?.get(index)
                            ?.let { it.quantity?.let { it1 ->
                                val totalCalories = it1 * (recipe.nutrition?.get(0) ?: 0.0)
                                FoodDescriptionDiary(recipe.name ?: "Nun",
                                    it1, totalCalories)
                            } }
                    }
                }

                HeaderRoundedBox(heading = "Dinner", nav = nav, totalDinner.doubleValue) {
                    for ((index, recipe) in dinnerRecipes.withIndex()) {
                        Log.d("Recipe", recipe.toString())
                        dailyData.dinner?.get(index)
                            ?.let { it.quantity?.let { it1 ->
                                val totalCalories = it1 * (recipe.nutrition?.get(0) ?: 0.0)
                                FoodDescriptionDiary(recipe.name ?: "Nun",
                                    it1, totalCalories)
                            } }
                    }
                }

                HeaderRoundedBox(heading = "Snack", nav = nav, totalSnacks.doubleValue) {
                    for ((index, recipe) in snacksRecipes.withIndex()) {
                        Log.d("Recipe", recipe.toString())
                        dailyData.snacks?.get(index)
                            ?.let { it.quantity?.let { it1 ->
                                val totalCalories = it1 * (recipe.nutrition?.get(0) ?: 0.0)
                                FoodDescriptionDiary(recipe.name ?: "Nun",
                                    it1, totalCalories)
                            } }
                    }
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
fun HeaderRoundedBox(heading: String, nav: NavHostController,total: Double, content: @Composable () -> Unit) {
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
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier
                        .width(160.dp)
                        .fillMaxHeight(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Outlined.ShoppingCart, contentDescription = null, tint = Color.White, modifier = Modifier.fillMaxHeight())
                    Column (
                        modifier = Modifier.padding(start = 10.dp)

                    ) {
                        Text(text = heading, style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White))
                        Text(text = "$total calories", style = TextStyle(fontSize = 15.sp, color = Color.White))
                    }
                }

                IconButton(onClick = { nav.navigate("userAddFood") }) {
                    Icon(imageVector = Icons.Outlined.AddCircle, contentDescription = "Add Icon",
                        tint = Color.White)
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
fun FoodDescriptionDiary(name: String = "Nun", quantity: Int = 1, cal: Double = 0.0) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 30.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(name, style = TextStyle(fontSize = 15.sp, color = Color.Black))
            Text(text = "$quantity")
        }
        Row {
            Text("$cal cal")
        }
    }
}

suspend fun fetchRecipes(recipesInDaily: List<RecipeInDaily>?): List<RecipeFirebase> {
    val recipes = mutableListOf<RecipeFirebase>()
    recipesInDaily?.forEach { recipeInDaily ->
        val recipeSnapshot = recipeInDaily.recipe?.get()?.await()
        val recipe = recipeSnapshot?.toObject(RecipeFirebase::class.java)
        if (recipe != null) {
            recipes.add(recipe)
        }
    }
    return recipes
}

fun calculateTotalCalories(recipes: List<RecipeFirebase>, recipesInDaily: List<RecipeInDaily>?): Double {
    var total = 0.0
    for ((index, recipe) in recipes.withIndex()) {
        val quantity = recipesInDaily?.get(index)?.quantity ?: 1
        total += (recipe.nutrition?.get(0) ?: 0.0) * quantity
    }
    return total
}

