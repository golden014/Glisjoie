package com.bluecactus.glisjoie.Repository

import android.util.Log
import com.bluecactus.glisjoie.Model.CommentModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CommentRepository {

    val db = Firebase.firestore;

    fun createCommentRepository(comment:CommentModel){
        Log.wtf("BookModel", "Sending upload request...")
        val commentDoc =
            db.collection("bookID")
                .document(comment.bookID!!)
                .collection("comment")
                .add(comment)
    }
}