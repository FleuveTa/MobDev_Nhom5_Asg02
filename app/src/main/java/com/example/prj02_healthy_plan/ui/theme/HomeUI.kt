package com.example.prj02_healthy_plan.ui.theme

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
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prj02_healthy_plan.activities.TungAnh

@Composable
fun HomeUI() {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Header()
        Content()
    }
}

@Composable
fun Header() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(Color.White),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ElevatedButton(
            onClick = { /*TODO*/ },
            modifier = Modifier.weight(1.5F, true),
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            )
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Time")

            Text(
                text = "Today",
                fontSize = 10.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.fillMaxWidth(1f)
            )
        }

        Text(
            text = "HealthyPLans",
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

@Composable
fun Content() {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        TabScreen()
    }
}

@Composable
fun TabScreen() {
    var tabIndex by remember { mutableStateOf(0) }
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
    val progress1 = Color(android.graphics.Color.parseColor("#FA9B31"))
    val progress2 = Color(android.graphics.Color.parseColor("#2CB9B0"))
    val progress3 = Color(android.graphics.Color.parseColor("#6C0D8F"))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
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
    val textProgressColor = Color(android.graphics.Color.parseColor("#3B3B3B"))

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

@Preview
@Composable
fun PreviewMacrosProgress() {
    MacrosScreen()
}

@Composable
fun CaloriesScreen() {
    val innerProgressColor = Color(android.graphics.Color.parseColor("#F58BA4"))
    val outerProgressColor = Color(android.graphics.Color.parseColor("#4ED22D"))
    val textProgressColor = Color(android.graphics.Color.parseColor("#3B3B3B"))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
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
    val color = Color(android.graphics.Color.parseColor("#F58BA4"))

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
    val color1 = Color(android.graphics.Color.parseColor("#FA9B31"))
    val color2 = Color(android.graphics.Color.parseColor("#2CB9B0"))
    val color3 = Color(android.graphics.Color.parseColor("#6C0D8F"))

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

@Preview
@Composable
fun PreviewHomeUI() {
    TungAnh()
}