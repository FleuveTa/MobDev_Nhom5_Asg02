package com.example.prj02_healthy_plan.ui.theme

import android.widget.CalendarView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import java.text.SimpleDateFormat
import java.util.Calendar


@Composable
fun CalendarUI(nav: NavController) {
    var date by remember {
        mutableStateOf("Today")
    }

//    val nav = rememberNavController()

    val today = Calendar.getInstance()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(245, 250, 255))
    ) {
        CalendarHeader(nav, date)

        AndroidView(factory = {CalendarView(it)}, update = {
            it.setOnDateChangeListener{calendarView, year, month, day ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, day)
                val formatter = SimpleDateFormat("dd-MM-yy")
                val formattedDate = formatter.format(selectedDate.time)
                if (selectedDate.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                    selectedDate.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
                    selectedDate.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH)) {
                    date = "Today"
                } else {
                    date = formattedDate
                }

                nav.navigate("home/$date")
            }
        },
            modifier = Modifier.align(Alignment.CenterHorizontally).fillMaxSize())
    }
}

@Composable
fun CalendarHeader(nav: NavController, date: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(
            onClick = {
            },
            modifier = Modifier.weight(1.5F, true)
        ) {
            Text(
                text = date,
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
}

