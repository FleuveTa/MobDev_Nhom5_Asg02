package com.example.prj02_healthy_plan.ui.theme

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
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


}