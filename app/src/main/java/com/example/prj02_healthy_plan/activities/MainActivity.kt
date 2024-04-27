package com.example.prj02_healthy_plan.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.prj02_healthy_plan.ui.theme.Prj02_Healthy_PlanTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        setContent {
            Prj02_Healthy_PlanTheme {
                val username = remember {
                    mutableStateOf("");
                }
                val password = remember {
                    mutableStateOf("")
                }
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(text = "Đăng ký")
                        TextField(value = username.value, onValueChange = {username.value = it}, label = { Text("Username")})
                        TextField(value = password.value, onValueChange = {password.value = it}, label = { Text("Password")})
                        Button(onClick = {
                            auth.createUserWithEmailAndPassword(username.value, password.value)
                                .addOnCompleteListener(this@MainActivity) { task ->
                                    if (task.isSuccessful) {
                                        // Sign in success, update UI with the signed-in user's information
                                        val user = auth.currentUser
                                        if (user != null) {
                                            Toast.makeText(
                                                baseContext,
                                                "Success" + user.email.toString(),
                                                Toast.LENGTH_SHORT,
                                            ).show()
                                        }
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(
                                            baseContext,
                                            "Authentication failed.",
                                            Toast.LENGTH_SHORT,
                                        ).show()
                                    }
                                }
                        }) {
                            Text(text = "Sign Up")
                        }
                        Text(text = "Đăng nhập")
                        Button(onClick = {
                            Toast.makeText(
                                baseContext,
                                "Chưa làm đâu hihi",
                                Toast.LENGTH_SHORT,
                            ).show()
                        }) {
                            Text(text = "Sign In")
                        }
                    }
                }
            }
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