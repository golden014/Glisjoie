package com.bluecactus.glisjoie.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.bluecactus.glisjoie.Repository.UserRepository
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterViewModel: ViewModel() {

    val db = Firebase.firestore
    val userRepo = UserRepository()

    fun registerUser(
        email: String,
        username: String,
        profilePictureURL: String,
        callback: (result: Int) -> Unit
        ){

        val newUser = hashMapOf(
            "email" to email,
            "username" to username,
            "status" to "Active",
            "role" to "Customer",
            "profilePictureURL" to profilePictureURL
        )

        if (email.equals("") || username.equals("") || profilePictureURL.equals("")) {

        }


        userRepo.registerUser(newUser) { result ->
            callback(result)
        };

    }
}