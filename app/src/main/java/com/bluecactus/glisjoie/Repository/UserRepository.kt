package com.bluecactus.glisjoie.Repository

import android.util.Log
import com.bluecactus.glisjoie.Model.UserModel
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.auth.User
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
//            .addOnCompleteListener() {
//                callback(200)
//            }
            .addOnFailureListener() {
                callback(500)
            }
            .addOnSuccessListener(){
                callback(200)
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

    fun getCurrUser (callback: (result: UserModel) -> Unit){
        val email = auth.currentUser?.email

        usersCollection.whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { it ->
                if (!it.isEmpty) {
                    val doc = it.documents[0]
                    callback(UserModel(
                        doc.id,
                        doc.getString("email") as String,
                        doc.getString("username") as String,
                        doc.getString("status") as String,
                        doc.getString("role") as String,
                        doc.getString("profilePictureURL") as String
                        ))
                }

            }
    }

    //get all customer
    fun getAllCustomer(callback: (Array<UserModel>) -> Unit) {
        var users = mutableListOf<UserModel>()

        usersCollection
            .whereEqualTo("role", "Customer")
            .get()
            .addOnSuccessListener { querySnapshot->
                for (doc in querySnapshot.documents) {
                    users.add(UserModel(
                        doc.id,
                        doc.getString("email") as String,
                        doc.getString("username") as String,
                        doc.getString("status") as String,
                        doc.getString("role") as String,
                        doc.getString("profilePictureURL") as String
                    ))
                    Log.e("admin", doc.getString("profilePictureURL") as String)

                    callback(users.toTypedArray())
                }
            }
    }

    //search customer
    fun searchCustomer(name: String, callback: (Array<UserModel>) -> Unit) {
        var filtered = mutableListOf<UserModel>()
        getAllCustomer() { users ->
            filtered = users.filter { it.username.contains(name) } as MutableList<UserModel>
            callback(filtered.toTypedArray())
//            for (i in users.indices) {
//                if (users[i].username.contains(name)) {
//                    filtered.add(users[i])
//
//                }
//
//                if (i == (users.size) - 1) {
//                    callback(filtered.toTypedArray())
////                    Log.e("filtered", filtered[filtered.size].username)
//                }
//            }

        }


    }

    fun getUserByID(userID: String, callback: (UserModel?) -> Unit) {
        db.collection("users").document(userID).get().addOnSuccessListener { user ->
            val curUser = if (user != null && user.exists()) {
                user.getString("email")?.let {
                    UserModel(
                        user.id,
                        it,
                        user.getString("username")!!,
                        user.getString("status")!!,
                        user.getString("role")!!,
                        user.getString("profilePictureURL")!!
                    )
                }
            } else {
                null
            }
            callback(curUser)
        }
    }

    //filter by status
    fun filterStatusUser(status: String, callback: (Array<UserModel>) -> Unit) {
        var users = mutableListOf<UserModel>()

        usersCollection
            .whereEqualTo("role", "Customer")
            .whereEqualTo("status", status)
            .get()
            .addOnSuccessListener { querySnapshot->
                for (doc in querySnapshot.documents) {
                    users.add(UserModel(
                        doc.id,
                        doc.getString("email") as String,
                        doc.getString("username") as String,
                        doc.getString("status") as String,
                        doc.getString("role") as String,
                        doc.getString("profilePictureURL") as String
                    ))
                    Log.e("admin", doc.getString("profilePictureURL") as String)

                    callback(users.toTypedArray())
                }
            }
    }

    //ban and unban user
    fun actionBanUser(userID: String, newStatus: String, callback: (Int) -> Unit) {
        usersCollection
            .document(userID)
            .update("status", newStatus)
            .addOnSuccessListener {
                callback(200)
            }
            .addOnFailureListener{
                callback(500)
            }
    }

    //update email
    fun updateEmail(newEmail: String, callback: (Int) -> Unit) {
        //cek email unique atau engga
        emailIsUnique(newEmail) { unique ->
            if (unique) {
                val user = FirebaseAuth.getInstance().currentUser
                val oldEmail = user?.email
                user?.updateEmail(newEmail)
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            usersCollection.whereEqualTo("email", oldEmail).get().addOnSuccessListener { userDoc ->
                                usersCollection.document(userDoc.documents[0].id).update("email", newEmail)
                                    .addOnSuccessListener {
                                        callback(200)
                                    }
                                    .addOnFailureListener{
                                        callback(500)
                                    }

                            }

                        } else {
                            callback(500)
                        }
                    }
                    ?.addOnFailureListener{
                        Log.e("updateEmail", "server error")
                        Log.e("UpdateEmail", it.message.toString())
                        callback(500)
                    }
            } else {
                callback(400)
            }
        }


    }

    //update password
    fun updatePassword(currPass: String,newPassword: String, callback: (Int) -> Unit) {
        val user = FirebaseAuth.getInstance().currentUser

        // Create credentials using the user's email and current password
        val credentials = EmailAuthProvider.getCredential(user?.email ?: "", currPass)

        user?.reauthenticate(credentials)?.addOnCompleteListener { reauthTask ->
            if (reauthTask.isSuccessful) {
                user.updatePassword(newPassword)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            callback(200)
                        } else {
                            callback(500)
                        }
                    }
                    .addOnFailureListener{
                        Log.e("updateEmail", "server error")
                        Log.e("UpdateEmail", it.message.toString())
                    }
            }
        }


    }


}