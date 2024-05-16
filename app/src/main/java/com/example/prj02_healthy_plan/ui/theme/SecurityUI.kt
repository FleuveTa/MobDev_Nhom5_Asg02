package com.example.prj02_healthy_plan.ui.theme

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Space
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.prj02_healthy_plan.activities.MainActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecurityUI(nav: NavHostController) {
    val user = Firebase.auth.currentUser
    val currentPassword = remember {
        mutableStateOf("")
    }
    val newPassword = remember {
        mutableStateOf("")
    }
    val confirmNewPassword = remember {
        mutableStateOf("")
    }
    val context = LocalContext.current
    var currentPasswordVisibility by remember { mutableStateOf(false) }
    var newPasswordVisibility by remember { mutableStateOf(false) }
    var confirmNewPasswordVisibility by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(
                    245,
                    250,
                    255
                )
            ),
                title = {
                    Text(
                        "Security",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                })
        }
    ) {innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(height = 60.dp)
                    .padding(5.dp)
            ) {
                OutlinedTextField(
                    value = currentPassword.value,
                    onValueChange = { currentPassword.value = it },
                    label = { Text("Current Password") },
                    visualTransformation = if (currentPasswordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        IconButton(onClick = { currentPasswordVisibility = !currentPasswordVisibility }) {
                            Icon(
                                imageVector = if (currentPasswordVisibility) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                contentDescription = "Toggle password visibility"
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeight(60.dp)
                        .align(Alignment.Center)
                )
            }

            Spacer(modifier = Modifier.requiredHeight(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(height = 60.dp)
                    .padding(5.dp)
            ) {
                OutlinedTextField(
                    value = newPassword.value,
                    onValueChange = { newPassword.value = it },
                    label = { Text("New Password") },
                    visualTransformation = if (newPasswordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        IconButton(onClick = { newPasswordVisibility = !newPasswordVisibility }) {
                            Icon(
                                imageVector = if (newPasswordVisibility) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                contentDescription = "Toggle password visibility"
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeight(60.dp)
                        .align(Alignment.Center)
                )
            }

            Spacer(modifier = Modifier.requiredHeight(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(height = 60.dp)
                    .padding(5.dp)
            ) {
                OutlinedTextField(
                    value = confirmNewPassword.value,
                    onValueChange = { confirmNewPassword.value = it },
                    label = { Text("New Password Confirm") },
                    visualTransformation = if (confirmNewPasswordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        IconButton(onClick = { confirmNewPasswordVisibility = !confirmNewPasswordVisibility }) {
                            Icon(
                                imageVector = if (confirmNewPasswordVisibility) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                contentDescription = "Toggle password visibility"
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeight(60.dp)
                        .align(Alignment.Center)
                )
            }

            Spacer(modifier = Modifier.requiredHeight(16.dp))

            Button(onClick = {
                if (newPassword.value.length < 6) {
                    Toast.makeText(context, "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show()
                }  else if (newPassword.value == confirmNewPassword.value) {
                    val credential = EmailAuthProvider
                        .getCredential(user?.email.toString(), currentPassword.value)
                    user?.reauthenticate(credential)?.addOnCompleteListener {
                        if (it.isSuccessful) {
                            Log.d("Try re-authenticated", "User re-authenticated.")
                            user.updatePassword(newPassword.value).addOnCompleteListener { task ->
                                Log.d("Try update password", "User password in.")
                                if (task.isSuccessful) {
                                    nav.popBackStack()
                                    Toast.makeText(
                                        context,
                                        "Password changed successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Password change failed",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        } else {
                            Toast.makeText(context, "Re-authentication failed, check your current password", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                } else {
                    Toast.makeText(context, "Passwords not match", Toast.LENGTH_SHORT)
                        .show()
                }
            }) {
                Text("Change Password")
            }
        }
    }
}