package edu.bluejack22_2.Glisjoie.Repository

import android.util.Log
import edu.bluejack22_2.Glisjoie.Model.CommentModel
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

    fun getCommentByBookID(Book: edu.bluejack22_2.Glisjoie.Model.BookModel, callback: (ArrayList<CommentModel>) -> Unit) {
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
                                if(doc.getString("userID").equals(UserID)){
                                    val comment = CommentModel(doc.getString("userID"),
                                        doc.getString("bookID"),
                                        doc.getDate("date"),
                                        doc.getDouble("rating")!!.toFloat(),
                                        doc.getString("description"));
                                    Log.d("COLLECTIONOFBOOKS", "getCommentByUserID: ${comment.description}")
                                    arrs.add(comment);
                                }
                            }
                            callback(arrs)
                        }
                }
            }

    }
}