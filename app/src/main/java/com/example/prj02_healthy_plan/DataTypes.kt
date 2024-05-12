package com.example.prj02_healthy_plan

data class Ingredient(
    val name: String? = null,
    val unit: String? = null,
    val nutrition: List<Double>? = null
)

data class IngredientInRecipe(
    val name: String? = null,
    val unit: String? = null,
    val quantity: Double? = null
)

data class RecipeFirebase(
    var id: String? = null,
    val name: String? = null,
    val description: String? = null,
    val imageUrl: String? = null,
    val instructionUrl: String? = null,
    val ingredients: List<IngredientInRecipe>? = null,
    val nutrition: List<Double>? = null
)