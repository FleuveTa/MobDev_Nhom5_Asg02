package com.example.prj02_healthy_plan.ui.theme

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test

class SignUpUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testSignUpUI() {
        // Set up mock functions
        val onSignupClickMock = mockk<() -> Unit>(relaxed = true)
        val toSigninMock = mockk<() -> Unit>(relaxed = true)

        // Launch the SignUpUI composable
        composeTestRule.setContent {
            SignUpUI(
                username = mutableStateOf(""),
                password = mutableStateOf(""),
                signup = onSignupClickMock,
                toSignin = toSigninMock
            )
        }

        // Verify if the necessary UI elements exist
        composeTestRule.onNodeWithText("Create your account").assertExists()
        composeTestRule.onNodeWithText("Email").assertExists()
        composeTestRule.onNodeWithTag("password_field").assertExists()
        composeTestRule.onNodeWithText("Sign Up").assertExists()
        composeTestRule.onNodeWithText("--------Or sign up with--------").assertExists()
        composeTestRule.onNodeWithText("To day is a new day. It's your day. You shape it.").assertExists()
        composeTestRule.onNodeWithText("Sign up to start becoming a better self.").assertExists()
        composeTestRule.onNodeWithTag("signin_button").assertExists()

        // Simulate user interactions
//        composeTestRule.onNodeWithTag("signup_button").performClick()

//        verify(exactly = 1) { onSignupClickMock.invoke() }

        // You can add more tests for other user interactions and verifications
    }
}