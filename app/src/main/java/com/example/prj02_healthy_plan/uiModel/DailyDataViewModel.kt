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
                    for (document in documents) {
                        val dailyData = document.toObject(DailyData::class.java)
                        dailyData.id = document.id
                        _dailyData.value = dailyData
                    }
                    Log.d("DATA AT : $date ", "${_dailyData.value}")
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
}