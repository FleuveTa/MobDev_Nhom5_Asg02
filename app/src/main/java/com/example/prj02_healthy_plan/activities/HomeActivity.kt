package com.example.prj02_healthy_plan.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import com.example.prj02_healthy_plan.R
import com.example.prj02_healthy_plan.ui.theme.Prj02_Healthy_PlanTheme
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
                Column {
                    Text(text = " Xin chao : " + auth.currentUser?.email.toString())
                    Button(
                        onClick = {
                            auth.signOut()
                            finish()
                        }
                    ) {
                        Text(text = "Log Out")
                    }
                }
            }
        }
    }
}