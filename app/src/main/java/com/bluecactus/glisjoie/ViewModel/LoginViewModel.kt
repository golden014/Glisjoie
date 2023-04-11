package com.bluecactus.glisjoie.ViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.bluecactus.glisjoie.R
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel: ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    //login, pass string email dan password dari textfield
    //gbisa pakai return biasa pakainya callback
    // ntar pakai nya:

    /*
    loginViewModel.loginValidation(email, password) { result ->
    if (result == "Success") {
        lanjut ke page lain
    } else {
        // Display an error message pakai result nya
    }
}
    */
    fun loginValidation(context: Context, email: String, password: String, callback: (result: String) -> Unit) {
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
                    callback(context.getString(R.string.credential_invalid_error))
                }
            }
    }


}