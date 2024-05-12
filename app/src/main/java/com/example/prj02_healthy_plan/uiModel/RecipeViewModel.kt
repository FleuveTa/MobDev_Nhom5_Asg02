package com.example.prj02_healthy_plan.uiModel

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

    suspend fun fetchRecipes() {
        db.collection("recipes").get().addOnSuccessListener { result ->
            val recipeList = result.documents.mapNotNull { it.toObject(RecipeFirebase::class.java) }
            _recipeList.value = recipeList
        }
    }
}