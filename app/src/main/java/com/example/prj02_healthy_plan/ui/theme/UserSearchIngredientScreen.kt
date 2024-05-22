package com.example.prj02_healthy_plan.ui.theme

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.prj02_healthy_plan.Ingredient
import com.example.prj02_healthy_plan.uiModel.IngredientViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserSearchIngredientScreen(nav: NavHostController, ingredientViewModel: IngredientViewModel){
    val allIngredientList by ingredientViewModel.ingredientList.collectAsState()
    val (filteredIngredientList, setFilteredIngredientList) = remember { mutableStateOf(allIngredientList) }
    val (isSearching, setIsSearching) = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
       ingredientViewModel.fetchIngredients()
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
                            "Add Ingredient",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                },
                navigationIcon = {
                    IconButton(onClick = { nav.navigate("explore") }, modifier = Modifier.testTag("UserSearchIngredientBackButton")) {
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
                    setIsSearching(true) // Mark as searching
                    setFilteredIngredientList(allIngredientList.filter { it.name?.contains(query, ignoreCase = true) == true })
                } else {
                    setIsSearching(false) // Mark as not searching
                    setFilteredIngredientList(allIngredientList)
                }
            }

            Spacer(modifier = Modifier.height(5.dp))

            // Show the list of ingredients
            if (!isSearching || filteredIngredientList.isEmpty()) {
                UserAddIngredientTabScreen(viewModel = ingredientViewModel)
            } else {
                // Show the filtered list of ingredients
                AllIngredientScreen(scrollState = rememberScrollState(), ingredientList = filteredIngredientList, viewModel = ingredientViewModel)
            }
        }
    }
}


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun UserAddIngredientTabScreen(viewModel: IngredientViewModel) {
    val userAddIngredientTabScreenScrollState = rememberScrollState()

    Column(modifier = Modifier.fillMaxWidth()) {
        AllIngredientScreen(scrollState = userAddIngredientTabScreenScrollState, ingredientList = viewModel.ingredientList.value, viewModel = viewModel)
    }
}

@Composable
fun AllIngredientScreen(scrollState: ScrollState, ingredientList: List<Ingredient>, viewModel: IngredientViewModel) {
    val userIngredients = viewModel.userIngredients
    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
    ) {
        ingredientList.forEach { ingredient->
            IngredientCanAdd(
                name = ingredient.name ?: "",
                unit = ingredient.unit ?: "",
                cal = ingredient.nutrition?.get(0) ?: 0.0,
                isAdded = userIngredients.contains(ingredient),
                isReadOnly = false
            ) {
                viewModel.toggleIngredient(ingredient)
            }
        }
    }
}
@Composable
fun IngredientCanAdd(name: String, unit: String, cal: Number, isAdded: Boolean, isReadOnly: Boolean, onToggle: (Boolean) -> Unit) {
    var added by remember { mutableStateOf(isAdded) }

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
                text = unit,
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
            // Add button here, when click on it, the ingredient will be added to the user's ingredient list and
            // the icon will change to a remove icon, when click on it again, the ingredient will be removed from the user's ingredient list
            IconButton(onClick = {
                if (!isReadOnly) {
                    added = !added
                }
                onToggle(added)
                if (added) {
                    Log.d("Add", "Added $name")
                } else {
                    Log.d("Remove", "Removed $name")
                }
            }, modifier = Modifier.testTag("ingredientAddButton")) {
                Icon(
                    imageVector = if (added) Icons.Default.RemoveCircle else Icons.Default.AddCircle,
                    contentDescription = if (added) "Remove" else "Add",
                    tint = if (added) Color.Red else GreenMain,
                    modifier = Modifier.size(30.dp))
            }
        }
    }
}