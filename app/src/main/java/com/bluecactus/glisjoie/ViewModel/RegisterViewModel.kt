package com.bluecactus.glisjoie.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.bluecactus.glisjoie.Repository.UserRepository
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterViewModel: ViewModel() {

    val userRepo = UserRepository()

    fun registerUser(
        email: String,
        username: String,
        profilePictureURL: String,
        password: String,
        confirmPassword: String,
        callback: (result: Int) -> Unit
        ){
        Log.e("registerViewModel", "masuk regisViewModel")
        val newUser = hashMapOf(
            "email" to email,
            "username" to username,
            "status" to "Active",
            "role" to "Customer",
            "profilePictureURL" to "dummy.com"
        )

        if (email.equals("") || username.equals("") || profilePictureURL.equals("") || password.equals("") || confirmPassword.equals("")) {
            callback(410)
            return
        } else if (!usernameValid(username)) {
            callback(420)
            return
        } else if (!password.equals(confirmPassword)) {
            callback(430)
            return
        }
        userRepo.emailIsUnique(email) { result ->
            if (!result) {
                callback(440);
                Log.e("registerViewModel", "email not unique")
            }
        }

        Log.e("registerViewModel", email)
        Log.e("registerViewModel", "validation passed")
        userRepo.registerUser(newUser) { result ->
            if (result == 200) {
                 userRepo.registerUserAuth(email, password) { result ->
                     callback(result)
                 }
            }
            callback(result)
        };



    }

    //space and special char validation
    fun usernameValid(username: String): Boolean {
        val pattern = "^[a-zA-Z0-9]+$".toRegex()
        return pattern.matches(username)
    }
}