package com.example.prj02_healthy_plan

import android.content.Context
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.prj02_healthy_plan.activities.AddFoodScreen
import com.example.prj02_healthy_plan.activities.AdminScreen
import com.example.prj02_healthy_plan.uiModel.RecipeViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import io.mockk.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class AddFoodScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    lateinit var mockCollectionReferenceWrapper: FirestoreCollectionReferenceWrapper

    lateinit var mockStorageReferenceWrapper: StorageReferenceWrapper



    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        // Mocking methods in the services
        mockCollectionReferenceWrapper = mockk()
        mockStorageReferenceWrapper = mockk()
    }

    @Test
    fun testAddFoodScreen() {
        // Mock data
        val ingredientList = listOf<Ingredient>()

        composeTestRule.setContent {
            AddFoodScreen(
                ingredientList = ingredientList,
                recipeRef = mockCollectionReferenceWrapper,
                storageRef = mockStorageReferenceWrapper,
            )
        }

        // Check if the "Add Recipe" button exists
        composeTestRule.onNodeWithText("Add Recipe").assertExists()

        // Check if the text input fields exist with the default text
        composeTestRule.onNodeWithTag("foodName").assertExists()
        composeTestRule.onNodeWithTag("foodName").performTextInput("Tomato Soup")
        composeTestRule.onNodeWithTag("foodDescription").assertExists()
        composeTestRule.onNodeWithTag("foodDescription").performTextInput("A delicious tomato soup")

    }
}
