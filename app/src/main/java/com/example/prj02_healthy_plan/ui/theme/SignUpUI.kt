package com.example.prj02_healthy_plan.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SignUpUI(username: MutableState<String>, password: MutableState<String>, signup: () -> Unit, toSignin: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Create your account",
                modifier = Modifier.padding(top = 30.dp),
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
            )
            Column(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth(0.9f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = "To day is a new day. It's your day. You shape it.")
                Text(text = "Sign up to start becoming a better self.")
            }
            Column(
                modifier = Modifier
                    .padding(top = 32.dp)
                    .fillMaxWidth(0.9f),
            ) {
                Text(text = "Email")
                UsernameField(username)
            }
            Column(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(0.9f),
            ) {
                Text(text = "Password")
                PasswordField(password)
                Button(
                    onClick = { signup() },
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(top = 40.dp)
                        .testTag("signup_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2BF361),
                    )
                ) {
                    Text(text = "Sign Up")
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
                    Text(text = "--------Or sign up with--------")
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
                        onClick = { /*TODO*/ }) {
                        Text(text = "Facebook")
                    }
                }
            }
            Row {
                Text(text = "Already have an account?")
                ClickableText(text = AnnotatedString(" Sign In"), onClick = {toSignin()}, style = TextStyle(color = Color.Blue),
                    modifier = Modifier.testTag("signin_button"))
            }
        }
    }
}