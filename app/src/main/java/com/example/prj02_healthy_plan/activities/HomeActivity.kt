package com.example.prj02_healthy_plan.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.prj02_healthy_plan.R
import com.example.prj02_healthy_plan.ui.theme.MoreTabUI
import com.example.prj02_healthy_plan.ui.theme.Prj02_Healthy_PlanTheme
import com.example.prj02_healthy_plan.ui.theme.Screens
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class HomeActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
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

@Composable
fun AppNavBar() {
    val navigationController = rememberNavController()
    val context = LocalContext.current
    val selected = remember { mutableStateOf(Icons.Default.Home) }

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
                        .weight(1f)
                        .padding(10.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    FloatingActionButton(onClick = { /*TODO*/ }, modifier = Modifier.size(48.dp), containerColor = Color.Green) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = Color.Black)
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
                composable(Screens.Home.screen) {TungAnh()}
                composable(Screens.Diary.screen) { Giang()}
                composable(Screens.Explore.screen) { ChienTa()}
                composable(Screens.More.screen) { MoreTabUI(auth = FirebaseAuth.getInstance(), context = context) }
        }
    }
}

@Composable
fun TungAnh() {
    Box(
modifier = Modifier
    .fillMaxWidth()
    .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Tung Anh")
    }
}

@Composable
fun Giang() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Giang")
    }
}


//@Composable
//fun DongDuong(auth: FirebaseAuth, context: Context) {
//    Column {
//        Text(text = " Xin chao : " + auth.currentUser?.email.toString())
//        Button(
//            onClick = {
//                auth.signOut()
//                if (context is Activity) {
//                    val intent = Intent(context, MainActivity::class.java)
//                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
//                    context.startActivity(intent)
//                    context.finish()
//                }
//            }
//        ) {
//            Text(text = "Log Out")
//        }
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(32.dp),
//            contentAlignment = Alignment.Center
//        ) {
//            Text(text = "DongDuong")
//        }
//    }
//}

@Composable
fun ChienTa() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Chien Ta")
    }
}