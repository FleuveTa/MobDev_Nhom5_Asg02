package com.example.prj02_healthy_plan.ui.theme

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick

import androidx.navigation.testing.TestNavHostController
import androidx.test.platform.app.InstrumentationRegistry
import com.example.prj02_healthy_plan.MyRecipe
import com.example.prj02_healthy_plan.RecipeFirebase
import com.example.prj02_healthy_plan.uiModel.RecipeViewModel
import com.google.firebase.FirebaseApp
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk

import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test

// test SearchResultScreen
class SearchResultScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var mockRecipeViewModel: RecipeViewModel

    @Before
    fun setup() {
        // Initialize Firebase
        FirebaseApp.initializeApp(InstrumentationRegistry.getInstrumentation().targetContext)

        // Create a mock of RecipeViewModel
        mockRecipeViewModel = mockk<RecipeViewModel>()

        // Mock the recipe list flow
        val mockRecipeList = listOf(
            RecipeFirebase(
                id = "1",
                name = "Pasta",
                description = "Delicious pasta recipe"
            ),
            RecipeFirebase(
                id = "2",
                name = "Pizza",
                description = "Tasty pizza recipe"
            )
        )
        val recipeListFlow = MutableStateFlow(mockRecipeList)
        every { mockRecipeViewModel.recipeList } returns recipeListFlow
        // mock the myRecipeList flow
        val mockMyRecipeList = listOf(
            MyRecipe(
                id = "1",
                userId = "1",
                user = null,
                recipes = mockRecipeList
            ),
            MyRecipe(
                id = "2",
                userId = "2",
                user = null,
                recipes = mockRecipeList
            )
        )
        val myRecipeListFlow = MutableStateFlow(mockMyRecipeList)
        every { mockRecipeViewModel.myRecipeList } returns myRecipeListFlow
        // mock fetchRecipes
        coEvery { mockRecipeViewModel.fetchRecipes() } answers {
            recipeListFlow.value = mockRecipeList
        }
    }

    @Test
    fun testSearchRecipeWithSearchInfo() {
        // Set up the initial search info
        val initialSearchInfo = "Pasta"

        // Create a mock NavController
        val navController = TestNavHostController(InstrumentationRegistry.getInstrumentation().targetContext)

        // Launch the SearchResultScreen with the mock RecipeViewModel and NavHostController
        // Set content
        composeTestRule.setContent {
            SearchResultScreen(
                nav = navController,
                searchInfo = mutableStateOf(initialSearchInfo),
                recipeViewModel = mockRecipeViewModel
            )
        }
        // Check if the search results are displayed
        composeTestRule.onNodeWithTag("recipeResult").assertIsDisplayed()

    }
    @Test
    fun testSearchRecipeWithEmptySearchInfo() {
        // Set up the initial search info
        val initialSearchInfo = ""

        // Create a mock NavController
        val navController = TestNavHostController(InstrumentationRegistry.getInstrumentation().targetContext)

        // Launch the SearchResultScreen with the mock RecipeViewModel and NavHostController
        // Set content
        composeTestRule.setContent {
            SearchResultScreen(
                nav = navController,
                searchInfo = mutableStateOf(initialSearchInfo),
                recipeViewModel = mockRecipeViewModel
            )
        }
        // Check if the search results are displayed
        composeTestRule.onNodeWithTag("resultList").assertIsDisplayed()
        // check if resultlist contains 2 items
        composeTestRule.onNodeWithTag("resultList").onChildren().assertCountEquals(2)
    }
}