package com.bluecactus.glisjoie.Repository

import android.util.Log
import com.bluecactus.glisjoie.Model.ViewHistoryModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HistoryRepository {
    val db = Firebase.firestore
    val usersCollection = db.collection("users")
    val auth = Firebase.auth

    fun updateViewHistory(userID: String, bookID: String) {
        val viewHistoryRef =
            db.collection("users")
                .document(userID)
                .collection("viewHistory")
        val query = viewHistoryRef.whereEqualTo("bookID", bookID)

        query.get().addOnSuccessListener { querySnapshot ->
            if (querySnapshot.isEmpty) {
                // belum pernah liat buku ini, jadi buat document baru dengan
                // bookID yg dikasih
                val viewHistory = hashMapOf(
                    "bookID" to bookID,
                    "date" to FieldValue.serverTimestamp(), //use server time stamp
                    "isDeleted" to "false"
                )
                viewHistoryRef
                    .add(viewHistory)
                    .addOnSuccessListener { documentReference ->
                        Log.d("viewHistory", "DocumentSnapshot added with ID: ${documentReference.id}")
                    }
                    .addOnFailureListener { e ->
                        Log.w("viewHistory", "Error adding document", e)
                    }

            } else {
                // sudah pernah liat buku ini, jadi update date nya jadi date
                // sekarang
                val docID = querySnapshot.documents[0].id
                viewHistoryRef
                    .document(docID)
                    .update("date", FieldValue.serverTimestamp()) // use server timestamp
                    .addOnSuccessListener {
                        Log.d("viewHistoryUpdate", "DocumentSnapshot successfully updated!")
                    }
                    .addOnFailureListener { e ->
                        Log.w("viewHistoryUpdate", "Error updating document", e)
                    }
            }
        }
    }

    fun getViewHistory(userID: String, callback: (Array<ViewHistoryModel>) -> Unit) {
        //TODO: lengkapin
    }
}