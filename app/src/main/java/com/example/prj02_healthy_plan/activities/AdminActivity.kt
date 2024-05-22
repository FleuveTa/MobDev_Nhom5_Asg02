package com.example.prj02_healthy_plan.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color.parseColor
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.BakeryDining
import androidx.compose.material.icons.outlined.FoodBank
import androidx.compose.material.icons.rounded.FoodBank
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.prj02_healthy_plan.ContextWrapper
import com.example.prj02_healthy_plan.ContextWrapperImpl
import com.example.prj02_healthy_plan.FirebaseAuthWrapper
import com.example.prj02_healthy_plan.FirebaseAuthWrapperImpl
import com.example.prj02_healthy_plan.RecipeFirebase
import com.example.prj02_healthy_plan.ui.theme.Content
import com.example.prj02_healthy_plan.ui.theme.Prj02_Healthy_PlanTheme
import com.example.prj02_healthy_plan.uiModel.RecipeViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AdminActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private val context: Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        val viewRecipeModel: RecipeViewModel by viewModels()

        setContent {
            AdminScreen(FirebaseAuthWrapperImpl(auth), ContextWrapperImpl(context), viewRecipeModel)
        }
    }
}

@Composable
fun AdminScreen(auth: FirebaseAuthWrapper, context: ContextWrapper, viewRecipeModel: RecipeViewModel) {
    val localContext = LocalContext.current
    val recipeList by viewRecipeModel.recipeList.collectAsState()

    LaunchedEffect(key1 = Unit) {
        viewRecipeModel.fetchRecipes()
    }
    Column(
        modifier = Modifier
            .padding(5.dp)
            .background(Color(245, 250, 255))
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "HealthyPlans",
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            color = Color(parseColor("#4ED22D")),
            modifier = Modifier
        )

        AdminSearchBar()

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Edit Recipes",
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp,
                color = Color(parseColor("#3B3B3B"))
            )

            IconButton(
                onClick = {
                    auth.signOut()
                    val c = context.getContext()
                    if (c is Activity) {
                        val intent = Intent(c, MainActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                        context.startActivity(intent)
                        context.finish()
                    } else {
                        Log.e("AdminActivity", "Context is not an Activity")
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Logout,
                    contentDescription = "Logout Icon",
                    modifier = Modifier
                        .size(30.dp)
                )
            }
        }

        Box(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth()
                .height(100.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White, RoundedCornerShape(20.dp))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(5.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.FoodBank,
                    contentDescription = "Food Icon",
                    tint = Color(parseColor("#4ED22D")),
                    modifier = Modifier
                        .size(55.dp)
                )

                Text(
                    text = "Add a new recipe",
                    fontWeight = FontWeight.Medium,
                    fontSize = 22.sp,
                    color = Color(parseColor("#3B3B3B"))
                )


                IconButton(
                    onClick = {
                        val intent = Intent(localContext, AddFoodActivity::class.java)
                        context.startActivity(intent)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.AddCircle,
                        contentDescription = "Add Icon",
                        tint = Color(parseColor("#4ED22D")),
                        modifier = Modifier
                            .size(40.dp)
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            for (recipe in recipeList) {
                FoodCanRemove(
                    name = recipe.name ?: "",
                    amount = recipe.description ?: "",
                    cal = recipe.nutrition?.get(0) ?: 0,
                    id = recipe.id ?: "",
                    model = viewRecipeModel
                )
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminSearchBar() {
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

    androidx.compose.material3.SearchBar(
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
fun FoodCanRemove(name: String, amount: String, cal: Number, id: String, model: RecipeViewModel) {
    Row(
        modifier = Modifier
            .padding(5.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(
                Color.White,
                RoundedCornerShape(15.dp)
            )
            .fillMaxWidth()
            .height(60.dp),
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

            IconButton(onClick = {
               model.deleteRecipe(id)
            }) {
                Icon(
                    imageVector = Icons.Filled.RemoveCircleOutline,
                    contentDescription = "Add Icon",
                    tint = Color.Red,
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    }
}