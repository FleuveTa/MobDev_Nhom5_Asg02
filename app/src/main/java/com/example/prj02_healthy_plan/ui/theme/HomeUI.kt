package com.example.prj02_healthy_plan.ui.theme

import PastOrPresentSelectableDates
import android.graphics.Color.parseColor
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
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.prj02_healthy_plan.R
import com.example.prj02_healthy_plan.activities.TungAnh
import convertMillisToDate

val textProgressColor = Color(parseColor("#3B3B3B"))

@Composable
fun HomeUI(nav: NavController, date: String = "Today") {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(245, 250, 255))
    ) {
        Header(nav, date)
        Content()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Header(nav: NavController) {
    val datePickerState = rememberDatePickerState(
        selectableDates = PastOrPresentSelectableDates
    )
    val selectedDateLabel = remember { mutableStateOf("Today") }
    val openDialog = remember { mutableStateOf(false) }
    val calendarPickerMainColor = Color(0xFF722276)


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(
            onClick = {openDialog.value = true},
            modifier = Modifier.weight(1.5F, true)
        ) {

            Text(
                text = selectedDateLabel.value,
                fontSize = 10.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "DropDown Icon")
        }

        Text(
            text = "HealthyPlans",
            fontSize = 30.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(3F, true),
            color = Color.Green,
            fontWeight = FontWeight.Medium)

        IconButton(
            onClick = { /*TODO*/ },
            Modifier.weight(1F, true)
        ) {
            Icon(
                imageVector = Icons.Outlined.Notifications,
                contentDescription = "Notification")
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
                        selectedDateLabel.value =
                            datePickerState.selectedDateMillis?.convertMillisToDate() ?: ""
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
}

@Composable
fun Content() {
    val waterColor = Color(parseColor("#63e5ff"))

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Color(245, 250, 255)),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        HomeTabScreen()

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
                    onClick = { /*TODO*/ }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Remove",
                        modifier = Modifier.size(30.dp),
                        tint = Color.Black)
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_water_drop_24),
                        contentDescription = "Water",
                        modifier = Modifier.size(60.dp),
                        tint = waterColor)

                    Text(text = "1.25l",
                        color = textProgressColor,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium)

                    Text(text = "Daily Water",
                        color = textProgressColor,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold)
                }

                IconButton(
                    onClick = { /*TODO*/ }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_add_circle_24),
                        contentDescription = "Add",
                        modifier = Modifier.size(30.dp),
                        tint = Color.Green)
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
                        fontSize = 16.sp)

                    CircularProgressIndicator(
                        progress = 0.3f,
                        color = Color.Green,
                        strokeWidth = 10.dp,
                        modifier = Modifier.size(70.dp)
                    )
                }

                Text(text = "Steps Walked",
                    color = textProgressColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold)
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
                        fontSize = 18.sp)

                    Icon(painter = painterResource(R.drawable.outline_sensor_occupied_24),
                        contentDescription = "Personal",
                        tint = Color.Red,
                        modifier = Modifier.size(40.dp))
                }

                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row {
                        Icon(painter = painterResource(R.drawable.baseline_access_time_24),
                            contentDescription = "Amount of Time",
                            tint = Color.Blue,
                            modifier = Modifier
                                .size(25.dp)
                                .padding(end = 5.dp))

                        Text(
                            text = "75 minutes",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium)
                    }

                    Row {
                        Icon(painter = painterResource(R.drawable.baseline_local_fire_department_24),
                            contentDescription = "Amount of Time",
                            tint = Color(parseColor("#FA9B31")),
                            modifier = Modifier
                                .size(25.dp)
                                .padding(end = 5.dp))

                        Text(
                            text = "350 Cals",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium)
                    }
                }

                IconButton(
                    onClick = { /*TODO*/ }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_add_circle_24),
                        contentDescription = "Add",
                        modifier = Modifier.size(40.dp),
                        tint = Color.Green)
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
                        modifier = Modifier.size(60.dp))

                    Icon(painter = painterResource(R.drawable.outline_scale_24),
                        contentDescription = "Scale",
                        tint = Color.Blue,
                        modifier = Modifier.size(35.dp))
                }

                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "70 kg",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold)

                    Text(
                        text = "You have a healthy BMI",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Light)
                }

                IconButton(
                    onClick = { /*TODO*/ }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_add_circle_24),
                        contentDescription = "Add",
                        modifier = Modifier.size(40.dp),
                        tint = Color.Green)
                }
            }
        }
    }
}

@Composable
fun HomeTabScreen() {
    var tabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Macros", "Calories")

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
            0 -> MacrosScreen()
            1 -> CaloriesScreen()
        }
    }
}

@Composable
fun MacrosScreen() {
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
            MacrosProgress(progress = 0.6, number = "245", color = progress1)

            MacrosProgress(progress = 0.6, number = "0", color = progress2)

            MacrosProgress(progress = 0.6, number = "12", color = progress3)
        }

        MacrosStats()
    }
}

@Composable
fun MacrosProgress(progress: Double, number: String, color: Color) {


    Box(contentAlignment = Alignment.Center) {
        Text(
            text = number + "g",
            fontWeight = FontWeight.ExtraBold,
            color = textProgressColor,
            fontSize = 22.sp)

        CircularProgressIndicator(
            progress = progress.toFloat(),
            color = color,
            strokeWidth = 8.dp,
            modifier = Modifier.size(100.dp)
        )
    }
}

@Composable
fun CaloriesScreen() {
    val innerProgressColor = Color(parseColor("#F58BA4"))
    val outerProgressColor = Color(parseColor("#4ED22D"))


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
                text = "2000",
                fontWeight = FontWeight.ExtraBold,
                color = textProgressColor,
                fontSize = 22.sp)

            CircularProgressIndicator(
                progress = 0.3f,
                color = outerProgressColor,
                strokeWidth = 10.dp,
                modifier = Modifier.size(130.dp)
            )

            CircularProgressIndicator(
                progress = 0.6f,
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

    Row (
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
fun MacrosStats() {
    val color1 = Color(parseColor("#FA9B31"))
    val color2 = Color(parseColor("#2CB9B0"))
    val color3 = Color(parseColor("#6C0D8F"))

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(45.dp, 0.dp, 45.dp, 0.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Stats(name = "Protein", color = color1, number = "245g")

        Stats(name = "Carbs", color = color2, number = "0g")

        Stats(name = "Fats", color = color3, number = "12g")
    }
}

@Composable
fun Stats(name: String, color: Color, number: String) {
    Column (
        modifier = Modifier
    ) {
        Text(
            text = name,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium)

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Square(
                shape = RectangleShape,
                color = color)

            Text(
                text = number,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(3.dp, 0.dp, 0.dp, 0.dp))
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

//@Preview
//@Composable
//fun PreviewHomeUI() {
//    TungAnh(nav)
//}