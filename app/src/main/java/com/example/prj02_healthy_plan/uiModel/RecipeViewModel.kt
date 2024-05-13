package com.example.prj02_healthy_plan.uiModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.prj02_healthy_plan.RecipeFirebase
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class RecipeViewModel : ViewModel() {
    private val db = Firebase.firestore
    private val _recipeList = MutableStateFlow<List<RecipeFirebase>>(emptyList())
    val recipeList: StateFlow<List<RecipeFirebase>> = _recipeList.asStateFlow()

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
}