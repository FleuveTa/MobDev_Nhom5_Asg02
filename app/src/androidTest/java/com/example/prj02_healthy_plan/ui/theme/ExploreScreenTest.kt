package com.example.prj02_healthy_plan.ui.theme


import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.testing.TestNavHostController
import androidx.test.platform.app.InstrumentationRegistry
import com.example.prj02_healthy_plan.Ingredient
import com.example.prj02_healthy_plan.RecipeFirebase
import com.example.prj02_healthy_plan.uiModel.IngredientViewModel
import com.example.prj02_healthy_plan.uiModel.RecipeViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockkClass
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test

public class ExploreScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var mockIngredientViewModel: IngredientViewModel
    private lateinit var mockRecipeViewModel: RecipeViewModel
    val navController : NavController = TestNavHostController(InstrumentationRegistry.getInstrumentation().targetContext)

    @Before
    fun setUp() {
        // Giả lập ViewModel với mockk
        mockIngredientViewModel = mockkClass(IngredientViewModel::class)
        mockRecipeViewModel = mockkClass(RecipeViewModel::class)

        // Giả lập dữ liệu trả về từ ViewModel
        val userIngredients = mutableStateListOf(
            Ingredient(name = "Tomato", unit = "kg", nutrition = listOf(18.0))
        )

        val ingredientList = MutableStateFlow(mutableStateListOf(
            Ingredient(name = "Tomato", unit = "kg", nutrition = listOf(18.0))
        )) as StateFlow<List<Ingredient>>

        every { mockIngredientViewModel.userIngredients } returns userIngredients
        every { mockIngredientViewModel.ingredientList } returns ingredientList
        coEvery { mockIngredientViewModel.fetchIngredients() } returns Unit


        val recipeList = MutableStateFlow(
            listOf(RecipeFirebase(id = "1", name = "Tomato Soup", imageUrl = "", nutrition = listOf(150.0), ingredients = listOf()))
        ) as StateFlow<List<RecipeFirebase>>

        every { mockRecipeViewModel.recipeList } returns recipeList

        coEvery { mockRecipeViewModel.fetchRecipes() } returns Unit
        coEvery {mockIngredientViewModel.toggleIngredient(any()) } answers {
            // Lấy đối số truyền vào
            val ingredient = arg<Ingredient>(0)

            if (mockIngredientViewModel.userIngredients.contains(ingredient)) {
                mockIngredientViewModel.userIngredients.remove(ingredient)
            } else {
                mockIngredientViewModel.userIngredients.add(ingredient)
            }
        }


    }

    @Test
    fun userManuallyAddIngredientTest() {
        val searchInfo = mutableStateOf("")

        composeTestRule.setContent {
            val navController = rememberNavController()
            NavHost(navController, startDestination = Screens.Explore.screen) {
                composable(Screens.Explore.screen) {
                    ChienTa(
                        nav = navController,
                        searchInfo = searchInfo,
                        ingredientViewModel = mockIngredientViewModel,
                        recipeViewModel = mockRecipeViewModel
                    )
                }

                composable(Screens.UserAddIngredient.screen) {
                    UserSearchIngredientScreen( nav = navController, mockIngredientViewModel)
                }
            }
        }

        composeTestRule.onNodeWithTag("ingredientAddButton").assertExists()
        composeTestRule.onNodeWithTag("ingredientAddButton").performClick()

        composeTestRule.onNodeWithTag("ingredientSearchOptionButton").assertExists()
        composeTestRule.onNodeWithTag("ingredientSearchOptionButton").performClick()

        composeTestRule.onNodeWithTag("addIngredientManually").assertExists()
        composeTestRule.onNodeWithTag("addIngredientManually").performClick()

        composeTestRule.onNodeWithTag("ingredientAddButton").assertExists()
        composeTestRule.onNodeWithTag("ingredientAddButton").performClick()

//        composeTestRule.onNodeWithTag("SearchBar").performClick()
//        composeTestRule.onNodeWithTag("SearchBar").performTextInput("Tomato")

        composeTestRule.onNodeWithTag("UserSearchIngredientBackButton").assertExists()
        composeTestRule.onNodeWithTag("UserSearchIngredientBackButton").performClick()

        composeTestRule.onNodeWithTag("ingredientAddButton").assertExists()

        Thread.sleep(5000)
    }
}
