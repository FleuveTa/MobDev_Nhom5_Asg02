package com.example.prj02_healthy_plan

import com.google.firebase.firestore.DocumentReference


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

data class MyRecipe(
    var id: String? = null,
    var userId: String? = null,
    var user: com.example.prj02_healthy_plan.User?  = null,
    var recipes: List<RecipeFirebase>? = null
)

data class MyRecipeFireBase(
    var id: String? = null,
    var user: DocumentReference? = null,
    var recipes: List<DocumentReference>? = null
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

data class RecipeInDaily(
    val recipe: DocumentReference? = null,
    val quantity: Int? = null
)

data class DailyData(
    var id: String? = null,
    val user: DocumentReference? = null,
    var water: Int? = null,
    val intake: List<Double>? = null,
    val burned: Int? = null,
    val steps: Int? = null,
    val date: String? = null,
    val breakfast: List<RecipeInDaily>? = null,
    val lunch: List<RecipeInDaily>? = null,
    val dinner: List<RecipeInDaily>? = null,
    val snacks: List<RecipeInDaily>? = null
) {

}