package com.bluecactus.glisjoie.Repository

import android.util.Log
import com.bluecactus.glisjoie.Model.BookPreviewModel
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

//import com.google.android.gms.tasks.Task
//import com.google.android.gms.tasks.Tasks
//import com.google.firebase.firestore.DocumentSnapshot
//import com.google.firebase.firestore.FirebaseFirestore
//import com.google.firebase.firestore.Query
//import com.google.firebase.firestore.ktx.firestore
//import com.google.firebase.ktx.Firebase

class BookRepository: com.bluecactus.glisjoie.Model.BookRepository {
    val db = Firebase.firestore
    val booksCollection = db.collection("books")

    override fun getTopBooks(callback: (Array<BookPreviewModel>) -> Unit) {
//        Log.e("debug book repo", "masuk")
//        val db = FirebaseFirestore.getInstance()
//        val books = mutableListOf<BookPreviewModel>()
//        val bookTasks:MutableList<Task<DocumentSnapshot>> = mutableListOf<Task<DocumentSnapshot>>()
//
//        // Query the "books" collection and sort by rating in descending order
//        db.collection("books")
//            .orderBy("rating", Query.Direction.DESCENDING)
//            .limit(20)
//            .get()
//            .addOnSuccessListener { booksResult ->
//                // Iterate over the books and retrieve the author's name from the "users" collection
////                bookTasks =
//                for (bookDoc in booksResult.documents) {
//                    Log.e("debug book repo: bookID's", bookDoc.id)
//                    val userID = bookDoc.get("userID")
//                    if (userID != null) {
//                        val authorTask = db.collection("users").document(userID as String).get()
//                        Log.e("debug book repo", "authorID's:" + userID)
//                        Log.e("debug book repo", authorTask.result.toString())
//
//                        bookTasks.add(authorTask)
//                    }
//                }
//
//                // Wait for all author retrieval tasks to complete
//                Tasks.whenAllComplete(bookTasks).addOnSuccessListener { authorResults ->
//                    // Iterate over the book and author results and create an array of Book objects
//                    for ((i, bookDoc) in booksResult.documents.withIndex()) {
//                        Log.e("debug book repo", i.toString() + " " + bookDoc)
//                        val authorResult = bookTasks[i]
////                        Log.e("debug book repo", authorResult.toString())
//                        if (authorResult.isSuccessful) {
//                            Log.e("debug book repo", "masul line 56")
//                            val authorDoc: DocumentSnapshot = authorResult.result as DocumentSnapshot
//                            Log.e("debug book repo 58", "masul line 57")
//                            Log.e("debug book repo 59", authorDoc.toString())
//                            if (authorDoc != null) {
//                                val title = bookDoc.getString("bookTitle")
//                                val authorName = authorDoc.data?.get("username")
//                                Log.e("debug book repo 63: title", title + " " +authorName)
//                                val book = title?.let {
//                                    if (authorName != null) {
////                                        BookPreviewModel(title, authorName)
//                                        Log.e("debug book repo 66: title", title + " " +authorName)
//                                    }
//                                }
//                                if (book != null) {
//                                    Log.e("debug book repo 70", "null")
//                                    books.add(book)
//                                }
//                            } else  {
//                                Log.e("debug book repo 74", "author doc null")
//                            }
//                        } else {
//                            Log.e("debug book repo 76", "authorresult fail")
//                        }
//                    }
//                    callback(books.toTypedArray())
//                }
//            }
//            .addOnFailureListener { e ->
//                Log.e("TAG", "Error getting books", e)
//            }

        // Wait for all book retrieval tasks to complete before returning the result
//        Tasks.await(bookTasks)
//        Tasks.await(Tasks.whenAllComplete(bookTasks))
//        return books.toTypedArray()

        val books = mutableListOf<BookPreviewModel>()
        db.collection("books")
            .orderBy("rating", Query.Direction.DESCENDING)
            .limit(20)
            .get()
            .addOnCompleteListener() { booksResult ->
                // Iterate over the books and retrieve the author's name from the "users" collection
//                bookTasks =
                for (bookDoc in booksResult.result.documents) {
                    Log.e("debug book repo: bookID's", bookDoc.id)
                    val userID = bookDoc.getString("userID")
                    Log.e("debug book repo: userID's",userID.toString())
//                    val temp = userID?.substring(1)
//                    Log.e("debug book repo: userID's",temp.toString())
                    if (userID != null) {
                        Log.e("user ID ::zz:: ", userID as String)
//                        if (temp != null) {
                            db.collection("users").document(userID).get()
                                .addOnCompleteListener() { user ->
                                    Log.e("aaa1", userID)
                                    user.result.getString("username")?.let { Log.e("aaa2", it) }
                                                    user.result.getString("username")?.let {
                                                        bookDoc.getString("bookTitle")?.let { it1->
                                                            bookDoc.getString("imageLink")?.let { it2 ->
                                                                BookPreviewModel(
                                                                    it1,
                                                                    it,
                                                                    it2
                                                                )
                                                            }

                                                        }
                                                    }?.let {
                                                        Log.e("added",
                                                            user.result.getString("username")!!
                                                        )
                                                        books.add(
                                                            it
                                                        )
                                                    }

                                    if (user != null && user.result.exists()) {
                                        Log.e("zzz", "DocumentSnapshot data: ${user.result.data}")
                                    } else {
                                        Log.e("zzz", "No such document")
                                    }

                                    if (books.size > 0) {
                                        Log.e("book repo", books[0].title)

                                    } else {
                                        Log.e("book repo", "books 0")
                                    }
                                    callback(books.toTypedArray())
                                }
//                        }
//                        Log.e("debug book repo", "authorID's:" + userID)
//                        Log.e("debug book repo", authorTask.result.toString())
//
//                        bookTasks.add(authorTask)
                    }
                }



            }

    }

    private fun <E> MutableList<E>.add(element: Unit) {

    }

    override fun performSearch(keyword: String, callback: (Array<BookPreviewModel>) -> Unit) {
        val books = mutableListOf<BookPreviewModel>()

        db.collection("books")
            .whereEqualTo("bookTitile", keyword)

    }

}