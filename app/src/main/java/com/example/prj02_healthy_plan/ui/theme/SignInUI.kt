package com.example.prj02_healthy_plan.ui.theme

import android.content.Intent
import android.credentials.GetCredentialException
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.NoCredentialException
import com.example.prj02_healthy_plan.R
import com.example.prj02_healthy_plan.activities.MainActivity
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.util.UUID

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun SignInUI(username: MutableState<String>, password: MutableState<String>, login: () -> Unit, toSignup: () -> Unit, auth: FirebaseAuth) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
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
                    Text(text = "Or sign in with")
                }
                val signInGoogle : () -> Unit = {
                    val credentialManager = CredentialManager.create(context)

                    val rawNonce = UUID.randomUUID().toString()
                    val bytes = rawNonce.toByteArray()
                    val md = MessageDigest.getInstance("SHA-256")
                    val digest = md.digest(bytes)
                    val hashedNonce = digest.fold("") { str, it -> str + "%02x".format(it) }

                    val googleIdOptions: GetGoogleIdOption = GetGoogleIdOption.Builder()
                        .setFilterByAuthorizedAccounts(false)
                        .setServerClientId("476449533184-cu56he7609keukdf9ncbnad0dot2291i.apps.googleusercontent.com")
                        .setNonce(hashedNonce)
                        .build()

                    val request : GetCredentialRequest = GetCredentialRequest.Builder()
                        .addCredentialOption(googleIdOptions)
                        .build()

                    coroutineScope.launch {
                        try {
                            val result = credentialManager.getCredential(
                                request = request,
                                context = context
                            )
                            val credential = result.credential

                            val googleIdTokenCredential = GoogleIdTokenCredential
                                .createFrom(credential.data)

                            val googleIdToken = googleIdTokenCredential.idToken

                            val credentialUser =
                                GoogleAuthProvider.getCredential(googleIdToken, null)
                            auth.signInWithCredential(credentialUser)
                                .addOnSuccessListener { authResult ->
                                    val user = authResult.user
                                    val intent = Intent(context, MainActivity::class.java)
                                    startActivity(context, intent, null)
                                    Toast.makeText(
                                        context,
                                        "Signed In : ${user?.email}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(context, "Sign In Failed", Toast.LENGTH_SHORT)
                                        .show()
                                }
                        } catch (e: GetCredentialException) {
                            Toast.makeText(context, "Sign In Failed", Toast.LENGTH_SHORT).show()
                        } catch (e: GoogleIdTokenParsingException) {
                            Toast.makeText(context, "Sign In Failed", Toast.LENGTH_SHORT).show()
                        } catch (e: GetCredentialCancellationException) {
                            Toast.makeText(context, "You canceled the SignIn", Toast.LENGTH_SHORT)
                                .show()
                        } catch (e: NoCredentialException) {
                            Toast.makeText(context, "Device have no Google account", Toast.LENGTH_SHORT).show()
                        }
                    }

                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = { signInGoogle() },
                        modifier = Modifier
                            .padding(10.dp),
                        colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2BF361)
                    )
                    ) {
                        Row (
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.google),
                                contentDescription = "user-avatar",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(30.dp)
                                    .clip(CircleShape)
                            )
                            Text(text = "Google", modifier = Modifier.padding(start = 10.dp))
                        }
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