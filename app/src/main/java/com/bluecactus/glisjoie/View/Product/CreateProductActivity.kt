package com.bluecactus.glisjoie.View.Product

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bluecactus.glisjoie.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CreateProductActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_product)

        val db = Firebase.firestore

        // Create a new user with a first and last name
        val user = hashMapOf(
            "first" to "Ada",
            "last" to "Lovelace",
            "born" to 1815
        )

        // Add a new document with a generated ID
        db.collection("Book")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d("PRODUCTACT", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("PRODUCTACT", "Error adding document", e)
            }

    }

}