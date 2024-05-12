package com.example.prj02_healthy_plan.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
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
        fullNameBox(name = "xxx")
        Spacer(Modifier.height(30.dp))
        heightBox(height = 180)
        Spacer(Modifier.height(30.dp))
        genderAndDOB(genderNumber = 2, dob = "21/11/2003")
        Spacer(Modifier.height(30.dp))
        activityLevelRow(activityLevel = 0)
        Spacer(Modifier.height(30.dp))
        caloriesRow(caloriesGoal = 2000, nutrientGoal = 0)
        Spacer(Modifier.height(30.dp))
        weightRow(weight = 80, targetWeight = 60)
        Spacer(Modifier.height(30.dp))
        goal(goalNumber = 0)
    }
}

@Composable
fun fullNameBox(name: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(height = 60.dp)
    ) {
        var value by remember {
            mutableStateOf(name)
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
fun heightBox(height: Number?) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(height = 60.dp)
    ) {
        var value by remember {
            mutableStateOf(height.toString())
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
fun genderAndDOB(genderNumber: Number, dob: String) {

    var isExpanded by remember {
        mutableStateOf(false)
    }

    var gender by remember {
        mutableStateOf("")
    }

    if (genderNumber == 0) {
        gender = "Male"
    } else if (genderNumber == 1) {
        gender = "Female"
    } else if (genderNumber == 2) {
        gender = "Not Specific"
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
//            setDOB("17-11-2003")
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
                    setDOB(dobd)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun activityLevelRow(activityLevel: Number) {

    var isExpanded by remember {
        mutableStateOf(false)
    }

    var level by remember {
        mutableStateOf("")
    }

    if (activityLevel == 0) {
        level = "Rarely"
    } else if (activityLevel == 1) {
        level = "Moderate"
    } else if (activityLevel == 2) {
        level = "Frequently"
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
fun caloriesRow(caloriesGoal: Number, nutrientGoal: Number) {

    var isExpanded by remember {
        mutableStateOf(false)
    }

    var goal by remember {
        mutableStateOf("")
    }

    if (nutrientGoal == 0) {
        goal = "More Fiber"
    } else if (nutrientGoal == 1) {
        goal = "Balance"
    } else if (nutrientGoal == 2) {
        goal = "More Protein"
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
                mutableStateOf(caloriesGoal.toString())
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
fun weightRow(weight: Number, targetWeight: Number) {
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
                mutableStateOf(weight.toString())
            }
            OutlinedTextField(
                value = startValue,
                onValueChange = {startValue = it},
                label = { Text("Starting Weight (kg)") },
                modifier = Modifier
                    .requiredHeight(60.dp)
                    .weight(1F))

            var goalValue by remember {
                mutableStateOf(targetWeight.toString())
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
fun goal(goalNumber: Number) {
    var isExpanded by remember {
        mutableStateOf(false)
    }

    var goal by remember {
        mutableStateOf("")
    }

    if (goalNumber == 0) {
        goal = "Lose Weight"
    } else if (goalNumber == 1) {
        goal = "Lose Weight & Gain Muscles"
    } else if (goalNumber == 2) {
        goal = "Gain Muscles"
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

@Preview
@Composable
fun TestInforUI() {
    val navigationController = rememberNavController()
    UserInforUI(navController = navigationController)
}