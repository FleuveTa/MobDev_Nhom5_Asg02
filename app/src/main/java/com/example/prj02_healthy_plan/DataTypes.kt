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

data class User(
    val fullName: String? = null,
    val height: Int? = null,
    val gender: Int? = null,
    val dob: String? = null,
    val activityLevel: Int? = null,
    val weeklyGoal: String? = null,
    val caloriesGoal: Int? = null,
    val nutrientGoal: Int? = null,
    val weight: Int? = null,
    val targetWeight: Int? = null,
    val goal: Int? = null
)