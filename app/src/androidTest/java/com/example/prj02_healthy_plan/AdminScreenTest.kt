package com.example.prj02_healthy_plan

import android.content.Context
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.prj02_healthy_plan.activities.AdminScreen
import com.example.prj02_healthy_plan.uiModel.RecipeViewModel
import com.google.firebase.auth.FirebaseAuth
import io.mockk.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.junit.Rule
import org.junit.Test

class AdminScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    private lateinit var mockRecipeViewModel: RecipeViewModel


    @Test
    fun testAdminScreen() {
        // Set up any necessary mocks or dependencies here
        val mockAuth = mockk<FirebaseAuthWrapper>()
        val mockContext = mockk<ContextWrapper>()
        mockRecipeViewModel = mockkClass(RecipeViewModel::class)

        val recipeList = MutableStateFlow(
            listOf(
                RecipeFirebase(id = "1", name = "Tomato Soup", imageUrl = "", nutrition = listOf(150.0), ingredients = listOf()),
                RecipeFirebase(id = "2", name = "Fried Chicken", imageUrl = "", nutrition = listOf(150.0), ingredients = listOf())
            )
        ) as StateFlow<List<RecipeFirebase>>

        every { mockRecipeViewModel.recipeList } returns recipeList

        coEvery { mockRecipeViewModel.fetchRecipes() } returns Unit

        every { mockRecipeViewModel.myRecipeList } returns MutableStateFlow(emptyList())

        every { mockAuth.signOut() } just Runs


        // Set content for the Composable
        composeTestRule.setContent {
            AdminScreen(auth = mockAuth, context = mockContext, viewRecipeModel = mockRecipeViewModel)
        }

        // Assert that UI components exist
        composeTestRule.onNodeWithText("HealthyPlans").assertExists()
        composeTestRule.onNodeWithText("Edit Recipes").assertExists()
        composeTestRule.onNodeWithText("Add a new recipe").assertExists()
        composeTestRule.onNodeWithContentDescription("Logout Icon").assertExists()

        // Perform interactions (e.g., click events)
        composeTestRule.onNodeWithContentDescription("Logout Icon").performClick()

        // Verify the behavior after performing interactions
        // For example, verify that the signOut method was called
        verify { mockAuth.signOut() }
    }
}