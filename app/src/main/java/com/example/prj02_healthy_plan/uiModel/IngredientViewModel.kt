package com.example.prj02_healthy_plan.uiModel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.prj02_healthy_plan.Ingredient
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class IngredientViewModel : ViewModel() {
    private val db = Firebase.firestore
    private val _ingredientList = MutableStateFlow<List<Ingredient>>(emptyList())
    val ingredientList: StateFlow<List<Ingredient>> = _ingredientList.asStateFlow()
    val userIngredients = mutableStateListOf<Ingredient>()
    fun fetchIngredients() {
        db.collection("ingredients").get().addOnSuccessListener { result ->
            val ingredientList = result.documents.mapNotNull { document ->
                val ingredient = document.toObject(Ingredient::class.java)
                ingredient
            }
            _ingredientList.value = ingredientList
        }
    }
    fun toggleIngredient(ingredient: Ingredient) {
        if (userIngredients.contains(ingredient)) {
            userIngredients.remove(ingredient)
        } else {
            userIngredients.add(ingredient)
        }
    }
}