package com.example.prj02_healthy_plan.ui.theme

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SignInUI(username: MutableState<String>, password: MutableState<String>, login: () -> Unit, toSignup: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Welcome 👋",
                modifier = Modifier.padding(top = 30.dp),
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
            )
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(0.9f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = "To day is a new day. It's your day. You shape it.")
                Text(text = "Sign in to start becoming a better self.")
            }
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(0.9f),
            ) {
                Text(text = "Email")
                UsernameField(username)
            }
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(0.9f),
            ) {
                Text(text = "Password")
                PasswordField(password)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(text = "Forgot password?")
                }
                Button(
                    onClick = { login() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2BF361),
                    )
                ) {
                    Text(text = "Sign in")
                }
            }
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(0.9f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = "--------Or sign in with--------")
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = { /*TODO*/ },
                    ) {
                        Text(text = "Google")
                    }
                    Button(
                        onClick = { }) {
                        Text(text = "Facebook")
                    }
                }
            }
            Row {
                Text(text = "Don't have account?")
                ClickableText(text = AnnotatedString(" Sign Up"), onClick = {toSignup()}, style = TextStyle(color = Color.Blue))
            }
        }
    }
}