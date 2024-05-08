package com.example.prj02_healthy_plan.ui.theme

import android.app.Activity
import android.content.Context
import android.content.Intent
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.prj02_healthy_plan.R
import com.example.prj02_healthy_plan.activities.MainActivity
import com.google.firebase.auth.FirebaseAuth

@Composable
fun MoreTabUI(auth: FirebaseAuth, context: Context, nav: NavController) {
    val transparentButtonColors = ButtonDefaults.buttonColors(
        containerColor = Color.Transparent,
        contentColor = Color.Black // Set text color
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(245, 250, 255))) {
        Header(nav)

        Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Image(
                painter = painterResource(id = R.drawable.avartar),
                contentDescription = "user-avatar",
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
                        )) {
                    Icon(
                        painter = painterResource(id = R.drawable.edit),
                        contentDescription = "edit-avatar",
                        modifier = Modifier
                            .align(alignment = Alignment.Center)
                    )
                }
            }
        }

        Text(
            text = "Random Girl Name",
            color = Color.Black,
            textAlign = TextAlign.Center,
            lineHeight = 1.27.em,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .fillMaxWidth())

        auth.currentUser?.let {
            it.email?.let { it1 ->
                Text(
                    text = it1,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    lineHeight = 1.43.em,
                    style = TextStyle(
                        fontSize = 14.sp,
                        letterSpacing = 0.25.sp),
                    modifier = Modifier
                        .fillMaxWidth())
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
                        .background(color = Color.LightGray))
                Button(colors = transparentButtonColors,
                    onClick = {
                        nav.navigate("userInfor")
                     }) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(alignment = Alignment.Top)
//                            .offset(
//                                x = 16.dp,
//                                y = 12.dp
//                            )
                            .requiredHeight(height = 24.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.edit_profile_icon),
                            contentDescription = "line/business/profile-line",
                            modifier = Modifier.align(Alignment.CenterStart))
                        Text(
                            text = "Edit profile information",
                            color = Color.Black,
                            lineHeight = 1.43.em,
                            style = TextStyle(
                                fontSize = 14.sp,
                                letterSpacing = 0.25.sp),
                            modifier = Modifier
                                .align(alignment = Alignment.CenterStart)
                                .offset(
                                    x = 35.dp,
                                    y = 2.dp
                                )
                                .fillMaxHeight())
                    }
                }

                Button(colors = transparentButtonColors,
                    onClick = { /*TODO*/ },
                    modifier = Modifier.offset(
                        x = 0.dp, y = 36.dp
                    )) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(alignment = Alignment.Top)
//                            .offset(
//                                x = 16.dp,
//                                y = 48.dp
//                            )
                            .requiredHeight(height = 24.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.sand_icon),
                            contentDescription = "line/business/profile-line",
                            modifier = Modifier.align(Alignment.CenterStart))
                        Text(
                            text = "Intermitten Fasting",
                            color = Color.Black,
                            lineHeight = 1.43.em,
                            style = TextStyle(
                                fontSize = 14.sp,
                                letterSpacing = 0.25.sp),
                            modifier = Modifier
                                .align(alignment = Alignment.CenterStart)
                                .offset(
                                    x = 35.dp,
                                    y = 2.dp
                                )
                                .fillMaxHeight())

                        Text(
                            text = "ON",
                            color = Color(0xff1573fe),
                            textAlign = TextAlign.End,
                            lineHeight = 1.43.em,
                            style = TextStyle(
                                fontSize = 14.sp,
                                letterSpacing = 0.25.sp),
                            modifier = Modifier
                                .fillMaxSize()
//                                .offset(
//                                    x = -35.dp,
//                                    y = 2.dp
//                                )
                        )
                    }
                }

                Button(colors = transparentButtonColors,
                    onClick = { /*TODO*/ },
                    modifier = Modifier.offset(
                        x = 0.dp, y = 72.dp
                    )) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(alignment = Alignment.Top)
//                            .offset(
//                                x = 16.dp,
//                                y = 84.dp
//                            )
                            .requiredHeight(height = 24.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.language_icon),
                            contentDescription = "line/business/profile-line",
                            modifier = Modifier.align(Alignment.CenterStart))
                        Text(
                            text = "Language",
                            color = Color.Black,
                            lineHeight = 1.43.em,
                            style = TextStyle(
                                fontSize = 14.sp,
                                letterSpacing = 0.25.sp),
                            modifier = Modifier
                                .align(alignment = Alignment.CenterStart)
                                .offset(
                                    x = 35.dp,
                                    y = 2.dp
                                )
                                .fillMaxHeight())

                        Text(
                            text = "English",
                            color = Color(0xff1573fe),
                            textAlign = TextAlign.End,
                            lineHeight = 1.43.em,
                            style = TextStyle(
                                fontSize = 14.sp,
                                letterSpacing = 0.25.sp),
                            modifier = Modifier
                                .fillMaxSize()
//                                .offset(
//                                    x = -35.dp,
//                                    y = 2.dp)
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
                    onClick = { /*TODO*/ })
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


                Button(colors = transparentButtonColors,
                    onClick = { /*TODO*/ },
                    modifier = Modifier.offset(x = 0.dp, y = 38.dp)) {
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
                        .background(color = Color.LightGray))

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
                            modifier = Modifier.align(Alignment.CenterStart))
                        Text(
                            text = "Help & Support",
                            color = Color.Black,
                            lineHeight = 1.43.em,
                            style = TextStyle(
                                fontSize = 14.sp,
                                letterSpacing = 0.25.sp),
                            modifier = Modifier
                                .align(alignment = Alignment.CenterStart)
                                .offset(
                                    x = 35.dp,
                                    y = 2.dp
                                )
                                .fillMaxHeight())
                    }
                }


                Button(colors = transparentButtonColors,
                    onClick = { /*TODO*/ },
                    modifier = Modifier.offset(
                        x = 0.dp, y = 36.dp))
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
                            modifier = Modifier.align(Alignment.CenterStart))
                        Text(
                            text = "Contact us",
                            color = Color.Black,
                            lineHeight = 1.43.em,
                            style = TextStyle(
                                fontSize = 14.sp,
                                letterSpacing = 0.25.sp),
                            modifier = Modifier
                                .align(alignment = Alignment.CenterStart)
                                .offset(
                                    x = 35.dp,
                                    y = 2.dp
                                )
                                .fillMaxHeight())
                    }
                }

                Button(colors = transparentButtonColors,
                    onClick = { /*TODO*/ },
                    modifier = Modifier.offset(
                        x = 0.dp, y = 72.dp))
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
                            modifier = Modifier.align(Alignment.CenterStart))
                        Text(
                            text = "Language",
                            color = Color.Black,
                            lineHeight = 1.43.em,
                            style = TextStyle(
                                fontSize = 14.sp,
                                letterSpacing = 0.25.sp),
                            modifier = Modifier
                                .align(alignment = Alignment.CenterStart)
                                .offset(
                                    x = 35.dp,
                                    y = 2.dp
                                )
                                .fillMaxHeight())

                        Text(
                            text = "Privacy policy",
                            color = Color(0xff1573fe),
                            textAlign = TextAlign.End,
                            lineHeight = 1.43.em,
                            style = TextStyle(
                                fontSize = 14.sp,
                                letterSpacing = 0.25.sp),
                            modifier = Modifier
                                .fillMaxSize())
                    }
                }

            }

        }
        Button(
            onClick = { auth.signOut()
                if (context is Activity) {
                    val intent = Intent(context, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    context.startActivity(intent)
                    context.finish() }},

            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .align(Alignment.End),
            colors = ButtonDefaults.buttonColors(Color.Red)
        ) {
            Text(text = "Logout",
                color = Color.White,
                textAlign = TextAlign.Center,
                lineHeight = 1.43.em,
                style = TextStyle(
                    fontSize = 14.sp,
                    letterSpacing = 0.25.sp))
        }
    }
}


