package com.example.prj02_healthy_plan.activities
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.prj02_healthy_plan.R
import com.example.prj02_healthy_plan.ui.theme.ChienTa
import com.example.prj02_healthy_plan.ui.theme.DetailRecipeScreen
import com.example.prj02_healthy_plan.ui.theme.Giang
import com.example.prj02_healthy_plan.ui.theme.GreenMain
import com.example.prj02_healthy_plan.ui.theme.HomeUI
import com.example.prj02_healthy_plan.ui.theme.MoreTabUI
import com.example.prj02_healthy_plan.ui.theme.Prj02_Healthy_PlanTheme
import com.example.prj02_healthy_plan.ui.theme.ScanScreen
import com.example.prj02_healthy_plan.ui.theme.ViewRecipeResultScreen
import com.example.prj02_healthy_plan.ui.theme.Screens
import com.example.prj02_healthy_plan.ui.theme.SearchResultScreen
import com.example.prj02_healthy_plan.ui.theme.SecurityUI
import com.example.prj02_healthy_plan.ui.theme.UserAddFoodScreen
import com.example.prj02_healthy_plan.ui.theme.UserInforUI
import com.example.prj02_healthy_plan.ui.theme.UserSearchIngredientScreen
import com.example.prj02_healthy_plan.uiModel.IngredientViewModel
import com.example.prj02_healthy_plan.uiModel.RecipeViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth

        setContent {
            Prj02_Healthy_PlanTheme {
                AppNavBar()
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun AppNavBar() {
    val currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
    val selectedDateFormattedLabel = remember { mutableStateOf(currentDate) }
    val navigationController = rememberNavController()
    val context = LocalContext.current
    val selected = remember { mutableStateOf(Icons.Default.Home) }
    val expanded = remember { mutableStateOf(false) }
    val ingredientSearchResult = remember { mutableStateOf("") }
    val recipeSearchName = remember { mutableStateOf("") }
    val recipeViewModel = remember { RecipeViewModel() }
    val ingredientViewModel = remember { IngredientViewModel() }
    LaunchedEffect(navigationController) {
        navigationController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.route) {
                Screens.Home.screen -> selected.value = Icons.Default.Home
                Screens.Diary.screen -> selected.value = Icons.Default.DateRange
                Screens.Explore.screen -> selected.value = Icons.Default.Search
                Screens.More.screen -> selected.value = Icons.Default.Settings
            }
        }
    }

    Box {
        Scaffold(
            bottomBar = {
                BottomAppBar(
                    containerColor = Color.White
                ) {
                    IconButton(
                        onClick = {
                            selected.value = Icons.Default.Home
                            navigationController.navigate(Screens.Home.screen)
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Home, contentDescription = "Home", modifier = Modifier.size(24.dp),
                            tint = if (selected.value == Icons.Default.Home) Color.Green else Color.Gray)
                    }

                    IconButton(
                        onClick = {
                            selected.value = Icons.Default.DateRange
                            navigationController.navigate(Screens.Diary.screen)
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.DateRange, contentDescription = "Diary", modifier = Modifier.size(24.dp),
                            tint = if (selected.value == Icons.Default.DateRange) Color.Green else Color.Gray)
                    }

                    Box(
                        modifier = Modifier
                            .weight(1.5f)
                            .padding(10.dp, bottom = 20.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        FloatingActionButton(onClick = { expanded.value = !expanded.value }, modifier = Modifier.size(40.dp), containerColor = Color.Green) {
                            Icon(Icons.Filled.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(35.dp))
                        }
                    }

                    IconButton(
                        onClick = {
                            selected.value = Icons.Default.Search
                            navigationController.navigate(Screens.Explore.screen)
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Search, contentDescription = "Explore", modifier = Modifier.size(24.dp),
                            tint = if (selected.value == Icons.Default.Search) Color.Green else Color.Gray)
                    }

                    IconButton(
                        onClick = {
                            selected.value = Icons.Default.Settings
                            navigationController.navigate(Screens.More.screen)
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Settings, contentDescription = "More", modifier = Modifier.size(24.dp),
                            tint = if (selected.value == Icons.Default.Settings) Color.Green else Color.Gray)
                    }
                }
            }
        ) {paddingValues ->
            NavHost(navController = navigationController,
                startDestination = Screens.Home.screen,
                modifier = Modifier.padding(paddingValues)) {
                composable(Screens.Home.screen) { TungAnh(nav = navigationController, date = selectedDateFormattedLabel)}
                composable(Screens.Diary.screen) { Giang(nav = navigationController, date = selectedDateFormattedLabel)}
                composable(Screens.Explore.screen) { ChienTa(nav = navigationController, recipeSearchName, ingredientViewModel, recipeViewModel) }
                composable(Screens.More.screen) { MoreTabUI(auth = FirebaseAuth.getInstance(), context = context, nav = navigationController)}
                composable(Screens.UserInfor.screen) { UserInforUI(navController = navigationController)}
                composable(Screens.DetailRecipe.screen) {DetailRecipeScreen(nav = navigationController, recipeViewModel)}
                composable(Screens.ViewRecipeResult.screen) { ViewRecipeResultScreen(nav = navigationController, recipeViewModel, ingredientViewModel) }
                composable(Screens.Scan.screen) { ScanScreen(nav = navigationController, ingredientSearchResult, ingredientViewModel) }
                // composable(Screens.SearchChoice.screen) { SearchChoiceScreen(nav = navigationController) }
                composable(Screens.SearchResult.screen) { SearchResultScreen(nav = navigationController, recipeSearchName, recipeViewModel) }
                composable(Screens.UserAddFood.screen) { UserAddFoodScreen(nav = navigationController, date = selectedDateFormattedLabel) }
                composable(Screens.UserAddIngredient.screen) { UserSearchIngredientScreen( nav = navigationController, ingredientViewModel) }
                composable(Screens.SecurityUI.screen) { SecurityUI( nav = navigationController) }
            }
        }
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(start = 10.dp)
        ) {
            Text(text = "What would like to track ?", style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold))
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        navigationController.navigate("userAddFood")
                        Toast
                            .makeText(context, "Track Food", Toast.LENGTH_SHORT)
                            .show()
                    },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Track Food")
                IconButton(onClick = { navigationController.navigate("userAddFood") }) {
                    Icon(Icons.Filled.Add, contentDescription = null, tint = GreenMain)
                }
            }
            Divider()
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        Toast
                            .makeText(context, "Scan Food", Toast.LENGTH_SHORT)
                            .show()
                    },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Scan Food")
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.Filled.Search, contentDescription = null, tint = GreenMain)
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun TungAnh(nav: NavController, date: MutableState<String>) {
    HomeUI(nav, date)
}