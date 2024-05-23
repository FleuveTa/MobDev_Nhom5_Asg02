package com.example.prj02_healthy_plan

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.tasks.await

interface FirebaseAuthWrapper {
    fun signOut()
    fun getAuth() : FirebaseAuth
    fun getEmail() : String
    fun getUID() : String

}

class FirebaseAuthWrapperImpl(private val firebaseAuth: FirebaseAuth) : FirebaseAuthWrapper {
    override fun signOut() {
        firebaseAuth.signOut()
    }

    override fun getAuth(): FirebaseAuth {
        return firebaseAuth
    }

    override fun getEmail(): String {
        return firebaseAuth.currentUser?.email.toString()
    }

    override fun getUID(): String {
        return firebaseAuth.currentUser?.uid.toString()
    }
}


interface ContextWrapper {
    fun startActivity(intent: Intent)
    fun finish()
    abstract fun requireActivity(): Any
    abstract fun getContext(): Context
}

class ContextWrapperImpl(private val context: Context) : ContextWrapper {
    override fun startActivity(intent: Intent) {
        context.startActivity(intent)
    }

    override fun finish() {
        if (context is Activity) {
            context.finish()
        } else {
            Log.e("ContextWrapperImpl", "Context is not an Activity")
        }
    }

    override fun requireActivity(): Activity {
        return context as Activity
    }

    override fun getContext(): Context {
        return context
    }
}


interface FirestoreWrapper {
    suspend fun fetchRecipes(): List<RecipeFirebase>
    suspend fun addMyRecipe(userId: String?, recipeId: String?)
    suspend fun deleteMyRecipe(userId: String?, recipeId: String)
    // Add other Firestore operations as needed
}


class FirestoreCollectionReferenceWrapper(private val collectionReference: CollectionReference) {
    fun add(data: Any): Task<DocumentReference> {
        return collectionReference.add(data)
    }

    fun get(): Task<QuerySnapshot> {
        return collectionReference.get()
    }

    fun document(documentPath: String): DocumentReference {
        return collectionReference.document(documentPath)
    }
}

class StorageReferenceWrapper(private val storageReference: StorageReference) {
    fun child(path: String): StorageReference {
        return storageReference.child(path)
    }

    fun putFile(uri: Uri): UploadTask {
        return storageReference.putFile(uri)
    }

    fun downloadUrl(): Task<Uri> {
        return storageReference.downloadUrl
    }
}

