package com.example.prj02_healthy_plan.uiModel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prj02_healthy_plan.Ingredient
import com.example.prj02_healthy_plan.MyRecipe
import com.example.prj02_healthy_plan.RecipeFirebase
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class RecipeViewModel : ViewModel() {
    private val db = Firebase.firestore
    private val _recipeList = MutableStateFlow<List<RecipeFirebase>>(emptyList())
    private val _myRecipeList = MutableStateFlow<List<MyRecipe>>(emptyList())
    val recipeList: StateFlow<List<RecipeFirebase>> = _recipeList.asStateFlow()
    val myRecipeList: StateFlow<List<MyRecipe>> = _myRecipeList.asStateFlow()
    val selectedRecipeName: MutableStateFlow<String> = MutableStateFlow("")


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

    fun fetchMyRecipes() {
        viewModelScope.launch {
            val myRecipes = fetchRecipesFromDB()
            _myRecipeList.value = myRecipes
        }
    }

    private suspend fun fetchRecipesFromDB(): List<MyRecipe> {
        val myRecipes = mutableListOf<MyRecipe>()
        try {
            val myRecipesSnapshot = db.collection("myRecipes").get().await()
            for (doc in myRecipesSnapshot.documents) {
                val myRecipe = MyRecipe(id = doc.id)

                val userRef = doc.get("user") as DocumentReference
                val recipeRefs = doc.get("recipes") as List<DocumentReference>

                val userSnapshot = userRef.get().await()
                val userId = userSnapshot.id
                val user = userSnapshot.toObject(com.example.prj02_healthy_plan.User::class.java)

                val recipes = mutableListOf<RecipeFirebase>()
                for (recipeRef in recipeRefs) {
                    val recipeSnapshot = recipeRef.get().await()
                    val recipe = recipeSnapshot.toObject(RecipeFirebase::class.java)
                    if (recipe != null) {
                        recipe.id = recipeSnapshot.id
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
        return myRecipes
    }

    fun addMyRecipe(userId: String?, recipeId: String?) {
        val userRef = db.collection("users").document(userId?:"")
        val recipeRef = db.collection("recipes").document(recipeId?:"")
        val newMyRecipe =  hashMapOf(
            "recipes" to arrayListOf(recipeRef),
            "user" to userRef
        )
        _myRecipeList.value = _myRecipeList.value + MyRecipe(user = null, userId = userId, recipes = listOf(RecipeFirebase(id = recipeId)))
        // find the user's myRecipes by User ID
        db.collection("myRecipes").whereEqualTo("user", userRef).get().addOnSuccessListener { result ->
            if (result.isEmpty) {
                // if the user doesn't have any myRecipes, create a new one
                db.collection("myRecipes").add(newMyRecipe).addOnSuccessListener {
                    fetchMyRecipes()  // Fetch the updated list
                }
            } else {
                // if the user already has myRecipes, add the new recipeRef into the recipes array
                val myRecipeId = result.documents[0].id
                db.collection("myRecipes").document(myRecipeId).update("recipes", FieldValue.arrayUnion(recipeRef))
            }
        }
    }
    fun deleteMyRecipe(userId: String?, id: String) {
        val userRef = db.collection("users").document(userId?:"")
        _myRecipeList.value = _myRecipeList.value.filter { it.id != id }
        db.collection("myRecipes").whereEqualTo("user", userRef).get().addOnSuccessListener { result ->
            if (result.isEmpty) {
                Log.d("Delete", "No myRecipes found")
            } else {
                val myRecipeId = result.documents[0].id
                // delete the recipeRef from the recipes array
                db.collection("myRecipes").document(myRecipeId)
                    .update("recipes", FieldValue.arrayRemove(db.collection("recipes").document(id)))
                    .addOnSuccessListener {
                        fetchMyRecipes()  // Fetch the updated list
                    }
            }
        }
    }
}

