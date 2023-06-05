package com.bluecactus.glisjoie.Model

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.util.*

class BookModel(
    var message: String?,
    var bookTitle: String,
    var bookID: String?,
    var userID: String?,
    var description: String,
    var imageLink: String?,
    private var db: FirebaseFirestore?,
    var imageURI: Uri?,
    val date: Date?,
    var rating: Float?,
    var author: String?,
) {
    public fun uploadImage(callback: (String?) -> Unit) {
        val storageRef = FirebaseStorage.getInstance().reference
        val filename = UUID.randomUUID().toString() + ".jpg"


        storageRef.child("images/$filename").putFile(imageURI!!)
            .addOnSuccessListener { taskSnapshot ->
                // Get the download URL for the image
                taskSnapshot.metadata?.reference?.downloadUrl?.addOnSuccessListener { uri ->
                    val downloadUrl = uri.toString()

                    // Store the download URL in Firestore
                    val imageDocRef =
                        FirebaseFirestore.getInstance().collection("images").document()
                    val image = hashMapOf(
                        "url" to downloadUrl
                    )
                    imageDocRef.set(image)
                        .addOnSuccessListener {
                            // Get the document ID of the image document
                            val imageDocId = imageDocRef.id

                            // Get the download URL from Firestore using the document ID
                            FirebaseFirestore.getInstance().collection("images")
                                .document(imageDocId).get()
                                .addOnSuccessListener { documentSnapshot ->
                                    val downloadUrlFromFirestore =
                                        documentSnapshot.get("url").toString()
                                    callback(downloadUrlFromFirestore)
                                }
                                .addOnFailureListener { exception ->
                                    message = "No response code 56"

                                    Log.wtf("BookModel", message)
                                    callback(null)
                                }
                        }
                        .addOnFailureListener { exception ->
                            message = "No response code 61"

                            Log.wtf("BookModel", message)
                            callback(null)
                        }
                }
                    ?.addOnFailureListener { exception ->
                        message = "No response code 62"

                        Log.wtf("BookModel", message)
                        callback(null)
                    }
            }
            .addOnFailureListener { exception ->
                message = "No response code 61"

                Log.wtf("BookModel", message)
                callback(null)
            }
    }

    


    fun createNewBook(newProduct: BookModel, callback: (String?) -> Unit) {
        if (newProduct.userID == null) {
            newProduct.userID = "1"
        }
        this.bookID = newProduct.bookID
        this.bookTitle = newProduct.bookTitle
        this.imageURI = newProduct.imageURI
        this.description = newProduct.description
        this.userID = newProduct.userID
        db = Firebase.firestore
        var message = ""
        uploadImage { downloadUrl ->
            if (downloadUrl != null) {
                // upload the book with the downloadUrl
                imageLink = downloadUrl
                Log.d("BookModel", "Success upload with link $downloadUrl")
                uploadBook { response ->
                    if (response != null) {
                        message = response
                        Log.d("BookModel", "createNewBook: $message")
                    }
                }
                message = "Upload success"
                Log.d("BookModel", "createNewBook: $message")
                callback(message)
            } else {
                message = "Fail to generate download URL"
                Log.wtf("BookModel", message)
                callback(message)
            }
        }
    }

    private fun uploadBook(callback: (String?) -> Unit) {
        var message = "";
        Log.wtf("BookModel", "Sending upload request...")
        db?.collection("books")
            ?.add(this)
            ?.addOnSuccessListener { documentReference ->
                Log.d("awikwok", "DocumentSnapshot added with ID: ${documentReference.id}")
                message = "Successfully created new book"
                Log.wtf("BookModel", message)
                callback(message);
            }?.addOnFailureListener {
                message = "Failed to upload a new book, please try again"
                callback(message);
            }
    }

    public fun updateBookByID(bookID: String, field: String, newValue: String, callback: (Int) -> Unit) {
        val db = Firebase.firestore
        db.collection("books")
            .document(bookID)
            .update(field, newValue)
            .addOnSuccessListener {
                callback(200)
            }
            .addOnFailureListener{
                callback(500)
            }
    }

    public fun deleteBookByID(bookID: String, callback: (Int) -> Unit) {
        val db = Firebase.firestore
        db.collection("books")
            .document(bookID)
            .delete()
            .addOnSuccessListener {
                callback(200)
            }
            .addOnFailureListener{
                callback(500)
            }
    }

}

