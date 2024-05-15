package com.example.prj02_healthy_plan.ui.theme

import android.util.Log
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
import androidx.compose.runtime.MutableDoubleState
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.prj02_healthy_plan.R
import com.example.prj02_healthy_plan.uiModel.UserViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

@Composable
fun UserInforUI(navController: NavController) {
    val userViewModel: UserViewModel = viewModel()
    val user = userViewModel.state.value
    val auth: FirebaseAuth = Firebase.auth
    val uId = auth.currentUser?.uid
    val db = Firebase.firestore

    Log.d(
        "Tester5",
        user.toString()
    )

    val nameValue = remember(user.fullName) {
        mutableStateOf(user.fullName ?: "")
    }
    val heightValue = remember(user.height) {
        mutableIntStateOf(user.height ?: 0)
    }
    val genderValue = remember(user.gender) {
        mutableIntStateOf(user.gender ?: 3)
    }
    val dobValue = remember(user.dob) {
        mutableStateOf(user.dob ?: "1-1-1970")
    }
    val activityLevelValue = remember(user.activityLevel) {
        mutableIntStateOf(user.activityLevel ?: 3)
    }
    val weeklyGoalValue = remember(user.weeklyGoal) {
        mutableDoubleStateOf(user.weeklyGoal ?: 3.0)
    }
    val caloriesGoalValue = remember(user.caloriesGoal) {
        mutableIntStateOf(user.caloriesGoal ?: 3)
    }
    val nutrientGoalValue = remember(user.nutrientGoal) {
        mutableIntStateOf(user.nutrientGoal ?: 3)
    }
    val weightValue = remember(user.weight) {
        mutableIntStateOf(user.weight ?: 0)
    }
    val targetWeightValue = remember(user.targetWeight) {
        mutableIntStateOf(user.targetWeight ?: 0)
    }
    val goalValue = remember(user.goal) {
        mutableIntStateOf(user.goal ?: 3)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(245, 250, 255)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        UserInforHeader(navController)
        Spacer(Modifier.height(10.dp))
        FullNameBox(nameState = nameValue)
        Spacer(Modifier.height(10.dp))
        HeightBox(heightState = heightValue)
        Spacer(Modifier.height(10.dp))
        GenderAndDOB(genderState = genderValue, dobState = dobValue)
        Spacer(Modifier.height(10.dp))
        ActivityLevelRow(activityLevelState = activityLevelValue, weeklyGoalState = weeklyGoalValue)
        Spacer(Modifier.height(10.dp))
        CaloriesRow(caloriesGoalState = caloriesGoalValue, nutrientGoalState = nutrientGoalValue)
        Spacer(Modifier.height(10.dp))
        WeightRow(weightState = weightValue, targetWeightState = targetWeightValue)
        Spacer(Modifier.height(10.dp))
        Goal(goalState = goalValue)
        Spacer(Modifier.height(10.dp))
        Button(onClick = {
            if (uId != null) {
                saveUserChanges(
                    uId,
                    db,
                    nameValue,
                    heightValue,
                    genderValue,
                    dobValue,
                    activityLevelValue,
                    weeklyGoalValue,
                    caloriesGoalValue,
                    nutrientGoalValue,
                    weightValue,
                    targetWeightValue,
                    goalValue
                )
            }
        }) {
            Text("Save changes")
        }
    }
}


fun saveUserChanges(
    uId: String,
    db: FirebaseFirestore,
    nameValue: MutableState<String>,
    heightValue: MutableIntState,
    gender: MutableIntState,
    dobValue: MutableState<String>,
    activityLevelValue: MutableIntState,
    weeklyGoalValue: MutableDoubleState,
    caloriesGoalValue: MutableIntState,
    nutrientGoalValue: MutableIntState,
    weightValue: MutableIntState,
    targetWeightValue: MutableIntState,
    goalValue: MutableIntState
) {
    db.collection("users").document(uId).set(
        hashMapOf(
            "fullName" to nameValue.value,
            "height" to heightValue.intValue,
            "gender" to gender.intValue,
            "dob" to dobValue.value,
            "activityLevel" to activityLevelValue.intValue,
            "weeklyGoal" to weeklyGoalValue.doubleValue,
            "caloriesGoal" to caloriesGoalValue.intValue,
            "nutrientGoal" to nutrientGoalValue.intValue,
            "weight" to weightValue.intValue,
            "targetWeight" to targetWeightValue.intValue,
            "goal" to goalValue.intValue,
        ))
}
@Composable
fun FullNameBox(nameState: MutableState<String>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(height = 60.dp)
    ) {
        OutlinedTextField(
            value = nameState.value,
            onValueChange = { nameState.value = it },
            label = { Text("Full name") },
            modifier = Modifier
                .fillMaxWidth()
                .requiredHeight(60.dp)
                .align(Alignment.Center)
                .testTag("fullNameTextField")
        )
    }
}

@Composable
fun HeightBox(heightState: MutableState<Int>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(height = 60.dp)
    ) {
        OutlinedTextField(
            value = if (heightState.value == 0) "" else heightState.value.toString(),
            onValueChange = {
                if (it.isNotEmpty()) {
                    heightState.value = it.toIntOrNull() ?: 0
                } else {
                    heightState.value = 0
                }
            },
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
fun GenderAndDOB(genderState: MutableState<Int>, dobState: MutableState<String>) {
    var isExpanded by remember {
        mutableStateOf(false)
    }

    var gender by remember {
        mutableStateOf("")
    }

    if (genderState.value == 0) {
        gender = "Male"
    } else if (genderState.value == 1) {
        gender = "Female"
    } else if (genderState.value == 2) {
        gender = "Not Specific"
    } else {
        gender = ""
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
                            genderState.value = 0
                            isExpanded = false
                        })
                    DropdownMenuItem(
                        text = { Text(text = "Female") },
                        onClick = {
                            gender = "Female"
                            genderState.value = 1
                            isExpanded = false
                        })
                    DropdownMenuItem(
                        text = { Text(text = "Not Specific") },
                        onClick = {
                            gender = "Other"
                            genderState.value = 2
                            isExpanded = false
                        })
                }
            }

            var showDatePicker by remember { mutableStateOf(false) }

            val transparentButtonColors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
            )

            Box(modifier = Modifier.weight(1F))
            {
                OutlinedTextField(
                    value = dobState.value,
                    onValueChange = {},
                    label = { Text("DoB") },
                    modifier = Modifier
                        .fillMaxSize()
                        .requiredHeight(60.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Black,
                        unfocusedBorderColor = Black
                    )
                )
                Button(
                    onClick = { showDatePicker = true },
                    colors = transparentButtonColors,
                    modifier = Modifier.fillMaxSize()
                )
                {}
            }

            if (showDatePicker) {
                CustomDatePickerDialog(label = "DOB", dateStr = dobState.value) {
                    dobState.value = dob
                    setDOB(dobState.value)
                    showDatePicker = false
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityLevelRow(activityLevelState: MutableState<Int>, weeklyGoalState: MutableState<Double>) {
    var isExpanded by remember {
        mutableStateOf(false)
    }

    var level by remember {
        mutableStateOf("")
    }

    if (activityLevelState.value == 0) {
        level = "Rarely"
    } else if (activityLevelState.value == 1) {
        level = "Moderate"
    } else if (activityLevelState.value == 2) {
        level = "Frequently"
    } else {
        level = ""
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(height = 60.dp)
    ) {
        //// Active Level
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
                    value = level,
                    label = { Text("Activity Level") },
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
                        text = { Text(text = "Rarely") },
                        onClick = {
                            level = "Rarely"
                            activityLevelState.value = 0
                            isExpanded = false
                        })
                    DropdownMenuItem(
                        text = { Text(text = "Moderate") },
                        onClick = {
                            level = "Moderate"
                            activityLevelState.value = 1
                            isExpanded = false
                        })
                    DropdownMenuItem(
                        text = { Text(text = "Frequently") },
                        onClick = {
                            level = "Frequently"
                            activityLevelState.value = 2
                            isExpanded = false
                        })
                }
            }

            /// Weekly Goal
            OutlinedTextField(
                value = weeklyGoalState.value.toString(),
                onValueChange = {
                    if (it.isNotEmpty()) {
                        weeklyGoalState.value = it.toDoubleOrNull() ?: 0.0
                    } else {
                        weeklyGoalState.value = 0.0
                    }
                },
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
fun CaloriesRow(caloriesGoalState: MutableState<Int>, nutrientGoalState: MutableState<Int>) {
    var isExpanded by remember {
        mutableStateOf(false)
    }

    var goal by remember {
        mutableStateOf("")
    }

    if (nutrientGoalState.value == 0) {
        goal = "More Fiber"
    } else if (nutrientGoalState.value == 1) {
        goal = "Balance"
    } else if (nutrientGoalState.value == 2) {
        goal = "More Protein"
    } else {
        goal = ""
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(height = 60.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .fillMaxWidth()
        )
        {
            // Calories Goal
            OutlinedTextField(
                value = if (caloriesGoalState.value == 0) "" else caloriesGoalState.value.toString(),
                onValueChange = {
                    if (it.isNotEmpty()) {
                        caloriesGoalState.value = it.toIntOrNull() ?: 0
                    } else {
                        caloriesGoalState.value = 0
                    }
                },
                label = { Text("Calories Goal (cal)") },
                modifier = Modifier
                    .requiredHeight(60.dp)
                    .weight(1F)
            )

            // Nutrient Goal
            ExposedDropdownMenuBox(
                expanded = isExpanded,
                onExpandedChange = { isExpanded = it },
                modifier = Modifier.weight(1F)
            ) {
                OutlinedTextField(
                    value = goal,
                    label = { Text("Nutrient Goal") },
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
                        text = { Text(text = "Balance") },
                        onClick = {
                            goal = "Balance"
                            nutrientGoalState.value = 1
                            isExpanded = false
                        })
                    DropdownMenuItem(
                        text = { Text(text = "More Protein") },
                        onClick = {
                            goal = "More Protein"
                            nutrientGoalState.value = 2
                            isExpanded = false
                        })
                    DropdownMenuItem(
                        text = { Text(text = "More Fiber") },
                        onClick = {
                            goal = "More Fiber"
                            nutrientGoalState.value = 0
                            isExpanded = false
                        })
                }
            }
        }
    }
}

@Composable
fun WeightRow(weightState: MutableState<Int>, targetWeightState: MutableState<Int>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(height = 60.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .fillMaxWidth()
        )
        {

            // Starting Weight
            OutlinedTextField(
                value = if (weightState.value == 0) "" else weightState.value.toString(),
                onValueChange = {
                    if (it.isNotEmpty()) {
                        weightState.value = it.toIntOrNull() ?: 0
                    } else {
                        weightState.value = 0
                    }
                },
                label = { Text("Starting Weight (kg)") },
                modifier = Modifier
                    .requiredHeight(60.dp)
                    .weight(1F)
            )

            // Target Weight
            OutlinedTextField(
                value = if (targetWeightState.value == 0) "" else targetWeightState.value.toString(),
                onValueChange = {
                    if (it.isNotEmpty()) {
                        targetWeightState.value = it.toIntOrNull() ?: 0
                    } else {
                        targetWeightState.value = 0
                    }
                },
                label = { Text("Target Weight (kg)") },
                modifier = Modifier
                    .requiredHeight(60.dp)
                    .weight(1F)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Goal(goalState: MutableState<Int>) {
    var isExpanded by remember {
        mutableStateOf(false)
    }

    var goal by remember {
        mutableStateOf("")
    }

    if (goalState.value == 0) {
        goal = "Lose weight"
    } else if (goalState.value == 1) {
        goal = "Gain muscles"
    } else if (goalState.value == 2) {
        goal = "Lose weight, Gain muscles"
    } else {
        goal = ""
    }


    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { isExpanded = it },
    ) {
        OutlinedTextField(
            value = goal,
            label = { Text("Goal") },
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
            onDismissRequest = { isExpanded = false },
        ) {
            DropdownMenuItem(
                text = { Text(text = "Lose weight") },
                onClick = {
                    goal = "Lose weight"
                    goalState.value = 0
                    isExpanded = false
                })
            DropdownMenuItem(
                text = { Text(text = "Gain muscles") },
                onClick = {
                    goal = "Gain muscles"
                    goalState.value = 1
                    isExpanded = false
                })
            DropdownMenuItem(
                text = { Text(text = "Lose weight, Gain muscles") },
                onClick = {
                    goal = "Lose weight, Gain muscles"
                    goalState.value = 2
                    isExpanded = false
                })
        }
    }
}

@Composable
fun UserInforHeader(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    )
    {
        IconButton(onClick = { navController.navigate("more") }) {
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
            fontWeight = FontWeight.Medium
        )
    }
}