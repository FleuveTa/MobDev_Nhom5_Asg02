package com.example.prj02_healthy_plan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import com.example.prj02_healthy_plan.ui.theme.Content
import com.example.prj02_healthy_plan.ui.theme.Header
import com.example.prj02_healthy_plan.uiModel.DailyDataViewModel
import com.example.prj02_healthy_plan.uiModel.IngredientViewModel
import com.example.prj02_healthy_plan.uiModel.RecipeViewModel
import com.example.prj02_healthy_plan.uiModel.UserViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkClass
import kotlinx.coroutines.flow.StateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeUIScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var mockDailyDataViewModel : DailyDataViewModel
    private lateinit var mockUserViewModel : UserViewModel
    val currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
    var dailyData = mutableStateOf(DailyData(
        id = "1",
        user = null,
        water = 0,
        intake = null,
        burned = 1000,
        steps = 1000,
        currentDate,
        breakfast = null,
        lunch = null,
        dinner = null,
    ))


    @Before
    fun setUp() {
        // Giả lập ViewModel với mockk
        mockDailyDataViewModel = mockkClass(DailyDataViewModel::class)
        mockUserViewModel = mockkClass(UserViewModel::class)


        var user = User(
            "Nguyen Van A",
            170,
            1,
            "01-01-2000",
            1,
            "-0.5",
            2000,
            1,
            70,
            65,
            1,
            avatar = null
        )

        // Giả lập dữ liệu trả về từ ViewModel
        every {
            mockUserViewModel.state.value
        } returns user


        every {mockDailyDataViewModel.addWater()} answers {
            val newDailyData = dailyData.value.copy()
            newDailyData.water = newDailyData.water?.plus(1)

            dailyData.value = newDailyData
        }
        every {mockDailyDataViewModel.minusWater()} answers {
            val newDailyData = dailyData.value.copy()
            newDailyData.water = newDailyData.water?.minus(1)

            dailyData.value = newDailyData
        }
        every {mockDailyDataViewModel.fetchDailyData(currentDate)} returns Unit

        every { mockDailyDataViewModel.dailyData.value } returns dailyData.value
        every { mockDailyDataViewModel.dailyData } returns mockk<StateFlow<DailyData>>() {
            every { value } returns dailyData.value
        }
    }

    @Test
    fun testHomeScreen() {
        // Test Home Screen
        composeTestRule.setContent {
            val currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
            val date = remember { mutableStateOf(currentDate) }
            val user = mockUserViewModel.state.value
            val nav = rememberNavController()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(245, 250, 255))
            ) {
                Header(nav, date)
                Content(user, mockDailyDataViewModel, dailyData = dailyData.value)
            }
        }

        // Kiểm tra dữ liệu hiển thị trên màn hình
        composeTestRule.onNodeWithTag("datePickerButton").assertExists()
        composeTestRule.onNodeWithTag("addWaterButton").assertExists()
        composeTestRule.onNodeWithTag("minusWaterButton").assertExists()
        composeTestRule.onNodeWithTag("waterText").assertExists()

        composeTestRule.onNodeWithTag("addWaterButton").performClick()

        composeTestRule.onNodeWithTag("waterText").assertTextEquals("1 liters")
    }

}