package com.example.prj02_healthy_plan.ui.theme

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.provider.OpenableColumns
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraControl
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.CameraController
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType.Companion.Uri
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.google.firebase.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import okio.IOException
import java.io.File
import java.io.FileOutputStream


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanScreen(nav: NavHostController, urlMutable: MutableState<String>) {
    val CAMERAX_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    val CAMERA_PERMISSION_REQUEST_CODE = 100
    var isCameraPermissionGranted by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val lifecycleOwner = (context as Activity) as LifecycleOwner
    val imageCapture = remember { mutableStateOf<ImageCapture?>(null) }
    var cameraControl by remember { mutableStateOf<CameraControl?>(null) }
    var flashMode by remember { mutableStateOf(false) }
    var flashIcon by remember { mutableStateOf(Icons.Default.FlashOff) }


    fun toggleFlash() {
        flashMode = !flashMode
        flashIcon = if (!flashMode) {
            Icons.Default.FlashOff
        } else {
            Icons.Default.FlashOn
        }
        cameraControl?.enableTorch(flashMode)
    }

    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {
        imageUri = it
    }

    // Kiểm tra và cập nhật trạng thái quyền camera khi Composable khởi chạy
    LaunchedEffect(key1 = true) {
        isCameraPermissionGranted = hasCameraPermission(context)
    }

    LaunchedEffect(imageUri) {
        val client = OkHttpClient()
        val contentResolver = context.contentResolver
        val tempFile = createTempFileFromUri(contentResolver, imageUri, context)
        tempFile?.let {
            val reqBody = it.asRequestBody("image/*".toMediaTypeOrNull())
            val body = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", it.name, reqBody)
                .build()

            val request = Request.Builder()
                .url("https://vision.foodvisor.io/api/1.0/en/analysis")
                .addHeader("Authorization", "Api-Key wPyhDwYu.8KyNaAAhCo9QbYrMFOx0EWubReZoWsYg")
                .post(body)
                .build()
            kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO).launch {
                val response = client.newCall(request).execute()
                if (!response.isSuccessful) throw IOException("Unexpected code $response")
                Log.d("ScanScreen", "Response: ${response.body?.string()}")
            }
        }
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
                    IconButton(onClick = {
                        nav.navigate("explore")
                        flashMode = false
                        cameraControl?.enableTorch(false)}) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { toggleFlash()}) {
                        Icon(flashIcon, modifier = Modifier.size(30.dp),contentDescription = "Flash", tint = Color.White)
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
                                    .build()
                                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                                // Setup image capture
                                imageCapture.value = ImageCapture.Builder()
                                    .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                                    .build()
                                try {
                                        cameraProvider.unbindAll()
                                    val cam = cameraProvider.bindToLifecycle(
                                        lifecycleOwner,
                                        cameraSelector,
                                        preview,
                                        imageCapture.value
                                    )
                                    cameraControl = cam.cameraControl
                                    cameraControl?.enableTorch(flashMode)
                                    preview.setSurfaceProvider(previewView.surfaceProvider)
                                } catch (exc: Exception) {
                                    // Handle the error appropriately
                                }
                                preview.setSurfaceProvider(previewView.surfaceProvider)
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
                IconButton(onClick = {
                    if (isCameraPermissionGranted) {
                    takePhoto(context, imageCapture.value, nav, { photoFile ->
                        Log.d("ScanScreen", "Photo taken: ${photoFile.absolutePath}")
                    }, { exception ->
                        Log.e("ScanScreen", "Error taking photo", exception)
                    }, urlMutable)
                } else {
                    ActivityCompat.requestPermissions(
                        context as Activity,
                        CAMERAX_PERMISSIONS,
                        CAMERA_PERMISSION_REQUEST_CODE
                    )
                }}) {
                    Icon(Icons.Default.PhotoCamera,
                        modifier = Modifier
                            .size(100.dp),
                        contentDescription = "Scan",
                        tint = Color.White)

                }
                IconButton(onClick = {
                    launcher.launch("image/*")
                    val client = OkHttpClient()
//                    imageUri?.path?.let {
//                        val imageFile = File(it)
//                        val reqBody = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
//                        val body = MultipartBody.Part.createFormData("image", imageFile.name, reqBody)
//
//                        val request = Request.Builder()
//                            .url("https://vision.foodvisor.io/api/1.0/en/analysis")
//                            .addHeader("Authorization", "Api-Key wPyhDwYu.8KyNaAAhCo9QbYrMFOx0EWubReZoWsYg")
//                            .post(reqBody)
//                            .build()
//
//                        client.newCall(request).execute().use { response ->
//                            if (!response.isSuccessful) throw IOException("Unexpected code $response")
//                            Log.d("ScanScreen", "Response: ${response.body?.string()}")
//                        }
//                    }
                }) {
                    Icon(Icons.Default.Image,
                        modifier = Modifier
                            .size(100.dp),
                        contentDescription = "Gallery",
                        tint = Color.White)
                }
                AsyncImage(
                    model = imageUri,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(4.dp)
                        .height(200.dp)
                        .width(200.dp),
                    contentScale = ContentScale.Crop
                )
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

fun takePhoto(
    context: Context,
    imageCapture: ImageCapture? = null,
    nav: NavHostController,
    onImageCaptured: (File) -> Unit,
    onError: (ImageCaptureException) -> Unit,
    result: MutableState<String>
) {
    val photoFile = File(
        context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
        "${System.currentTimeMillis()}.jpg"
    )
    val PHOTO_URI_KEY = "IngredientPhoto_uri"
    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
    result.value = photoFile.toUri().toString()
    nav.navigate("explore")

    imageCapture?.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                uploadPhotoToFirebase(photoFile, { downloadUrl ->
//                    onImageCaptured(photoFile)

                }, { exception ->
                    Log.e("ScanScreen", "Error uploading image", exception)
                })
            }

            override fun onError(exception: ImageCaptureException) {
                onError(exception)
            }
        }
    )
}

fun uploadPhotoToFirebase(photoFile: File, onSuccess: (String) -> Unit, onError: (Exception) -> Unit) {
    val storage = Firebase.storage
    val storageRef = storage.reference
    val photosRef = storageRef.child("photos/${photoFile.name}")

    val uploadTask = photosRef.putFile(photoFile.toUri())

    uploadTask.addOnSuccessListener {
        // Get the download URL
        photosRef.downloadUrl.addOnSuccessListener { uri ->
           Log.d("ScanScreen", "Image uploaded: $uri")
        }.addOnFailureListener { exception ->
            onError(exception)
        }
    }.addOnFailureListener { exception ->
        onError(exception)
    }
}

fun createTempFileFromUri(contentResolver: ContentResolver, uri: Uri?, context: Context): File? {
    if (uri == null) return null

    val fileName = getFileName(contentResolver, uri)
    val tempFile = File.createTempFile(fileName, null, context.cacheDir)
    tempFile.deleteOnExit()

    return try {
        val inputStream = contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(tempFile)
        inputStream?.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }
        tempFile
    } catch (e: IOException) {
        Log.e("ScanScreen", "Error creating temp file", e)
        null
    }
}

fun getFileName(contentResolver: ContentResolver, uri: Uri): String {
    var name = "temp_file"
    val cursor = contentResolver.query(uri, null, null, null, null)
    cursor?.use {
        if (it.moveToFirst()) {
            val index = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (index != -1) {
                name = it.getString(index)
            }
        }
    }
    return name
}



