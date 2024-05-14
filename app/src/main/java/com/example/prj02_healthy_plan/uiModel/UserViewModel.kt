package com.example.prj02_healthy_plan.uiModel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prj02_healthy_plan.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class UserViewModel : ViewModel() {
    val auth: FirebaseAuth = Firebase.auth
    val state = mutableStateOf(User())
    val uId = auth.currentUser?.uid

    init {
        getUserInfor()
    }

    private fun getUserInfor() {
        viewModelScope.launch {
            if (uId != null) {
                state.value = fetchUserInfor(uId = uId)
            }
        }
    }
}

suspend fun fetchUserInfor(uId: String): User {
    val db = Firebase.firestore
    var user = User()

    try {
        val document = db.collection("users").document(uId).get().await()
        if (document.exists()) {
            user = document.toObject<User>()!!
        }
    } catch (e: FirebaseFirestoreException) {
        Log.d("error", "getDataFromFireStore: $e")
    }

    return user
}