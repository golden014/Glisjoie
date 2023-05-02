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
    private var message: String?,
    private var bookTitle: String,
    private var bookID: String?,
    private var userID: String?,
    private var description: String,
    private var imageLink: String?,
    private var db: FirebaseFirestore?,
    var imageURI: Uri?,
    val date: Date?,
    val rating: Float?
) {

    private fun uploadImage(callback: (String?) -> Unit) {
        val storageRef = FirebaseStorage.getInstance().reference
        val filename = UUID.randomUUID().toString() + ".jpg"

        storageRef.child("images/$filename").putFile(imageURI!!)
            .addOnSuccessListener { taskSnapshot ->
                // Get the download URL for the image
                taskSnapshot.metadata?.reference?.downloadUrl?.addOnSuccessListener { uri ->
                    val downloadUrl = uri.toString()

                    // Store the download URL in Firestore
                    val imageDocRef = FirebaseFirestore.getInstance().collection("images").document()
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
                                    val downloadUrlFromFirestore = documentSnapshot.get("url").toString()
                                    callback(downloadUrlFromFirestore)
                                }
                                .addOnFailureListener { exception ->
                                    message = "No response code 56"

                                    Log.wtf("BookModel",message)
                                    callback(null)
                                }
                        }
                        .addOnFailureListener { exception ->
                            message = "No response code 61"

                            Log.wtf("BookModel",message)
                            callback(null)
                        }
                }
                    ?.addOnFailureListener { exception ->
                        message = "No response code 62"

                        Log.wtf("BookModel",message)
                        callback(null)
                    }
            }
            .addOnFailureListener { exception ->
                message = "No response code 61"

                Log.wtf("BookModel",message)
                callback(null)
            }
    }




    fun createNewBook(newProduct : BookModel): String? {
        if(newProduct.userID == null){
            newProduct.userID = "1"
        }
        this.bookID = newProduct.bookID
        this.bookTitle = newProduct.bookTitle
        this.imageURI = newProduct.imageURI
        this.description = newProduct.description
        this.userID = newProduct.userID
        db = Firebase.firestore

        uploadImage { downloadUrl ->
            if (downloadUrl != null) {
                // upload the book with the downloadUrl
                imageLink = downloadUrl
                Log.d("BookModel", "Success upload with link $downloadUrl")
                uploadBook{ response ->
                    message = response
                }
            } else {
                message = "Fail to generate download URL"
                Log.wtf("BookModel",message)
            }
        }

        //TODO: BUATIN HANDLE (DONE)
        return message
    }


    private fun uploadBook(callback: (String?) -> Unit) {
        Log.wtf("BookModel","Sending upload request...")
        db?.collection("books")
            ?.add(this)
            ?.addOnSuccessListener { documentReference ->
                Log.d("awikwok", "DocumentSnapshot added with ID: ${documentReference.id}")
                message = "Successfully created new book"
                Log.wtf("BookModel",message)
            }?.addOnFailureListener {
                message = "Failed to upload a new book, please try again"
            }
    }
}