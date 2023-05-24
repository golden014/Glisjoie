package com.bluecactus.glisjoie.Repository

import android.util.Log
import com.bluecactus.glisjoie.Model.BookModel
import com.bluecactus.glisjoie.Model.CommentModel
import com.bluecactus.glisjoie.Model.UserModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.collections.ArrayList

class CommentRepository {

    val db = Firebase.firestore;

    fun createCommentRepository(comment:CommentModel, callback: (String) -> Unit){
        //var a = CommentModel("2qaX8ruzVaZyi8SLZOOS", "8jmeFAsRMDQsCDJ90gYu", Date(), 3f, "Test");
        Log.wtf("CommentRepository", "Uploading...")
        val commentDoc =
            db.collection("books")
                .document(comment.bookID!!)
                .collection("comment")
                .add(comment)
        callback("Success Comment")
    }

    fun getCommentByBookID(Book:BookModel, callback: (ArrayList<CommentModel>) -> Unit) {
        Log.d("JEVON912", "getCommentByBookID: ${Book.bookID}")
        db.collection("books")
            .document(Book.bookID!!)
            .collection("comment")
            .get()
            .addOnSuccessListener {query ->
                val commentsList = ArrayList<CommentModel>()
                for (document in query) {
                    val comment = CommentModel(document.getString("userID"),
                        document.getString("bookID"),
                        document.getDate("date"),
                        document.getDouble("rating")!!.toFloat(),
                        document.getString("description"));
                    commentsList.add(comment);
                }
                callback(commentsList)
            }
    }

    fun getCommentByUserID(UserID:String, callback: (ArrayList<CommentModel>) -> Unit) {

        var arrs = ArrayList<CommentModel>();

        db.collection("books")
            .get()
            .addOnSuccessListener { bookQuery ->
                for(books in bookQuery.documents){
                    db.collection("books").document(books.id).collection("comment").whereEqualTo("userID", UserID).get()
                        .addOnSuccessListener{ docs ->
                            for (doc in docs.documents){
                                val comment = CommentModel(doc.getString("userID"),
                                    doc.getString("bookID"),
                                    doc.getDate("date"),
                                    doc.getDouble("rating")!!.toFloat(),
                                    doc.getString("description"));
                                Log.d("COLLECTIONOFBOOKS", "getCommentByUserID: ${comment.description}")
                                arrs.add(comment);
                            }
                            callback(arrs)
                        }
                }
            }

    }

    fun getCommentByUserID(user:UserModel, callback: (ArrayList<CommentModel>) -> Unit?) {
        db.collection("books")
            .get()
            .addOnSuccessListener { bookQuery ->
                for (book in bookQuery) {
                    val commentsCollection = book.reference.collection("comment")
                    commentsCollection.whereEqualTo("userID", user.userDocumentID)
                        .get()
                        .addOnSuccessListener { commentQuery ->
                            for (comment in commentQuery) {
                                val commentsList = ArrayList<CommentModel>()
                                for (document in commentQuery) {
                                    val comment = CommentModel(document.getString("userID"),
                                        document.getString("bookID"),
                                        document.getDate("date"),
                                        document.getDouble("rating")!!.toFloat(),
                                        document.getString("description"));
                                    commentsList.add(comment);
                                }
                                callback(commentsList)
                            }
                        }
                }
            }
    }
}