package com.example.prj02_healthy_plan.uiModel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prj02_healthy_plan.Ingredient
import com.example.prj02_healthy_plan.MyRecipe
import com.example.prj02_healthy_plan.RecipeFirebase
import com.example.prj02_healthy_plan.User
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class RecipeViewModel : ViewModel() {
    private val db = Firebase.firestore
    private val _recipeList = MutableStateFlow<List<RecipeFirebase>>(emptyList())
    val recipeList: StateFlow<List<RecipeFirebase>> = _recipeList.asStateFlow()
    val myRecipeList = MutableStateFlow<List<MyRecipe>>(emptyList())
    val selectedRecipeName: MutableStateFlow<String> = MutableStateFlow("")

    init {
        fetchMyRecipes()
    }

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


    private fun fetchMyRecipes() {
        viewModelScope.launch {
            val myRecipes = fetchRecipesFromDB()
            myRecipeList.value = myRecipes
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
                val user = userSnapshot.toObject(User::class.java)

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

    private fun addMyRecipe(userId: String?, recipeId: String?) {
        val userRef = db.collection("users").document(userId ?: "")
        val recipeRef = db.collection("recipes").document(recipeId ?: "")
        val newMyRecipe = hashMapOf(
            "recipes" to arrayListOf(recipeRef),
            "user" to userRef
        )

        val currentRecipes = myRecipeList.value.toMutableList()
        currentRecipes.add(MyRecipe(user = null, userId = userId, recipes = listOf(RecipeFirebase(id = recipeId))))
        myRecipeList.value = currentRecipes

        db.collection("myRecipes").whereEqualTo("user", userRef).get().addOnSuccessListener { result ->
            if (result.isEmpty) {
                db.collection("myRecipes").add(newMyRecipe).addOnSuccessListener {
                    fetchMyRecipes()
                }
            } else {
                val myRecipeId = result.documents[0].id
                db.collection("myRecipes").document(myRecipeId).update("recipes", FieldValue.arrayUnion(recipeRef))
            }
        }
    }

    private fun deleteMyRecipe(userId: String?, recipeId: String) {
        val userRef = db.collection("users").document(userId ?: "")
        val currentRecipes = myRecipeList.value.toMutableList()
        currentRecipes.removeAll { it.recipes?.any { recipe -> recipe.id == recipeId } == true }
        myRecipeList.value = currentRecipes

        db.collection("myRecipes").whereEqualTo("user", userRef).get().addOnSuccessListener { result ->
            if (result.isEmpty) {
                Log.d("Delete", "No myRecipes found")
            } else {
                val myRecipeId = result.documents[0].id
                db.collection("myRecipes").document(myRecipeId)
                    .update("recipes", FieldValue.arrayRemove(db.collection("recipes").document(recipeId)))
                    .addOnSuccessListener {
                        fetchMyRecipes()
                    }
            }
        }
    }

    fun toggleMyRecipe(userId: String?, recipeId: String) {
        val userRef = db.collection("users").document(userId ?: "")
        db.collection("myRecipes").whereEqualTo("user", userRef).get().addOnSuccessListener { result ->
            if (result.isEmpty) {
                addMyRecipe(userId, recipeId)
            } else {
                val myRecipeId = result.documents[0].id
                val myRecipe = myRecipeList.value.find { it.id == myRecipeId }
                if (myRecipe != null) {
                    if (myRecipe.recipes?.any { it.id == recipeId } == true) {
                        deleteMyRecipe(userId, recipeId)
                    } else {
                        db.collection("myRecipes").document(myRecipeId)
                            .update("recipes", FieldValue.arrayUnion(db.collection("recipes").document(recipeId)))
                            .addOnSuccessListener {
                                fetchMyRecipes()
                            }
                    }
                }
            }
        }
    }
}

