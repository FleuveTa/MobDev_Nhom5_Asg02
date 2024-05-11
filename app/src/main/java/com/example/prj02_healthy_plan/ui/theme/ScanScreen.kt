package com.example.prj02_healthy_plan.ui.theme

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanScreen(nav: NavHostController) {
    val CAMERAX_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    val CAMERA_PERMISSION_REQUEST_CODE = 100
    var isCameraPermissionGranted by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Kiểm tra và cập nhật trạng thái quyền camera khi Composable khởi chạy
    LaunchedEffect(key1 = true) {
        isCameraPermissionGranted = hasCameraPermission(context)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black
                ),
                title = {
                    Text(
                        "Food Scanner",
                        style = TextStyle(
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Default.FlashOff, modifier = Modifier.size(30.dp),contentDescription = "Flash", tint = Color.White)
                    }
                },
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.Black)
        ) {
            // Camera
            if (isCameraPermissionGranted) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 150.dp, start = 15.dp, end = 15.dp)
                        .height(300.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.Gray)
                ) {
                    AndroidView(
                        factory = { context ->
                            val previewView = PreviewView(context)
                            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
                            cameraProviderFuture.addListener({
                                val cameraProvider = cameraProviderFuture.get()
                                val preview = androidx.camera.core.Preview.Builder()
                                    .setTargetAspectRatio(AspectRatio.RATIO_16_9)
                                    .build()
                                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                                preview.setSurfaceProvider(previewView.surfaceProvider)
                                cameraProvider.bindToLifecycle(context as LifecycleOwner, cameraSelector, preview)
                            }, ContextCompat.getMainExecutor(context))
                            previewView
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            } else {
                Button(
                    onClick = {
                        ActivityCompat.requestPermissions(
                            context as Activity,
                            CAMERAX_PERMISSIONS,
                            CAMERA_PERMISSION_REQUEST_CODE
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 150.dp, start = 15.dp, end = 15.dp)
                        .height(300.dp)
                        .clip(RoundedCornerShape(20.dp)),
                    colors = ButtonDefaults.buttonColors(Color.Gray)
                ) {
                    Text("Request Camera Permission", color = Color.White)
                }
            }
            // Scan button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, start = 15.dp, end = 15.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.Default.PhotoCamera,
                        modifier = Modifier
                            .size(100.dp),
                        contentDescription = "Scan",
                        tint = Color.White)

                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.Default.Image,
                        modifier = Modifier
                            .size(100.dp),
                        contentDescription = "Gallery",
                        tint = Color.White)

                }
            }
        }
    }
}

fun hasCameraPermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED
}

@Preview
@Composable
fun PreviewScanScreen() {
    ScanScreen(nav = rememberNavController())
}