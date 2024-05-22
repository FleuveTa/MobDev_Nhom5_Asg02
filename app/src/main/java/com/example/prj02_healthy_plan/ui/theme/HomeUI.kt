package com.example.prj02_healthy_plan.ui.theme

import PastOrPresentSelectableDates
import android.content.pm.PackageManager
import android.graphics.Color.parseColor
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.prj02_healthy_plan.DailyData
import com.example.prj02_healthy_plan.R
import com.example.prj02_healthy_plan.User
import com.example.prj02_healthy_plan.uiModel.DailyDataViewModel
import com.example.prj02_healthy_plan.uiModel.UserViewModel
import convertMillisToDate
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

val textProgressColor = Color(parseColor("#3B3B3B"))

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun HomeUI(nav: NavController, date: MutableState<String>) {
    val dailyViewModel: DailyDataViewModel = viewModel()

    val userViewModel: UserViewModel = viewModel()
    val user = userViewModel.state.value
    val dailyData by dailyViewModel.dailyData.collectAsState()

    LaunchedEffect(date.value) {
        dailyViewModel.fetchDailyData(date.value)
        Log.d("FETCHING : ", "DATE: ${date.value}")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(245, 250, 255))
    ) {
        Header(nav, date)
        Content(user, dailyViewModel, dailyData)
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Header(nav: NavController, dateFormatted: MutableState<String>) {
    val context = LocalContext.current
    val datePickerState = rememberDatePickerState(
        selectableDates = PastOrPresentSelectableDates
    )
    val selectedDateLabel = remember { mutableStateOf("Today") }
    val openDialog = remember { mutableStateOf(false) }
    val calendarPickerMainColor = Color(0xFF722276)
    val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }
    val openNotificationDialog = remember { mutableStateOf(false) }

    val hasNotificationPermission = remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            Log.d("PERMISSION", "GRANTED")
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
            Log.d("PERMISSION", "DENIED")
        }
    }

    LaunchedEffect(dateFormatted.value) {
        val parsedDate = dateFormat.parse(dateFormatted.value)
        parsedDate?.let {
            datePickerState.selectedDateMillis = it.time
            selectedDateLabel.value = it.time.convertMillisToDate()
            Log.d("Converted", it.time.convertMillisToDate())
        }
    }

    LaunchedEffect(dateFormatted.value) {
        val parsedDate = dateFormat.parse(dateFormatted.value)
        parsedDate?.let {
            datePickerState.selectedDateMillis = it.time
            selectedDateLabel.value = it.time.convertMillisToDate()
            Log.d("Converted", it.time.convertMillisToDate())
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(
            onClick = { openDialog.value = true },
            modifier = Modifier.weight(1.5F, true).testTag("datePickerButton")
        ) {

            Text(
                text = selectedDateLabel.value,
                fontSize = 10.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "DropDown Icon"
            )
        }

        Text(
            text = "HealthyPlans",
            fontSize = 30.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(3F, true),
            color = Color.Green,
            fontWeight = FontWeight.Medium
        )

        IconButton(
            onClick = { openNotificationDialog.value = true },
            Modifier.weight(1F, true)
        ) {
            Icon(
                imageVector = if (hasNotificationPermission.value) Icons.Default.Notifications else Icons.Outlined.Notifications,
                contentDescription = "Notification"
            )
        }
    }

    if (openDialog.value) {
        DatePickerDialog(
            colors = DatePickerDefaults.colors(
                containerColor = Color(0xFFF5F0FF),
            ),
            onDismissRequest = {
                // Action when the dialog is dismissed without selecting a date
                openDialog.value = false
            },
            confirmButton = {
                // Confirm button with custom action and styling
                TextButton(
                    onClick = {
                        // Action to set the selected date and close the dialog
                        openDialog.value = false
                        datePickerState.selectedDateMillis?.let {
                            dateFormatted.value = dateFormat.format(Date(it))
                        }
                        selectedDateLabel.value =
                            datePickerState.selectedDateMillis?.convertMillisToDate() ?: "Today"
                    }
                ) {
                    Text("OK", color = calendarPickerMainColor)
                }
            },
            dismissButton = {
                // Dismiss button to close the dialog without selecting a date
                TextButton(
                    onClick = {
                        openDialog.value = false
                    }
                ) {
                    Text("CANCEL", color = calendarPickerMainColor)
                }
            }
        ) {
            // The actual DatePicker component within the dialog
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    selectedDayContainerColor = calendarPickerMainColor,
                    selectedDayContentColor = Color.White,
                    selectedYearContainerColor = calendarPickerMainColor,
                    selectedYearContentColor = Color.White,
                    todayContentColor = calendarPickerMainColor,
                    todayDateBorderColor = calendarPickerMainColor
                )
            )
        }
    }

    if (openNotificationDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openNotificationDialog.value = false
            },
            confirmButton = {
                TextButton(onClick = {
                    if (hasNotificationPermission.value) {
                        openNotificationDialog.value = false
                    } else {
                        requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                        hasNotificationPermission.value = !hasNotificationPermission.value
                        openNotificationDialog.value = false
                    }
                }) {
                    Text(
                        "Confirm",
                        fontSize = 16.sp
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    openNotificationDialog.value = false
                }) {
                    Text(
                        "Dismiss",
                        fontSize = 16.sp
                    )
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notification Icon"
                )
            },
            title = {
                Text(
                    "Notifications",
                    fontWeight = FontWeight.Medium,
                    fontSize = 28.sp
                )
            },
            text = {
                Box(
                    modifier = Modifier
                        .padding(15.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    if (hasNotificationPermission.value) {
                        Text(
                            "If you want to turn off notifications, please change in the settings!",
                            fontSize = 18.sp
                        )
                    } else {
                        Text(
                            "Do you want to turn on notifications?",
                            fontSize = 16.sp
                        )
                    }
                }
            },
            modifier = Modifier
                .padding(5.dp)
        )
    }
}

@Composable
fun Content(
    user: User,
    dailyDataViewModel: DailyDataViewModel,
    dailyData : DailyData
) {
    val waterColor = Color(parseColor("#63e5ff"))


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(245, 250, 255)),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        HomeTabScreen(dailyData)

        Row(
            modifier = Modifier
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier
                    .width(180.dp)
                    .height(130.dp)
                    .clip(shape = RoundedCornerShape(30.dp))
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFFF0F0FF), Color.White),
                            start = Offset(200f, 200f),
                            end = Offset(0f, 100f)
                        ),
                        shape = RoundedCornerShape(30.dp)
                    ),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { dailyDataViewModel.minusWater() },
                    modifier = Modifier.testTag("minusWaterButton")
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Remove",
                        modifier = Modifier.size(30.dp),
                        tint = Color.Black
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_water_drop_24),
                        contentDescription = "Water",
                        modifier = Modifier.size(60.dp),
                        tint = waterColor
                    )

                    Text(
                        text = dailyData.water.toString() + " liters",
                        color = textProgressColor,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.testTag("waterText")
                    )

                    Text(
                        text = "Daily Water",
                        color = textProgressColor,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                IconButton(
                    onClick = { dailyDataViewModel.addWater()},
                    modifier = Modifier.testTag("addWaterButton")
                ) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_add_circle_24),
                        contentDescription = "Add",
                        modifier = Modifier.size(30.dp),
                        tint = Color.Green
                    )
                }
            }

            Spacer(modifier = Modifier.width(15.dp))

            Column(
                modifier = Modifier
                    .width(180.dp)
                    .height(130.dp)
                    .clip(shape = RoundedCornerShape(30.dp))
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFFF0F0FF), Color.White),
                            start = Offset(200f, 200f),
                            end = Offset(0f, 100f)
                        ),
                        shape = RoundedCornerShape(30.dp)
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "7,550",
                        fontWeight = FontWeight.ExtraBold,
                        color = textProgressColor,
                        fontSize = 16.sp
                    )

                    CircularProgressIndicator(
                        progress = 0.3f,
                        color = Color.Green,
                        strokeWidth = 10.dp,
                        modifier = Modifier.size(70.dp)
                    )
                }

                Text(
                    text = "Steps Walked",
                    color = textProgressColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .clip(shape = RoundedCornerShape(30.dp))
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(parseColor("#FF53B0")), Color(parseColor("#FFFFFF"))),
                        start = Offset(200f, 200f),
                        end = Offset(0f, 100f)
                    ),
                    shape = RoundedCornerShape(30.dp)
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(15.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Exercise",
                        color = textProgressColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    Icon(
                        painter = painterResource(R.drawable.outline_sensor_occupied_24),
                        contentDescription = "Personal",
                        tint = Color.Red,
                        modifier = Modifier.size(40.dp)
                    )
                }

                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row {
                        Icon(
                            painter = painterResource(R.drawable.baseline_access_time_24),
                            contentDescription = "Amount of Time",
                            tint = Color.Blue,
                            modifier = Modifier
                                .size(25.dp)
                                .padding(end = 5.dp)
                        )

                        Text(
                            text = "75 minutes",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Row {
                        Icon(
                            painter = painterResource(R.drawable.baseline_local_fire_department_24),
                            contentDescription = "Amount of Time",
                            tint = Color(parseColor("#FA9B31")),
                            modifier = Modifier
                                .size(25.dp)
                                .padding(end = 5.dp)
                        )

                        Text(
                            text = "350 Cals",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                IconButton(
                    onClick = { /*TODO*/ }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_add_circle_24),
                        contentDescription = "Add",
                        modifier = Modifier.size(40.dp),
                        tint = Color.Green
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .clip(shape = RoundedCornerShape(30.dp))
                .background(
                    Color.White,
                    shape = RoundedCornerShape(30.dp)
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(15.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        progress = 0.8f,
                        color = Color.Green,
                        strokeWidth = 6.dp,
                        modifier = Modifier.size(60.dp)
                    )

                    Icon(
                        painter = painterResource(R.drawable.outline_scale_24),
                        contentDescription = "Scale",
                        tint = Color.Blue,
                        modifier = Modifier.size(35.dp)
                    )
                }

                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = user.weight.toString() + " kg",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "You have a healthy BMI",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Light
                    )
                }

                IconButton(
                    onClick = { /*TODO*/ }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_add_circle_24),
                        contentDescription = "Add",
                        modifier = Modifier.size(40.dp),
                        tint = Color.Green
                    )
                }
            }
        }
    }
}

@Composable
fun HomeTabScreen(dailyData: DailyData) {
    var tabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Macros", "Calories")

    val userViewModel: UserViewModel = viewModel()
    val user = userViewModel.state.value

    Column(modifier = Modifier.fillMaxWidth()) {
        TabRow(selectedTabIndex = tabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(text = { Text(title) },
                    selected = tabIndex == index,
                    onClick = { tabIndex = index }
                )
            }
        }
        when (tabIndex) {
            0 -> MacrosScreen(dailyData.intake ?: listOf(0.0, 0.0, 0.0, 0.0))
            1 -> CaloriesScreen(dailyData.intake?.get(0) ?: 1.0, user.caloriesGoal ?: 1000)
        }
    }
}

@Composable
fun MacrosScreen(macros: List<Double>) {
    val progress1 = Color(parseColor("#FA9B31"))
    val progress2 = Color(parseColor("#2CB9B0"))
    val progress3 = Color(parseColor("#6C0D8F"))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(shape = RoundedCornerShape(30.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFFF0F0FF), Color.White),
                    start = Offset(200f, 200f),
                    end = Offset(0f, 100f)
                ),
                shape = RoundedCornerShape(30.dp)
            ),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp, 0.dp, 20.dp, 0.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            MacrosProgress(progress = 0.6, number = macros[1].toString(), color = progress1)

            MacrosProgress(progress = 0.6, number = macros[2].toString(), color = progress2)

            MacrosProgress(progress = 0.6, number = macros[3].toString(), color = progress3)
        }

        MacrosStats(macros)
    }
}

@Composable
fun MacrosProgress(progress: Double, number: String, color: Color) {


    Box(contentAlignment = Alignment.Center) {
        Text(
            text = number + "g",
            fontWeight = FontWeight.ExtraBold,
            color = textProgressColor,
            fontSize = 22.sp
        )

        CircularProgressIndicator(
            progress = progress.toFloat(),
            color = color,
            strokeWidth = 8.dp,
            modifier = Modifier.size(100.dp)
        )
    }
}

@Composable
fun CaloriesScreen(calories: Double, target: Int) {
    val innerProgressColor = Color(parseColor("#F58BA4"))
    val outerProgressColor = Color(parseColor("#4ED22D"))

    var progress by remember {
        mutableFloatStateOf((calories / target).toFloat())
    }
    progress = (calories / target).toFloat()

    val animatedProgress by animateFloatAsState(targetValue = progress, label = "")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(shape = RoundedCornerShape(30.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFFF0F0FF), Color.White),
                    start = Offset(200f, 200f),
                    end = Offset(0f, 100f)
                ),
                shape = RoundedCornerShape(30.dp)
            ),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = calories.toInt().toString(),
                fontWeight = FontWeight.ExtraBold,
                color = textProgressColor,
                fontSize = 22.sp
            )

            CircularProgressIndicator(
                progress = animatedProgress,
                color = outerProgressColor,
                strokeWidth = 10.dp,
                modifier = Modifier.size(130.dp)
            )

            CircularProgressIndicator(
                progress = animatedProgress,
                color = innerProgressColor,
                strokeWidth = 8.dp,
                modifier = Modifier
                    .size(105.dp)
                    .graphicsLayer(
                        rotationZ = 180f
                    )
            )
        }

        CaloriesStats()
    }
}

@Composable
fun CaloriesStats() {
    val color = Color(parseColor("#F58BA4"))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(25.dp, 0.dp, 25.dp, 0.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Stats(name = "Consumed", color = Color.Green, number = "1600")

        Stats(name = "Burned", color = Color.Blue, number = "600")

        Stats(name = "Heart Points", color = color, number = "30")
    }
}

@Composable
fun MacrosStats(macros: List<Double>) {
    val color1 = Color(parseColor("#FA9B31"))
    val color2 = Color(parseColor("#2CB9B0"))
    val color3 = Color(parseColor("#6C0D8F"))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(45.dp, 0.dp, 45.dp, 0.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Stats(name = "Protein", color = color1, number = macros[1].toString())

        Stats(name = "Carbs", color = color2, number = macros[2].toString())

        Stats(name = "Fats", color = color3, number = macros[3].toString())
    }
}

@Composable
fun Stats(name: String, color: Color, number: String) {
    Column(
        modifier = Modifier
    ) {
        Text(
            text = name,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Square(
                shape = RectangleShape,
                color = color
            )

            Text(
                text = number,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(3.dp, 0.dp, 0.dp, 0.dp)
            )
        }

    }
}

@Composable
fun Square(shape: Shape, color: Color) {
    Box(
        modifier = Modifier
            .size(10.dp)
            .clip(shape)
            .background(color)
    )
}
