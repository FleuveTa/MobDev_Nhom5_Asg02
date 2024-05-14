package com.example.prj02_healthy_plan.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.prj02_healthy_plan.ui.theme.Prj02_Healthy_PlanTheme
import com.example.prj02_healthy_plan.ui.theme.SignInUI
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        val context = baseContext;
        setContent {
            Prj02_Healthy_PlanTheme {
                val username = remember { mutableStateOf("") }
                val password = remember { mutableStateOf("") }
                SignInUI(username, password, login = {
                    auth.signInWithEmailAndPassword(username.value, password.value)
                        .addOnCompleteListener(this@MainActivity) { task ->
                            if (task.isSuccessful) {
                                if (auth.currentUser?.email == "admin@gmail.com") {
                                    startActivity(Intent(context, AdminActivity::class.java))
                                } else {
                                    startActivity(Intent(context, HomeActivity::class.java))
                                }
                                Toast.makeText(
                                    baseContext,
                                    "Dang nhap thanh cong",
                                    Toast.LENGTH_SHORT,
                                ).show()
                            } else {
                                Toast.makeText(
                                    baseContext,
                                    "Co mot so van de gi do",
                                    Toast.LENGTH_SHORT,
                                ).show()
                            }
                        }
                }, toSignup = {
                    val intent = Intent(context, SignUpActivity::class.java)
                    startActivity(intent)
                },
                    auth = auth)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            Toast.makeText(
                baseContext,
                "Already logged in",
                Toast.LENGTH_SHORT,
            ).show()
            if (currentUser.email == "admin@gmail.com") {
                startActivity(Intent(baseContext, AdminActivity::class.java))
            } else {
                startActivity(Intent(baseContext, HomeActivity::class.java))
            }

        }
    }

    @Composable
    fun Greeting(name: String, modifier: Modifier = Modifier) {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        Prj02_Healthy_PlanTheme {
            Greeting("Android")
        }
    }
}