package com.example.prj02_healthy_plan.ui.theme

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.assertContentDescriptionEquals
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertHeightIsAtLeast
import androidx.compose.ui.test.assertWidthIsAtLeast
import androidx.compose.ui.unit.dp
import org.junit.Rule
import org.junit.Test

class GetStartedTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testGetStartedUIComponents() {
        composeTestRule.setContent {
            GetStarted()
        }

        // Check if the title "HealthyPlans" exists
        composeTestRule.onNodeWithText("HealthyPlans")
            .assertExists()
            .assertIsDisplayed()
            .assertTextEquals("HealthyPlans")

        // Check if the image with content description "Eating healthy food" exists
        composeTestRule.onNodeWithContentDescription("Eating healthy food")
            .assertExists()
            .assertIsDisplayed()
            .assertContentDescriptionEquals("Eating healthy food")
            .assertHeightIsAtLeast(300.dp)
            .assertWidthIsAtLeast(281.dp)

        // Check if the "Healthy Recipes" text exists
        composeTestRule.onNodeWithText("Healthy Recipes")
            .assertExists()
            .assertIsDisplayed()
            .assertTextEquals("Healthy Recipes")

        // Check if the subtitle text exists
        composeTestRule.onNodeWithText("Browse thousands of healthy recipes from all over the world.")
            .assertExists()
            .assertIsDisplayed()
            .assertTextEquals("Browse thousands of healthy recipes from all over the world.")

        // Check if the "Get Started" button exists and is clickable
        composeTestRule.onNodeWithText("Get Started")
            .assertExists()
            .assertIsDisplayed()
            .assertTextEquals("Get Started")
            .assertHasClickAction()
            .performClick()
    }
}
