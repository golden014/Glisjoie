package com.bluecactus.glisjoie.Repository

import android.util.Log
import com.bluecactus.glisjoie.Model.BookModel
import com.bluecactus.glisjoie.Model.BookPreviewModel
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
        val books = mutableListOf<BookPreviewModel>()
        db.collection("books")
            .orderBy("rating", Query.Direction.DESCENDING)
            .limit(20)
            .get()
            .addOnCompleteListener() { booksResult ->
                // Iterate over the books and retrieve the author's name from the "users" collection
//                bookTasks =
                for (bookDoc in booksResult.result.documents) {
                    val userID = bookDoc.getString("userID")
                    if (userID != null) {
                            db.collection("users").document(userID).get()
                                .addOnCompleteListener() { user ->
                                                    user.result.getString("username")?.let {
                                                        bookDoc.getString("bookTitle")?.let { it1->
                                                            bookDoc.getString("imageLink")?.let { it2 ->
                                                                bookDoc.get("rating")?.let { it3 ->
                                                                    bookDoc.getString("userID")?.let { it4 ->
                                                                        BookPreviewModel(
                                                                            it1,
                                                                            it,
                                                                            it2,
                                                                            bookDoc.id,
                                                                            (it3 as Number).toDouble(),
                                                                            it4
                                                                        )
                                                                    }

                                                                }

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
                                    callback(books.toTypedArray())
                                }
                    }
                }



            }

    }


    override fun performSearch(keyword: String, callback: (Array<BookPreviewModel>) -> Unit) {
        Log.e("searchdebug", "performsearch start, keyword: $keyword")
        val books = mutableListOf<BookPreviewModel>()


        db.collection("books").whereGreaterThanOrEqualTo("bookTitle", keyword)
            .whereLessThan("bookTitle", keyword + "z")
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    Log.e("emptyDebug", "no doc found from given keyword")
                    callback(emptyArray())
                } else {
                    Log.e("searchdebug", "success")
                    for (document in documents.documents) {
                        val title = document.getString("bookTitle")
                        Log.e("searchdebug title::", title.toString())
                        val bookID = document.id
                        val cover = document.getString("imageLink")
                        val rating = document.get("rating")
                        var authorName = ""

                        Log.e("searchdebug", title.toString())
                        document.getString("userID")?.let {
                            db.collection("users").document(it)
                                .get()
                                .addOnSuccessListener { userDoc ->
                                    authorName = userDoc.getString("username").toString()
                                    Log.e("searchdebug", userDoc.getString("username").toString())

                                    books.add(BookPreviewModel(title.toString(), authorName, cover.toString(), bookID,
                                        (rating as Number).toDouble(), userDoc.id
                                    ))


                                    Log.e("searchdebug lol", books.toString())
                                    callback(books.toTypedArray())
                                }
                        }
                    }
                }



            }
            .addOnFailureListener {
                Log.e("searchdebug",  "fail")
            }
            .addOnCompleteListener {
                Log.e("searchdebug", "complete start")
            }
            .addOnCanceledListener {
                Log.e("searchdebug", "cancelled start")
            }
    }

    //Add book
    override fun getBookByID(bookID: String?, callback: (BookModel?) -> Unit) {
        if(bookID == null){
            Log.e("BookRepository", "getBookByID: bookID is null")
            return
        }
        db!!.collection("books").document(bookID).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val book = task.result
                if (book != null && book.exists()) {
                    val bookTitle = book.getString("bookTitle").toString()
                    val imageLink = book.getString("imageLink").toString()
                    val rating = book.getDouble("rating")?.toFloat()!!
                    val description = book.getString("description").toString()
                    val date = book.getDate("date")
                    val userID = book.getString("userID")!!

                    // Retrieve author from "users" collection
                    db!!.collection("users").document(userID).get().addOnCompleteListener { userTask ->
                        if (userTask.isSuccessful) {
                            val user = userTask.result
                            val author = user?.getString("username")?.toString() ?: "n/a"

                            val bookModel = BookModel("", bookTitle, bookID, userID, description, imageLink, db, null, date, rating, author)
                            Log.d("BookRepository", "received book model of: ${bookModel.bookTitle}")
                            callback(bookModel)
                        } else {
                            println("Error getting user details: ${userTask.exception}")
                            callback(null)
                        }
                    }
                } else {
                    println("Book not found.")
                    callback(null)
                }
            } else {
                println("Error getting book details: ${task.exception}")
                callback(null)
            }
        }
    }
}