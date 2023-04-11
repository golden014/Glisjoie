package com.bluecactus.glisjoie.View.product

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bluecactus.glisjoie.R
import com.bluecactus.glisjoie.ViewModel.BookModelView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CreateProductActivity : AppCompatActivity() {

    private lateinit var viewModel : BookModelView
    private lateinit var selectImageBtn : Button
    private lateinit var imageView : ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_product)

        //Membuat instance viewmodel
        viewModel = ViewModelProvider(this)[BookModelView::class.java]

        imageView = findViewById<ImageView>(R.id.CoverImage)
        selectImageBtn = findViewById<Button>(R.id.selectImageBtn)
//        val TextString = getString

        //
        val imagePicker = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                //Set placeholder image
                viewModel.imageID.value?.let { imageView.setImageResource(it) }
            }
        }


        selectImageBtn.setOnClickListener {
            imagePicker.launch("image/*")
//            Log.d("DATA", imagePicker.)
        }

    }
}