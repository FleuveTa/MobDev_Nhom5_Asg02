package com.example.prj02_healthy_plan.uiModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.prj02_healthy_plan.Ingredient
import com.example.prj02_healthy_plan.MyRecipeFirebase
import com.example.prj02_healthy_plan.RecipeFirebase
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import okhttp3.internal.concurrent.Task

class RecipeViewModel : ViewModel() {
    private val db = Firebase.firestore
    private val _recipeList = MutableStateFlow<List<RecipeFirebase>>(emptyList())
    private val _myRecipeList = MutableStateFlow<List<MyRecipeFirebase>>(emptyList())
    private val _ingredientList = MutableStateFlow<List<Ingredient>>(emptyList())
    val recipeList: StateFlow<List<RecipeFirebase>> = _recipeList.asStateFlow()
    val myRecipeList: StateFlow<List<MyRecipeFirebase>> = _myRecipeList.asStateFlow()
    val ingredientList: StateFlow<List<Ingredient>> = _ingredientList.asStateFlow()

    fun fetchRecipes() {
        db.collection("recipes").get().addOnSuccessListener { result ->
            val recipeList = result.documents.mapNotNull { document ->
                val recipe = document.toObject(RecipeFirebase::class.java)
                recipe?.id = document.id
                recipe
            }
            _recipeList.value = recipeList
        }
    }


    fun deleteRecipe(id: String) {
        db.collection("recipes").document(id).delete()
            .addOnSuccessListener {
                fetchRecipes()
            }
            .addOnFailureListener { e ->
                Log.w("Delete", "Error deleting document", e)
            }
    }

    suspend fun fetchMyRecipes() {
        val myRecipes = mutableListOf<MyRecipeFirebase>()

        try {
            // Get all myRecipes
            val myRecipesSnapshot = db.collection("myRecipes").get().await()


            for (doc in myRecipesSnapshot.documents) {
                val myRecipe = MyRecipeFirebase(id = doc.id)

                // Get user reference and recipe references
                val userRef = doc.get("user") as DocumentReference
                val recipeRefs = doc.get("recipes") as List<DocumentReference>

                // Convert user reference to user data
                val userSnapshot = userRef.get().await()
                val userId = userSnapshot.id
                val user = userSnapshot.toObject(com.example.prj02_healthy_plan.User::class.java)

                // Convert recipe references to recipe data
                val recipes = mutableListOf<RecipeFirebase>()
                for (recipeRef in recipeRefs) {
                    val recipeSnapshot = recipeRef.get().await()
                    val recipe = recipeSnapshot.toObject(RecipeFirebase::class.java)
                    if (recipe != null) {
                        recipes.add(recipe)
                    }
                }
                myRecipe.userId = userId
                myRecipe.user = user
                myRecipe.recipes = recipes
                myRecipes.add(myRecipe)
            }
            Log.d("MyRecipes", myRecipes.toString())
        } catch (e: Exception) {
            Log.e("MyRecipes", "Error fetching recipes", e)
        }
        _myRecipeList.value = myRecipes
    }

    fun deleteMyRecipe(id: String) {
        db.collection("myRecipes").document(id).delete()
            .addOnSuccessListener {
            }
            .addOnFailureListener { e ->
                Log.w("Delete", "Error deleting document", e)
            }
    }
    fun fetchIngredients() {
        db.collection("ingredients").get().addOnSuccessListener { result ->
            val ingredientList = result.documents.mapNotNull { document ->
                val ingredient = document.toObject(Ingredient::class.java)
                ingredient
            }
            _ingredientList.value = ingredientList
        }
    }


}

