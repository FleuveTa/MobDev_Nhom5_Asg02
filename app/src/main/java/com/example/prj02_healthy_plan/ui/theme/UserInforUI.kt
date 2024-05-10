package com.example.prj02_healthy_plan.ui.theme

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavController
import com.example.prj02_healthy_plan.R


@Composable
fun UserInforUI(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(245, 250, 255))
    ) {
        UserInforHeader(navController)
        Spacer(Modifier.height(30.dp))
        fullNameBox()
        Spacer(Modifier.height(30.dp))
        heightBox()
        Spacer(Modifier.height(30.dp))
        genderAndDOB()
        Spacer(Modifier.height(30.dp))
        activityLevelRow()
        Spacer(Modifier.height(30.dp))
        caloriesRow()
        Spacer(Modifier.height(30.dp))
        weightRow()
        Spacer(Modifier.height(30.dp))
        goal()
    }
}

@Composable
fun fullNameBox() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(height = 60.dp)
    ) {
        var value by remember {
            mutableStateOf("Doan Minh Hoang")
        }
        OutlinedTextField(
            value = value,
            onValueChange = {value = it},
            label = { Text("Full name") },
            modifier = Modifier
                .fillMaxWidth()
                .requiredHeight(60.dp)
                .align(Alignment.Center)
        )
    }
}

@Composable
fun heightBox() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(height = 60.dp)
    ) {
        var value by remember {
            mutableStateOf("175")
        }
        OutlinedTextField(
            value = value,
            onValueChange = {value = it},
            label = { Text("Current Height (cm)") },
            modifier = Modifier
                .fillMaxWidth()
                .requiredHeight(60.dp)
                .align(Alignment.Center)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun genderAndDOB() {

    var isExpanded by remember {
        mutableStateOf(false)
    }

    var gender by remember {
        mutableStateOf("Male")
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(height = 60.dp)
    ) {

        //// Gender
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .fillMaxWidth()
        )
        {
            ExposedDropdownMenuBox(
                expanded = isExpanded,
                onExpandedChange = { isExpanded = it },
                modifier = Modifier.weight(1F)
            ) {
                OutlinedTextField(
                    value = gender,
                    label = { Text("Gender") },
                    onValueChange = {},
                    readOnly = true,
                    colors = ExposedDropdownMenuDefaults.textFieldColors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent
                    ),
                    modifier = Modifier.menuAnchor(),
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                    })
                ExposedDropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false },
                ) {
                    DropdownMenuItem(
                        text = { Text(text = "Male") },
                        onClick = {
                            gender = "Male"
                            isExpanded = false
                        })
                    DropdownMenuItem(
                        text = { Text(text = "Female") },
                        onClick = {
                            gender = "Female"
                            isExpanded = false
                        })
                    DropdownMenuItem(
                        text = { Text(text = "Other") },
                        onClick = {
                            gender = "Other"
                            isExpanded = false
                        })
                }
            }

            // DOB
            setDOB("17-11-2003")
            var dobd by remember {
                mutableStateOf(dob)
            }

            var showDatePicker by remember { mutableStateOf(false) }

            val transparentButtonColors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
            )

            Box(modifier = Modifier.weight(1F))
            {
                OutlinedTextField(
                    value = dobd,
                    onValueChange = {},
                    label = { Text("DoB") },
                    modifier = Modifier
                        .fillMaxSize()
                        .requiredHeight(60.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Black,
                        unfocusedBorderColor = Black)
                )
                Button(onClick = { showDatePicker = true },
                    colors = transparentButtonColors,
                    modifier = Modifier.fillMaxSize())
                {}
            }

            if (showDatePicker) {
                CustomDatePickerDialog(label = "DOB", dateStr = dobd) {
                    dobd = dob
                    showDatePicker = false
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun activityLevelRow() {

    var isExpanded by remember {
        mutableStateOf(false)
    }

    var level by remember {
        mutableStateOf("Moderate")
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(height = 60.dp)
    ) {

        //// Active Level
        Row (
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .fillMaxWidth())
        {
            ExposedDropdownMenuBox(
                expanded = isExpanded,
                onExpandedChange = {isExpanded = it},
                modifier =  Modifier.weight(1F)
            ) {
                OutlinedTextField(
                    value = level,
                    label = {Text("Activity Level")},
                    onValueChange = {},
                    readOnly = true,
                    colors = ExposedDropdownMenuDefaults.textFieldColors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent
                    ),
                    modifier = Modifier.menuAnchor(),
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                    })
                ExposedDropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = {isExpanded = false},
                ) {
                    DropdownMenuItem(
                        text = { Text(text = "Rarely") },
                        onClick = {
                            level = "Rarely"
                            isExpanded = false
                        })
                    DropdownMenuItem(
                        text = { Text(text = "Moderate") },
                        onClick = {
                            level = "Moderate"
                            isExpanded = false
                        })
                    DropdownMenuItem(
                        text = { Text(text = "Frequently") },
                        onClick = {
                            level = "Frequently"
                            isExpanded = false
                        })
                }
            }
            var value by remember {
                mutableStateOf("-0.5")
            }

            /// Weekly Goal
            OutlinedTextField(
                value = value,
                onValueChange = {value = it},
                label = { Text("Weekly Goal (kg)") },
                modifier = Modifier
                    .requiredHeight(60.dp)
                    .weight(1F)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun caloriesRow() {

    var isExpanded by remember {
        mutableStateOf(false)
    }

    var goal by remember {
        mutableStateOf("Balance")
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(height = 60.dp)
    ) {
        Row (
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .fillMaxWidth())
        {

            // Calories Goal
            var value by remember {
                mutableStateOf("2234")
            }
            OutlinedTextField(
                value = value,
                onValueChange = {value = it},
                label = { Text("Calories Goal (cal)") },
                modifier = Modifier
                    .requiredHeight(60.dp)
                    .weight(1F)
            )


            // Nutrient Goal
            ExposedDropdownMenuBox(
                expanded = isExpanded,
                onExpandedChange = {isExpanded = it},
                modifier =  Modifier.weight(1F)
            ) {
                OutlinedTextField(
                    value = goal,
                    label = {Text("Nutrient Goal")},
                    onValueChange = {},
                    readOnly = true,
                    colors = ExposedDropdownMenuDefaults.textFieldColors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent
                    ),
                    modifier = Modifier.menuAnchor(),
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                    })
                ExposedDropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = {isExpanded = false},
                ) {
                    DropdownMenuItem(
                        text = { Text(text = "Balance") },
                        onClick = {
                            goal = "Balance"
                            isExpanded = false
                        })
                    DropdownMenuItem(
                        text = { Text(text = "More Protein") },
                        onClick = {
                            goal = "More Protein"
                            isExpanded = false
                        })
                    DropdownMenuItem(
                        text = { Text(text = "More Fiber") },
                        onClick = {
                            goal = "More Fiber"
                            isExpanded = false
                        })
                }
            }
        }
    }
}

@Composable
fun weightRow() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(height = 60.dp)
    ) {
        Row (
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .fillMaxWidth())
        {

            // Starting Weight
            var startValue by remember {
                mutableStateOf("80")
            }
            OutlinedTextField(
                value = startValue,
                onValueChange = {startValue = it},
                label = { Text("Starting Weight (kg)") },
                modifier = Modifier
                    .requiredHeight(60.dp)
                    .weight(1F))

            var goalValue by remember {
                mutableStateOf("70")
            }

            // Target Weight
            OutlinedTextField(
                value = goalValue,
                onValueChange = {goalValue = it},
                label = { Text("Target Weight (kg)") },
                modifier = Modifier
                    .requiredHeight(60.dp)
                    .weight(1F))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun goal() {
    var isExpanded by remember {
        mutableStateOf(false)
    }

    var goal by remember {
        mutableStateOf("Lose weight, Gain muscles")
    }

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = {isExpanded = it},
    ) {
        OutlinedTextField(
            value = goal,
            label = {Text("Goal")},
            onValueChange = {},
            readOnly = true,
            colors = ExposedDropdownMenuDefaults.textFieldColors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent
            ),
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
            })
        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = {isExpanded = false},
        ) {
            DropdownMenuItem(
                text = { Text(text = "Lose weight") },
                onClick = {
                    goal = "Lose weight"
                    isExpanded = false
                })
            DropdownMenuItem(
                text = { Text(text = "Gain muscles") },
                onClick = {
                    goal = "Gain muscles"
                    isExpanded = false
                })
            DropdownMenuItem(
                text = { Text(text = "Lose weight, Gain muscles") },
                onClick = {
                    goal = "Lose weight, Gain muscles"
                    isExpanded = false
                })
        }
    }
}

//@Preview(widthDp = 342, heightDp = 50)
//@Composable
//private fun DefaultPreview() {
//    goal()
//}


@Composable
fun UserInforHeader(navController: NavController) {
    Row (modifier = Modifier
        .fillMaxWidth()
        .height(50.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically)
        {
            IconButton(onClick = { navController.navigate("more")}) {
                Icon(
                    painter = painterResource(id = R.drawable.left_arrow),
                    contentDescription = "Vector",
                )
            }
            Text(
                text = "Edit profile",
                color = Color(0xFF000000),
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Left,
                modifier = Modifier.weight(2F, true),
                fontWeight = FontWeight.Medium)

        }

}

