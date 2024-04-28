package com.example.prj02_healthy_plan.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.prj02_healthy_plan.ui.theme.Prj02_Healthy_PlanTheme
import com.example.prj02_healthy_plan.ui.theme.SignUpUI
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class SignUpActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val context = baseContext;
        auth = Firebase.auth
        setContent {
            Prj02_Healthy_PlanTheme {
                val username = remember { mutableStateOf("") }
                val password = remember { mutableStateOf("") }
                SignUpUI(
                    username,
                    password,
                    signup = {
                        auth.createUserWithEmailAndPassword(username.value, password.value)
                                .addOnCompleteListener(this@SignUpActivity) { task ->
                                    if (task.isSuccessful) {
                                        val user = auth.currentUser
                                        if (user != null) {
                                            Toast.makeText(
                                                baseContext,
                                                "Success create user with email: " + user.email.toString(),
                                                Toast.LENGTH_SHORT,
                                            ).show()
                                        }
                                        startActivity(Intent(context, MainActivity::class.java))
                                    } else {
                                        Toast.makeText(
                                            baseContext,
                                            "Authentication failed.",
                                            Toast.LENGTH_SHORT,
                                        ).show()
                                    }
                                }
                    },
                    toSignin = {
                        val intent = Intent(context, MainActivity::class.java)
                        startActivity(intent)
                    })
            }
        }
    }
}