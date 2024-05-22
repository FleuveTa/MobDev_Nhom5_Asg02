package com.example.prj02_healthy_plan

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

interface FirebaseAuthWrapper {
    fun signOut()
}

class FirebaseAuthWrapperImpl(private val firebaseAuth: FirebaseAuth) : FirebaseAuthWrapper {
    override fun signOut() {
        firebaseAuth.signOut()
    }
}


interface ContextWrapper {
    fun startActivity(intent: Intent)
    fun finish()
}

class ContextWrapperImpl(private val context: Context) : ContextWrapper {
    override fun startActivity(intent: Intent) {
        context.startActivity(intent)
    }

    override fun finish() {
        if (context is Activity) {
            context.finish()
        }
    }
}


interface FirestoreWrapper {
    suspend fun fetchRecipes(): List<RecipeFirebase>
    suspend fun addMyRecipe(userId: String?, recipeId: String?)
    suspend fun deleteMyRecipe(userId: String?, recipeId: String)
    // Add other Firestore operations as needed
}
