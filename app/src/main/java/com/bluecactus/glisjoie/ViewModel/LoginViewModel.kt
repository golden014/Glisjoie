package com.bluecactus.glisjoie.ViewModel

import android.util.Log
import com.bluecactus.glisjoie.R
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    //login, pass string email dan password dari textfield
    //gbisa pakai return biasa pakainya callback
    fun loginValidation(email: String, password: String, callback: (result: String) -> Unit) {

        //empty validation
        if (email.isBlank() || password.isBlank()) {
            //return error message empty
            callback(R.string.credential_empty_error.toString())
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback("Success")
                } else {
                    callback("Invalid Credentials")
                }
            }

    }


}