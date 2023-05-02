package com.bluecactus.glisjoie.ViewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.bluecactus.glisjoie.Model.UserModel
import com.bluecactus.glisjoie.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

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

        auth.signOut()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    callback("Success")
                } else {
                    callback(context.getString(R.string.credential_invalid_error))
                }
            }
    }

    fun bindUserInfo(email: String, callback: (currUser: UserModel) -> Unit) {
        var currUser: UserModel = UserModel("", "", "", "", "", "")
        val db = Firebase.firestore
        val usersCollection = db.collection("users")

        usersCollection.whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
//                    Log.e("currUserViewModel", "docs exist with specified email")
                    val doc = querySnapshot.documents[0]
//                    Log.e("currUserViewModel", querySnapshot.documents[0].get("username") as String)
//                    currUser = doc.toObject<UserModel>()!!
//                    currUser.userDocumentID = doc.id
                    currUser = UserModel(
                        doc.id,
                        doc.get("email") as String,
                        doc.get("username") as String,
                        doc.get("status") as String,
                        doc.get("role") as String,
                        doc.get("profilePictureURL") as String
                    )
                    callback(currUser)
//                    Log.e("currUserViewModel", currUser.userDocumentID)
                } else {
//                    Log.e("bind user error", "error when binding user info using user's email")
                    // No documents exist with the email field equal to "qwerty@email.com"
                }
            }
            .addOnFailureListener { exception ->
                // Handle any errors that occurred while retrieving the document
            }

        callback(currUser)
    }


}