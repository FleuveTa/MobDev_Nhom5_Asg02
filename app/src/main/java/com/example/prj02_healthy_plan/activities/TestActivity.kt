package com.example.prj02_healthy_plan.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import com.example.prj02_healthy_plan.R

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { 
            Text(text = "This is Home Screen")
            Button(onClick = {}) {
                Text(text = "Log Out")
            }
        }
    }
}