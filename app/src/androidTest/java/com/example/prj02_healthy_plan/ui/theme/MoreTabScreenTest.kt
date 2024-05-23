package com.example.prj02_healthy_plan.ui.theme

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.prj02_healthy_plan.ContextWrapper
import com.example.prj02_healthy_plan.FirebaseAuthWrapper
import com.example.prj02_healthy_plan.User
import com.example.prj02_healthy_plan.uiModel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import io.mockk.every
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

class MoreTabUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testMoreTabScreen() {
        val mockAuth = mockk<FirebaseAuthWrapper>()
        val mockContext = mockk<ContextWrapper>()
//        val userViewModel = mockk<UserViewModel>()
        every { mockAuth.getUID() } returns "1"
        every { mockAuth.getEmail() } returns "test@gmail.com"

        val user = User(
            fullName = "Test"
        )

        composeTestRule.setContent {
            val nav = rememberNavController()
            NavHost(nav, startDestination = Screens.More.screen) {
                composable(Screens.More.screen) {
                    MoreTabUI(auth = mockAuth, context = mockContext, nav = nav, user)
                }

                composable(Screens.UserInfor.screen) {
                    UserInforUI(navController = nav)
                }
            }
        }

        composeTestRule.onNodeWithTag("changeInforButton").assertExists()
        composeTestRule.onNodeWithTag("changeInforButton").performClick()
        composeTestRule.onNodeWithTag("userInfor").assertIsDisplayed()
    }
}