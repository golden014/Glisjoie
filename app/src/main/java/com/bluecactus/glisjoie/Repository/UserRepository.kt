package com.bluecactus.glisjoie.Repository

import android.util.Log
import com.bluecactus.glisjoie.Model.UserModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserRepository {
    val db = Firebase.firestore
    val usersCollection = db.collection("users")
    val auth = Firebase.auth

    fun registerUser(newUser:  HashMap<String, String>, callback: (result: Int) -> Unit) {
        Log.e("registerRepo", newUser.get("email").toString())

        db.collection("users")
            .add(newUser)
            .addOnSuccessListener { documentReference ->
                Log.d("", "DocumentSnapshot written with ID: ${documentReference.id}")
                callback(200)
            }
            .addOnFailureListener { e ->
                Log.w("", "Error adding document", e)
                callback(500)
            }
    }

    fun registerUserAuth(email: String, password: String, callback: (result: Int) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener() {
                callback(200)
            }
            .addOnFailureListener() {
                callback(500)
            }
    }

    fun emailIsUnique(email: String, callback: (result: Boolean) -> Unit) {
        usersCollection.whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val doc = querySnapshot.documents[0]
                    callback(false)
                } else {
                    callback(true)
                }
            }
            .addOnFailureListener { exception ->
                callback(false)
                Log.e("register", exception.toString())
            }
    }

}