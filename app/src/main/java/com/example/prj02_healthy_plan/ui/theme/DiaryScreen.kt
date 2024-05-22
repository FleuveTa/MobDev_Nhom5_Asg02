package com.example.prj02_healthy_plan.ui.theme

import PastOrPresentSelectableDates
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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import convertMillisToDate
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Giang(nav: NavHostController, date: MutableState<String>) {
    val dailyDataViewModel: DailyDataViewModel = viewModel()
    val dailyData by dailyDataViewModel.dailyData.collectAsState()

    val openDialog = remember { mutableStateOf(false) }
    val selectedDateLabel = remember { mutableStateOf("Today") }
    val datePickerState = rememberDatePickerState(
        selectableDates = PastOrPresentSelectableDates
    )
    val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }
    val calendarPickerMainColor = Color(0xFF722276)

    LaunchedEffect(date.value) {
        dailyDataViewModel.fetchDailyData(date.value)
        val parsedDate = dateFormat.parse(date.value)
        parsedDate?.let {
            datePickerState.selectedDateMillis = it.time
            selectedDateLabel.value = it.time.convertMillisToDate()
            Log.d("Converted", it.time.convertMillisToDate())
        }
    }

    val userViewModel: UserViewModel = viewModel()
    val user = userViewModel.state.value

    val breakfastRecipes = remember { mutableStateListOf<RecipeFirebase>() }
    val lunchRecipes = remember { mutableStateListOf<RecipeFirebase>() }
    val dinnerRecipes = remember { mutableStateListOf<RecipeFirebase>() }
    val snacksRecipes = remember { mutableStateListOf<RecipeFirebase>() }

    val totalBreakfast = remember { mutableStateListOf<Double>() }
    val totalLunch = remember { mutableStateListOf<Double>() }
    val totalDinner = remember { mutableStateListOf<Double>() }
    val totalSnacks = remember { mutableStateListOf<Double>() }

    LaunchedEffect(dailyData) {
        breakfastRecipes.clear()
        lunchRecipes.clear()
        dinnerRecipes.clear()
        snacksRecipes.clear()

        totalBreakfast.clear()
        totalLunch.clear()
        totalDinner.clear()
        totalSnacks.clear()

        breakfastRecipes.addAll(fetchRecipes(dailyData.breakfast))
        lunchRecipes.addAll(fetchRecipes(dailyData.lunch))
        dinnerRecipes.addAll(fetchRecipes(dailyData.dinner))
        snacksRecipes.addAll(fetchRecipes(dailyData.snacks))

        totalBreakfast.addAll(calculateTotalCalories(breakfastRecipes, dailyData.breakfast))
        totalLunch.addAll(calculateTotalCalories(lunchRecipes, dailyData.lunch))
        totalDinner.addAll(calculateTotalCalories(dinnerRecipes, dailyData.dinner))
        totalSnacks.addAll(calculateTotalCalories(snacksRecipes, dailyData.snacks))


        dailyDataViewModel.updateTotalIntake(
            totalBreakfast[0] + totalLunch[0] + totalDinner[0] + totalSnacks[0],
            totalBreakfast[1] + totalLunch[1] + totalDinner[1] + totalSnacks[1],
            totalBreakfast[2] + totalLunch[2] + totalDinner[2] + totalSnacks[2],
            totalBreakfast[3] + totalLunch[3] + totalDinner[3] + totalSnacks[3],
            date.value
        )
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
                    TextButton(
                        onClick = {openDialog.value = true}
                    ) {

                        Text(
                            text = selectedDateLabel.value,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold
                        )

                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "DropDown Icon")
                    }
                }
            )
        }
    ) {innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color(245, 250, 255))
            .padding(innerPadding)
        ) {
            val percent = (dailyData.intake?.get(0) ?: 0.0) * 100 / (user.caloriesGoal ?: 1)
            val formattedPercent = String.format("%.2f", percent).toFloat()
            val remaining = (user.caloriesGoal ?: 0) -  (dailyData.intake?.get(0)?.toInt() ?: 0)
            Column(
                modifier = Modifier
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

                HeaderRoundedBox (heading = "Breakfast", nav = nav, totalBreakfast.firstOrNull() ?: 0.0) {
                    for ((index, recipe) in breakfastRecipes.withIndex()) {
                        Log.d("Recipe", recipe.toString())
                        if (dailyData.breakfast?.isNotEmpty() == true && index < (dailyData.breakfast?.size ?: 0)) {
                            dailyData.breakfast?.get(index)
                                ?.let { it.quantity?.let { it1 ->
                                    val totalCalories = it1 * (recipe.nutrition?.get(0) ?: 0.0)
                                    FoodDescriptionDiary(recipe.name ?: "Nun",
                                        it1, totalCalories)
                                } }
                        }
                    }
                }

                HeaderRoundedBox (heading = "Lunch", nav = nav, totalLunch.firstOrNull() ?: 0.0) {
                    for ((index, recipe) in lunchRecipes.withIndex()) {
                        Log.d("Recipe", recipe.toString())
                        if (dailyData.breakfast?.isNotEmpty() == true && index < (dailyData.breakfast?.size ?: 0)) {
                            dailyData.lunch?.get(index)
                                ?.let {
                                    it.quantity?.let { it1 ->
                                        val totalCalories = it1 * (recipe.nutrition?.get(0) ?: 0.0)
                                        FoodDescriptionDiary(
                                            recipe.name ?: "Nun",
                                            it1, totalCalories
                                        )
                                    }
                                }
                        }
                    }
                }

                HeaderRoundedBox(heading = "Dinner", nav = nav, totalDinner.firstOrNull() ?: 0.0) {
                    for ((index, recipe) in dinnerRecipes.withIndex()) {
                        Log.d("Recipe", recipe.toString())
                        if (dailyData.breakfast?.isNotEmpty() == true && index < (dailyData.breakfast?.size ?: 0)) {
                            dailyData.dinner?.get(index)
                                ?.let {
                                    it.quantity?.let { it1 ->
                                        val totalCalories = it1 * (recipe.nutrition?.get(0) ?: 0.0)
                                        FoodDescriptionDiary(
                                            recipe.name ?: "Nun",
                                            it1, totalCalories
                                        )
                                    }
                                }
                        }
                    }
                }

                HeaderRoundedBox(heading = "Snack", nav = nav, totalSnacks.firstOrNull() ?: 0.0) {
                    for ((index, recipe) in snacksRecipes.withIndex()) {
                        Log.d("Recipe", recipe.toString())
                        if (dailyData.breakfast?.isNotEmpty() == true && index < (dailyData.breakfast?.size ?: 0)) {
                            dailyData.snacks?.get(index)
                                ?.let {
                                    it.quantity?.let { it1 ->
                                        val totalCalories = it1 * (recipe.nutrition?.get(0) ?: 0.0)
                                        FoodDescriptionDiary(
                                            recipe.name ?: "Nun",
                                            it1, totalCalories
                                        )
                                    }
                                }
                        }
                    }
                }

            }
        }

        if (openDialog.value) {
            DatePickerDialog(
                colors = DatePickerDefaults.colors(
                    containerColor = Color(0xFFF5F0FF),
                ),
                onDismissRequest = {
                    // Action when the dialog is dismissed without selecting a date
                    openDialog.value = false
                },
                confirmButton = {
                    // Confirm button with custom action and styling
                    TextButton(
                        onClick = {
                            // Action to set the selected date and close the dialog
                            openDialog.value = false
                            datePickerState.selectedDateMillis?.let {
                                date.value = dateFormat.format(Date(it))
                            }
                            selectedDateLabel.value =
                                datePickerState.selectedDateMillis?.convertMillisToDate() ?: "Today"
                        }
                    ) {
                        Text("OK", color = calendarPickerMainColor)
                    }
                },
                dismissButton = {
                    // Dismiss button to close the dialog without selecting a date
                    TextButton(
                        onClick = {
                            openDialog.value = false
                        }
                    ) {
                        Text("CANCEL", color = calendarPickerMainColor)
                    }
                }
            ) {
                // The actual DatePicker component within the dialog
                DatePicker(
                    state = datePickerState,
                    colors = DatePickerDefaults.colors(
                        selectedDayContainerColor = calendarPickerMainColor,
                        selectedDayContentColor = Color.White,
                        selectedYearContainerColor = calendarPickerMainColor,
                        selectedYearContentColor = Color.White,
                        todayContentColor = calendarPickerMainColor,
                        todayDateBorderColor = calendarPickerMainColor
                    )
                )
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

//Tên là TotalNutrion mới đúng, nhưng chưa sửa vì khó merge :D
fun calculateTotalCalories(recipes: List<RecipeFirebase>, recipesInDaily: List<RecipeInDaily>?): List<Double> {
    var totalCalories = 0.0
    var totalProtein = 0.0
    var totalCarb = 0.0
    var totalFat = 0.0
    for ((index, recipe) in recipes.withIndex()) {
        val quantity = recipesInDaily?.get(index)?.quantity ?: 1
        totalCalories += (recipe.nutrition?.get(0) ?: 1.0) * quantity
        Log.d("Nutrition of ${recipe.name}", recipe.nutrition.toString() + " ,quantity: " + quantity)
        totalProtein += (recipe.nutrition?.get(1) ?: 0.0) * quantity
        totalCarb += (recipe.nutrition?.get(2) ?: 0.0) * quantity
        totalFat += (recipe.nutrition?.get(3) ?: 0.0) * quantity
    }
    Log.d("TotalNutrition", listOf(totalCalories, totalProtein, totalCarb, totalFat).toString())
    return listOf(totalCalories, totalProtein, totalCarb, totalFat)
}

