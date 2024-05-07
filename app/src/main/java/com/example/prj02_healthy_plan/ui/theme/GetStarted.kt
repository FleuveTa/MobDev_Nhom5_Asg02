package com.example.prj02_healthy_plan.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.prj02_healthy_plan.R

@Composable
fun GetStarted() {
    Column {
        Box(modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(40.dp)
            .weight(1F))
        {
            Text(
                text = "HealthyPlans",
                color = Color(0xff4ed22d),
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold),
                modifier = Modifier.align(Alignment.Center))
        }

        Box(modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(300.dp)
            .weight(2F))
        {
            Image(
                painter = painterResource(id = R.drawable.get_started_img),
                contentDescription = "Eating healthy food",
                modifier = Modifier
                    .requiredWidth(width = 281.dp)
                    .requiredHeight(300.dp)
                    .align(Alignment.Center))
            Box(modifier = Modifier
                .fillMaxWidth()
                .offset(x=0.dp, y=260.dp))
            {
                Text(
                    text = "Healthy Recipes",
                    color = Color.Black.copy(alpha = 0.85f),
                    textAlign = TextAlign.Center,
                    lineHeight = 1.4.em,
                    style = TextStyle(
                        fontSize = 25.sp),
                    modifier = Modifier.align(Alignment.Center))
                Text(
                    text = "Browse thousands of healthy recipes from all over the world.",
                    color = Color.Black.copy(alpha = 0.45f),
                    textAlign = TextAlign.Center,
                    lineHeight = 1.4.em,
                    style = TextStyle(
                        fontSize = 17.sp),
                    modifier = Modifier
                        .requiredWidth(width = 300.dp)
                        .align(Alignment.Center)
                        .offset(x = 0.dp, y = 35.dp))
            }
        }

        Button(modifier = Modifier
            .requiredWidth(width = 290.dp)
            .requiredHeight(height = 72.dp)
            .clip(shape = RoundedCornerShape(24.dp))
            .background(color = Color(0xff91c788))
            .fillMaxWidth()
            .weight(1F)
            .align(Alignment.CenterHorizontally),
            onClick = {},
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Black // Set text color
            ))
        {
            Text(
                text = "Get Started",
                color = Color.White,
                style = TextStyle(
                    fontSize = 25.sp),
                modifier = Modifier)
        }
    }
}

@Preview
@Composable
private fun EatingHealthyFoodcuate1Preview() {
    GetStarted()
}