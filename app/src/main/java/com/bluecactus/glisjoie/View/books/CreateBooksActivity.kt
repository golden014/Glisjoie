package com.bluecactus.glisjoie.View.books

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.bluecactus.glisjoie.R
import com.bluecactus.glisjoie.ViewModel.BookViewModel
import com.bluecactus.glisjoie.ViewModel.ImageSelectionViewModel

class CreateBooksActivity : AppCompatActivity() {

    private lateinit var viewModel : ImageSelectionViewModel
    private lateinit var bookViewModel : BookViewModel
    private lateinit var selectImageBtn : Button
    private lateinit var addBookBtn : Button
    private lateinit var imageView : ImageView
    private lateinit var titleTF : EditText
    private lateinit var descriptionTF : EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_product)

        //Membuat instance viewmodel
        viewModel = ViewModelProvider(this, ImageSelectionViewModel.Factory(this as CreateBookFragment)).get(ImageSelectionViewModel::class.java)
        bookViewModel = ViewModelProvider(this)[BookViewModel::class.java]
        //Ngambil component di xml
        imageView = findViewById<ImageView>(R.id.CoverImage)

        titleTF = findViewById<EditText>(R.id.BookTitleTF)
        descriptionTF = findViewById<EditText>(R.id.DescriptionTF)

        addBookBtn = findViewById<Button>(R.id.AddBookBtn)
        selectImageBtn = findViewById<Button>(R.id.selectImageBtn)


        //Inisialiasi gambar awal kucing no image
        imageView.setImageResource(R.drawable.image)

        //Observer jika image data berubah
        viewModel.imageData.observe(this) {
            viewModel.setView(imageView)
        }

        //[SELECT IMAGE BTN] Buka view untuk melakukan select image
        selectImageBtn.setOnClickListener {
            viewModel.selectImage()
        }

        //[ADD BOOK BTN] Buat ngeupload book
        addBookBtn.setOnClickListener{
            bookViewModel.createBook(viewModel.imageData.value?.imageUri, titleTF.text.toString(), descriptionTF.text.toString())
        }

        //[ ALERT ] observe response buat alert klo misal product berhasil/gagal
        bookViewModel.response.observe(this) { message ->
            if(message != ""){
                AlertDialog.Builder(this)
                    .setMessage(message)
                    .setPositiveButton("OK", null)
                    .create()
                    .show()
            }
        }
    }
}