package com.example.prj02_healthy_plan.ui.theme

import android.util.Log
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
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
import androidx.compose.ui.platform.LocalContext
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
import com.example.prj02_healthy_plan.uiModel.RecipeViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserAddFoodScreen(nav: NavHostController, date: MutableState<String>){
    val viewRecipeModel: RecipeViewModel = viewModel()
    val allRecipeList by viewRecipeModel.recipeList.collectAsState()
    val (filteredRecipeList, setFilteredRecipeList) = remember { mutableStateOf(allRecipeList) }
    val (isSearching, setIsSearching) = remember { mutableStateOf(false) }
    var selectedMeal by remember { mutableStateOf("breakfast") }

    LaunchedEffect(key1 = Unit) {
        viewRecipeModel.fetchRecipes()
    }

    Log.d("Date for add", date.value)

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
                    var expanded by remember { mutableStateOf(false) }
                    var selectedMealLabel by remember { mutableStateOf("Breakfast") }
                    val options = listOf("Breakfast", "Lunch", "Dinner", "Snacks")

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = {expanded = it}
                    ) {
                        TextField(
                            value = selectedMealLabel + " ," + date.value,
                            label = { Text("Meal") },
                            onValueChange = {},
                            readOnly = true,
                            colors = ExposedDropdownMenuDefaults.textFieldColors(
                                unfocusedContainerColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent
                            ),
                            modifier = Modifier.menuAnchor(),
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                            }
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            options.forEach { label ->
                                DropdownMenuItem(
                                    onClick = {
                                        selectedMeal = label.lowercase(Locale.ROOT)
                                        Log.d("Meal : ", selectedMeal)
                                        selectedMealLabel = label
                                        expanded = false
                                    },
                                    text = { Text(label) }
                                )
                            }
                        }
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
            SearchBar(nav = nav) { query ->
                if (query.isNotEmpty()) {
                    setIsSearching(true) // Đánh dấu là đang tìm kiếm
                    setFilteredRecipeList(allRecipeList.filter { recipe ->
                        recipe.name?.contains(query, ignoreCase = true) == true
                    })
                } else {
                    setIsSearching(false) // Đánh dấu là không tìm kiếm
                    setFilteredRecipeList(allRecipeList)
                }
            }

            Spacer(modifier = Modifier.height(5.dp))

            if (!isSearching || filteredRecipeList.isEmpty()) {
                UserAddFoodTabScreen(recipeList = allRecipeList, date = date.value, meal = selectedMeal)
            } else {
                UserAddFoodTabScreen(recipeList = filteredRecipeList, date = date.value, meal = selectedMeal)
            }

        }
    }
}

@Composable
fun UserAddFoodTabScreen(recipeList: List<RecipeFirebase>, date: String, meal: String) {
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
            0 -> AllFoodScreen(scrollState = userAddFoodTabScreenScrollState, recipeList = recipeList, date, meal)
            1 -> HistoryAddScreen(scrollState = userAddFoodTabScreenScrollState)
        }
    }
}

@Composable
fun AllFoodScreen(scrollState: ScrollState, recipeList: List<RecipeFirebase>, date: String, meal: String) {
    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
    ) {
        recipeList.forEach { recipe ->
            FoodCanAdd(
                name = recipe.name ?: "",
                id = recipe.id ?: "",
                cal = recipe.nutrition?.get(0) ?: 0,
                date = date,
                meal = meal
            )
        }
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
fun FoodCanAdd(name: String, id: String, cal: Number, date: String, meal: String) {
    val dailyDataViewModel: DailyDataViewModel = viewModel()
    val db = Firebase.firestore
    val context = LocalContext.current
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
                text = id,
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

            val newRecipeInDaily = RecipeInDaily(
                recipe = db.collection("recipes").document(id),
                quantity = 1
            )

            IconButton(onClick = { dailyDataViewModel.updateMeal(meal, date, newRecipeInDaily, context)}) {
                Icon(
                    imageVector = Icons.Default.AddCircle,
                    contentDescription = "Add Icon",
                    tint = Color.Green,
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(nav: NavHostController, onSearch: (String) -> Unit) {
    var text by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    val items = remember { mutableStateListOf<String>() }

    androidx.compose.material3.SearchBar(
        modifier = Modifier
            .fillMaxWidth(),
        query = text,
        onQueryChange = {
            text = it
        },
        onSearch = {
            onSearch(text)
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

