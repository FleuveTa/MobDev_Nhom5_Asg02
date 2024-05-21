package com.example.prj02_healthy_plan.uiModel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prj02_healthy_plan.DailyData
import com.example.prj02_healthy_plan.RecipeFirebase
import com.example.prj02_healthy_plan.RecipeInDaily
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DailyDataViewModel : ViewModel() {
    private val db = Firebase.firestore
    private val _dailyData = MutableStateFlow<DailyData>(DailyData())
    val dailyData: StateFlow<DailyData> = _dailyData.asStateFlow()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid
    private val userRef = db.collection("users").document(userId?:"")
    fun fetchDailyData(date: String) {

        viewModelScope.launch {
            val dailyDataRef = db.collection("dailyData")
            val query = dailyDataRef
                .whereEqualTo("user", userRef)
                .whereEqualTo("date", date)

            query.get()
                .addOnSuccessListener { documents ->
                    if (documents.isEmpty) {
                        createNewDailyData(date)
                        Log.d("DATA AT : $date ", "No data")
                    } else {
                        for (document in documents) {
                            val dailyData = document.toObject(DailyData::class.java)
                            dailyData.id = document.id
                            _dailyData.value = dailyData
                        }
                        Log.d("DATA AT : $date ", "${_dailyData.value}")
                    }
                }
                .addOnFailureListener { e ->
                    Log.w("Error", "Error getting documents: ", e)
                }
        }
    }

    fun addWater() {
        val newDailyData = _dailyData.value.copy()
        newDailyData.water = newDailyData.water?.plus(1)

        _dailyData.value = newDailyData

        viewModelScope.launch {
            db.collection("dailyData")
                .document(_dailyData.value.id?:"")
                .update("water", _dailyData.value.water)
        }
    }

    fun updateMeal(mealType: String, date: String, recipe: RecipeInDaily, context: Context) {
        //val recipeRef = db.collection("recipes").document("recipeId")
        viewModelScope.launch {
            val dailyDataRef = db.collection("dailyData")
            val query = dailyDataRef
                .whereEqualTo("user", userRef)
                .whereEqualTo("date", date)
            query.get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        document.reference.update(mealType, FieldValue.arrayUnion(recipe))
                    }
                    Toast.makeText(context, "Added to $mealType", Toast.LENGTH_SHORT).show()
                }
        }
    }

    fun updateTotalIntake(breakfast: Double, lunch: Double, dinner: Double, snacks: Double, date: String) {
        val newDailyData = _dailyData.value.copy()
        newDailyData.intake = listOf(breakfast, lunch, dinner, snacks)

        _dailyData.value = newDailyData

        viewModelScope.launch {
//            db.collection("dailyData")
//                .document(_dailyData.value.id?:"")
//                .update("intake", _dailyData.value.intake)
            val dailyDataRef = db.collection("dailyData")
            val query = dailyDataRef
                .whereEqualTo("user", userRef)
                .whereEqualTo("date", date)
            query.get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        document.reference.update("intake", newDailyData.intake)
                    }
                }
        }

    }

    fun createNewDailyData(date: String) {
        val newDailyData = DailyData(
            user = userRef,
            water = 0,
            intake = listOf(0.0, 0.0, 0.0, 0.0),
            burned = 0,
            steps = 0,
            date = date,
            breakfast = listOf(),
            lunch = listOf(),
            dinner = listOf(),
            snacks = listOf()
        )

        // Chuyển đổi DailyData thành HashMap
        val dailyDataMap = hashMapOf(
            "user" to newDailyData.user,
            "water" to newDailyData.water,
            "intake" to newDailyData.intake,
            "burned" to newDailyData.burned,
            "steps" to newDailyData.steps,
            "date" to newDailyData.date,
            "breakfast" to newDailyData.breakfast,
            "lunch" to newDailyData.lunch,
            "dinner" to newDailyData.dinner,
            "snacks" to newDailyData.snacks
        )

        val dailyDataRef = db.collection("dailyData")
        dailyDataRef.add(dailyDataMap)
            .addOnSuccessListener { documentReference ->
                newDailyData.id = documentReference.id
                _dailyData.value = newDailyData
                Log.d("CREATED NEW AT $date ", "${_dailyData.value}")
            }
    }
}