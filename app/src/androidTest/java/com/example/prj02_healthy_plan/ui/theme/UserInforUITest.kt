package com.example.prj02_healthy_plan.ui.theme

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class UserInforUITest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun fullNameBoxUpdatesStateOnValueChange() {
        val name = mutableStateOf("")
        composeTestRule.setContent {
            FullNameBox(name)
        }

        val newFullName = "John Doe"
        composeTestRule.onNodeWithText("Full name").performTextInput(newFullName)
        composeTestRule.onNodeWithText("Full name").assertTextContains(newFullName)
    }

    @Test
    fun heightBoxUpdatesStateOnValueChange() {
        val h = mutableIntStateOf(0)
        composeTestRule.setContent {
            HeightBox(h)
        }

        val nH = 180
        composeTestRule.onNodeWithText("Current Height (cm)").performTextInput(nH.toString())
        composeTestRule.onNodeWithText("Current Height (cm)").assertTextContains(nH.toString())
    }

    @Test
    fun testGenderSelection() {
        // Create a MutableState for gender and dob
        val genderState = mutableStateOf(0)
        val dobState = mutableStateOf("")

        // Set the content to be tested
        composeTestRule.setContent {
            GenderAndDOB(genderState = genderState, dobState = dobState)
        }

        // Initial state
        composeTestRule.onNodeWithText("Gender").assertExists()
        composeTestRule.onNodeWithText("Male").assertExists()

        // Open the dropdown menu
        composeTestRule.onNodeWithText("Male").performClick()

        // Select "Female"
        composeTestRule.onNodeWithText("Female").performClick()
        composeTestRule.onNodeWithText("Female").assertExists()

        // Verify gender state is updated
        assert(genderState.value == 1)
    }

    @Test
    fun testActivityLevelSelection() {
        // Create a MutableState for activity level and weekly goal
        val activityLevelState = mutableStateOf(0)
        val weeklyGoalState = mutableStateOf("0.0")

        // Set the content to be tested
        composeTestRule.setContent {
            ActivityLevelRow(activityLevelState = activityLevelState, weeklyGoalState = weeklyGoalState)
        }

        // Initial state
        composeTestRule.onNodeWithText("Activity Level").assertExists()
        composeTestRule.onNodeWithText("Rarely").assertExists()

        // Open the dropdown menu
        composeTestRule.onNodeWithText("Rarely").performClick()

        // Select "Moderate"
        composeTestRule.onNodeWithText("Moderate").performClick()
        composeTestRule.onNodeWithText("Moderate").assertExists()

        // Verify activity level state is updated
        assert(activityLevelState.value == 1)
    }

    @Test
    fun testWeeklyGoalInput() {
        // Create MutableState for activity level and weekly goal
        val activityLevelState = mutableStateOf(0)
        val weeklyGoalState = mutableStateOf("")

        // Set the content to be tested
        composeTestRule.setContent {
            ActivityLevelRow(activityLevelState = activityLevelState, weeklyGoalState = weeklyGoalState)
        }

        // Verify initial state of the weekly goal input
        composeTestRule.onNodeWithText("Weekly Goal (kg)").assertExists()

        // Input new weekly goal value
        composeTestRule.onNodeWithText("Weekly Goal (kg)").performTextInput("0.5")

        // Verify weekly goal state is updated
        assert(weeklyGoalState.value == "0.5")
    }

    @Test
    fun testCaloriesGoalInput() {
        // Create a MutableState for calories goal and nutrient goal
        val caloriesGoalState = mutableStateOf(0)
        val nutrientGoalState = mutableStateOf(0)

        // Set the content to be tested
        composeTestRule.setContent {
            CaloriesRow(caloriesGoalState = caloriesGoalState, nutrientGoalState = nutrientGoalState)
        }

        // Verify initial calories goal value
        composeTestRule.onNodeWithTag("CaloriesGoalInput").assertExists()
        composeTestRule.onNodeWithTag("CaloriesGoalInput").assertTextContains("")

        // Input new calories goal value
        composeTestRule.onNodeWithTag("CaloriesGoalInput").performTextInput("2000")

        // Verify calories goal state is updated
        assert(caloriesGoalState.value == 2000)
    }

    @Test
    fun testNutrientGoalSelection() {
        // Create a MutableState for calories goal and nutrient goal
        val caloriesGoalState = mutableStateOf(0)
        val nutrientGoalState = mutableStateOf(0)

        // Set the content to be tested
        composeTestRule.setContent {
            CaloriesRow(caloriesGoalState = caloriesGoalState, nutrientGoalState = nutrientGoalState)
        }

        // Initial state
        composeTestRule.onNodeWithTag("NutrientGoalDropdown").assertExists()
        composeTestRule.onNodeWithText("More Fiber").assertExists()

        // Open the dropdown menu
        composeTestRule.onNodeWithTag("NutrientGoalDropdown").performClick()

        // Select "Balance"
        composeTestRule.onNodeWithTag("BalanceOption").performClick()
        composeTestRule.onNodeWithText("Balance").assertExists()

        // Verify nutrient goal state is updated
        assert(nutrientGoalState.value == 1)
    }

    @Test
    fun testStartingWeightInput() {
        // Create a MutableState for weight and target weight
        val weightState = mutableStateOf(0)
        val targetWeightState = mutableStateOf(0)

        // Set the content to be tested
        composeTestRule.setContent {
            WeightRow(weightState = weightState, targetWeightState = targetWeightState)
        }

        // Verify initial starting weight value
        composeTestRule.onNodeWithTag("StartingWeightInput").assertExists()
        composeTestRule.onNodeWithTag("StartingWeightInput").assertTextContains("")

        // Input new starting weight value
        composeTestRule.onNodeWithTag("StartingWeightInput").performTextInput("70")

        // Verify starting weight state is updated
        assert(weightState.value == 70)
    }

    @Test
    fun testTargetWeightInput() {
        // Create a MutableState for weight and target weight
        val weightState = mutableStateOf(0)
        val targetWeightState = mutableStateOf(0)

        // Set the content to be tested
        composeTestRule.setContent {
            WeightRow(weightState = weightState, targetWeightState = targetWeightState)
        }

        // Verify initial target weight value
        composeTestRule.onNodeWithTag("TargetWeightInput").assertExists()
        composeTestRule.onNodeWithTag("TargetWeightInput").assertTextContains("")

        // Input new target weight value
        composeTestRule.onNodeWithTag("TargetWeightInput").performTextInput("65")

        // Verify target weight state is updated
        assert(targetWeightState.value == 65)
    }

    @Test
    fun testGoalSelection() {
        // Create a MutableState for goal
        val goalState = mutableStateOf(0)

        // Set the content to be tested
        composeTestRule.setContent {
            Goal(goalState = goalState)
        }

        // Initial state
        composeTestRule.onNodeWithTag("GoalDropdown").assertExists()
        composeTestRule.onNodeWithText("Lose weight").assertExists()

        // Open the dropdown menu
        composeTestRule.onNodeWithTag("GoalDropdown").performClick()

        // Select "Gain muscles"
        composeTestRule.onNodeWithTag("GainMusclesOption").performClick()
        composeTestRule.onNodeWithText("Gain muscles").assertExists()

        // Verify goal state is updated
        assert(goalState.value == 1)
    }
}