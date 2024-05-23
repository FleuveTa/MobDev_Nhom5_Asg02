package com.example.prj02_healthy_plan.ui.theme

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.prj02_healthy_plan.ContextWrapper
import com.example.prj02_healthy_plan.FirebaseAuthWrapper
import com.example.prj02_healthy_plan.R
import com.example.prj02_healthy_plan.User
import com.example.prj02_healthy_plan.activities.MainActivity
import com.example.prj02_healthy_plan.uiModel.UserViewModel
import com.google.common.io.ByteStreams.readBytes
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreTabUI(auth: FirebaseAuthWrapper, context: ContextWrapper, nav: NavController, user: User) {
    val transparentButtonColors = ButtonDefaults.buttonColors(
        containerColor = Color.Transparent,
        contentColor = Color.Black // Set text color
    )

    val avatarValue = remember(user.avatar) {
        mutableStateOf(user.avatar ?: "")
    }

    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val pickPhotoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            selectedImageUri = uri
        }
    )

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
                        "Settings",
                        style = TextStyle(
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        auth.signOut()
                        if (context is Activity) {
                            val intent = Intent(context, MainActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                            context.startActivity(intent)
                            context.finish()
                        }
                    }) {
                        Icon(Icons.Default.Logout, contentDescription = "Logout Icon")
                    }
                })
        }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(245, 250, 255))
        ) {
            Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                AsyncImage(
                    model = avatarValue.value,
                    placeholder = painterResource(R.drawable.baseline_account_circle_24),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                )

                Box(modifier = Modifier.align(Alignment.BottomEnd)) {
                    Box(
                        modifier = Modifier
                            .size(size = 30.dp)
                            .clip(shape = CircleShape)
                            .background(color = Color(0xfff5f5f5))
                            .border(
                                border = BorderStroke(5.dp, Color.White),
                                shape = CircleShape
                            )
                    ) {
                        IconButton(onClick = {
                            val cont = context.getContext()
                            pickPhotoLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                            selectedImageUri?.let {
                                uploadToStorage(it, cont, auth.getUID())
                            }
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.edit),
                                contentDescription = "edit-avatar",
                                modifier = Modifier
                                    .align(alignment = Alignment.Center)
                            )
                        }
                    }
                }
            }

            Text(
                text = user.fullName ?: "",
                color = Color.Black,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                lineHeight = 1.27.em,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .fillMaxWidth()
            )

            var email: String = ""
            email = auth.getEmail()

            Text(
                text = email,
                color = Color.Black,
                textAlign = TextAlign.Center,
                lineHeight = 1.43.em,
                style = TextStyle(
                    fontSize = 16.sp,
                    letterSpacing = 0.25.sp
                ),
                modifier = Modifier
                    .fillMaxWidth())

            Spacer(modifier = Modifier.height(30.dp))
            Box(Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeight(height = 120.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .requiredHeight(height = 120.dp)
                            .clip(shape = RoundedCornerShape(8.dp))
                            .background(color = Color.LightGray)
                    )
                    Button(colors = transparentButtonColors,
                        onClick = {
                            nav.navigate("userInfor")
                        }, modifier = Modifier.testTag("changeInforButton")) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(alignment = Alignment.Top)
                                .requiredHeight(height = 24.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.edit_profile_icon),
                                contentDescription = "line/business/profile-line",
                                modifier = Modifier.align(Alignment.CenterStart)
                            )
                            Text(
                                text = "Edit profile information",
                                color = Color.Black,
                                lineHeight = 1.43.em,
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    letterSpacing = 0.25.sp
                                ),
                                modifier = Modifier
                                    .align(alignment = Alignment.CenterStart)
                                    .offset(
                                        x = 35.dp,
                                        y = 2.dp
                                    )
                                    .fillMaxHeight()
                            )
                        }
                    }

                    Button(
                        colors = transparentButtonColors,
                        onClick = { /*TODO*/ },
                        modifier = Modifier.offset(
                            x = 0.dp, y = 36.dp
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(alignment = Alignment.Top)
                                .requiredHeight(height = 24.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.sand_icon),
                                contentDescription = "line/business/profile-line",
                                modifier = Modifier.align(Alignment.CenterStart)
                            )
                            Text(
                                text = "Intermitten Fasting",
                                color = Color.Black,
                                lineHeight = 1.43.em,
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    letterSpacing = 0.25.sp
                                ),
                                modifier = Modifier
                                    .align(alignment = Alignment.CenterStart)
                                    .offset(
                                        x = 35.dp,
                                        y = 2.dp
                                    )
                                    .fillMaxHeight()
                            )

                            Text(
                                text = "ON",
                                color = Color(0xff1573fe),
                                textAlign = TextAlign.End,
                                lineHeight = 1.43.em,
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    letterSpacing = 0.25.sp
                                ),
                                modifier = Modifier
                                    .fillMaxSize()
                            )
                        }
                    }

                    Button(
                        colors = transparentButtonColors,
                        onClick = { /*TODO*/ },
                        modifier = Modifier.offset(
                            x = 0.dp, y = 72.dp
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(alignment = Alignment.Top)
                                .requiredHeight(height = 24.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.language_icon),
                                contentDescription = "line/business/profile-line",
                                modifier = Modifier.align(Alignment.CenterStart)
                            )
                            Text(
                                text = "Language",
                                color = Color.Black,
                                lineHeight = 1.43.em,
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    letterSpacing = 0.25.sp
                                ),
                                modifier = Modifier
                                    .align(alignment = Alignment.CenterStart)
                                    .offset(
                                        x = 35.dp,
                                        y = 2.dp
                                    )
                                    .fillMaxHeight()
                            )

                            Text(
                                text = "English",
                                color = Color(0xff1573fe),
                                textAlign = TextAlign.End,
                                lineHeight = 1.43.em,
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    letterSpacing = 0.25.sp
                                ),
                                modifier = Modifier
                                    .fillMaxSize()
                            )
                        }

                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
            Box(Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeight(height = 86.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .requiredHeight(height = 86.dp)
                            .clip(shape = RoundedCornerShape(8.dp))
                            .background(color = Color.LightGray)
                    )

                    Button(colors = transparentButtonColors,
                        onClick = { nav.navigate("security") })
                    {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(alignment = Alignment.Top)
                                .requiredHeight(height = 24.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.security_icon),
                                contentDescription = "line/business/profile-line",
                                modifier = Modifier.align(Alignment.CenterStart)
                            )
                            Text(
                                text = "Security",
                                color = Color.Black,
                                lineHeight = 1.43.em,
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    letterSpacing = 0.25.sp
                                ),
                                modifier = Modifier
                                    .align(alignment = Alignment.CenterStart)
                                    .offset(
                                        x = 35.dp,
                                        y = 2.dp
                                    )
                                    .fillMaxHeight()
                            )
                        }
                    }


                    Button(
                        colors = transparentButtonColors,
                        onClick = { /*TODO*/ },
                        modifier = Modifier.offset(x = 0.dp, y = 38.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(alignment = Alignment.Top)
                                .requiredHeight(height = 24.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.theme_icon),
                                contentDescription = "line/business/profile-line",
                                modifier = Modifier.align(Alignment.CenterStart)
                            )
                            Text(
                                text = "Theme",
                                color = Color.Black,
                                lineHeight = 1.43.em,
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    letterSpacing = 0.25.sp
                                ),
                                modifier = Modifier
                                    .align(alignment = Alignment.CenterStart)
                                    .offset(
                                        x = 35.dp,
                                        y = 2.dp
                                    )
                                    .fillMaxHeight()
                            )

                            Text(
                                text = "Light Mode",
                                color = Color(0xff1573fe),
                                textAlign = TextAlign.End,
                                lineHeight = 1.43.em,
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    letterSpacing = 0.25.sp
                                ),
                                modifier = Modifier
                                    .fillMaxSize()
                            )
                        }
                    }


                }
            }

            Spacer(modifier = Modifier.height(30.dp))
            Box(Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeight(height = 120.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .requiredHeight(height = 120.dp)
                            .clip(shape = RoundedCornerShape(8.dp))
                            .background(color = Color.LightGray)
                    )

                    Button(colors = transparentButtonColors,
                        onClick = { /*TODO*/ })
                    {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(alignment = Alignment.Top)
                                .requiredHeight(height = 24.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.help_icon),
                                contentDescription = "line/business/profile-line",
                                modifier = Modifier.align(Alignment.CenterStart)
                            )
                            Text(
                                text = "Help & Support",
                                color = Color.Black,
                                lineHeight = 1.43.em,
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    letterSpacing = 0.25.sp
                                ),
                                modifier = Modifier
                                    .align(alignment = Alignment.CenterStart)
                                    .offset(
                                        x = 35.dp,
                                        y = 2.dp
                                    )
                                    .fillMaxHeight()
                            )
                        }
                    }


                    Button(
                        colors = transparentButtonColors,
                        onClick = { /*TODO*/ },
                        modifier = Modifier.offset(
                            x = 0.dp, y = 36.dp
                        )
                    )
                    {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(alignment = Alignment.Top)
                                .requiredHeight(height = 24.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.contact_icon),
                                contentDescription = "line/business/profile-line",
                                modifier = Modifier.align(Alignment.CenterStart)
                            )
                            Text(
                                text = "Contact us",
                                color = Color.Black,
                                lineHeight = 1.43.em,
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    letterSpacing = 0.25.sp
                                ),
                                modifier = Modifier
                                    .align(alignment = Alignment.CenterStart)
                                    .offset(
                                        x = 35.dp,
                                        y = 2.dp
                                    )
                                    .fillMaxHeight()
                            )
                        }
                    }

                    Button(
                        colors = transparentButtonColors,
                        onClick = { /*TODO*/ },
                        modifier = Modifier.offset(
                            x = 0.dp, y = 72.dp
                        )
                    )
                    {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(alignment = Alignment.Top)
                                .requiredHeight(height = 24.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.policy_icon),
                                contentDescription = "line/business/profile-line",
                                modifier = Modifier.align(Alignment.CenterStart)
                            )
                            Text(
                                text = "Policy",
                                color = Color.Black,
                                lineHeight = 1.43.em,
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    letterSpacing = 0.25.sp
                                ),
                                modifier = Modifier
                                    .align(alignment = Alignment.CenterStart)
                                    .offset(
                                        x = 35.dp,
                                        y = 2.dp
                                    )
                                    .fillMaxHeight()
                            )

                            Text(
                                text = "Privacy policy",
                                color = Color(0xff1573fe),
                                textAlign = TextAlign.End,
                                lineHeight = 1.43.em,
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    letterSpacing = 0.25.sp
                                ),
                                modifier = Modifier
                                    .fillMaxSize()
                            )
                        }
                    }

                }

            }
        }
    }
}

fun uploadToStorage(uri: Uri, context: Context, uId: String) {
    val storage = Firebase.storage
    val storageRef = storage.reference
    val db = Firebase.firestore

    val imageRef = storageRef.child("images/${uri.lastPathSegment}")

    val uploadTask = imageRef.putFile(uri)
    uploadTask.addOnFailureListener {
        Toast.makeText(context, "Upload failed", Toast.LENGTH_SHORT).show()
    }.addOnSuccessListener { taskSnapshot ->
        Toast.makeText(context, "Upload success", Toast.LENGTH_SHORT).show()
        imageRef.downloadUrl.addOnSuccessListener { uri ->
            db.collection("users").document(uId).update("avatar", uri.toString())
        }
    }
}


