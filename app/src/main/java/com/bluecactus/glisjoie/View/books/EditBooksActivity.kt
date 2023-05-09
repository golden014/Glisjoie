package com.bluecactus.glisjoie.View.books

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bluecactus.glisjoie.R
import com.bluecactus.glisjoie.ViewModel.BookViewModel

class EditBooksActivity : AppCompatActivity() {

    private lateinit var bookCover: ImageView
    private lateinit var bookTitle: EditText
    private lateinit var bookDescription: EditText
    private lateinit var submitButton: Button

    private lateinit var viewModel: BookViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_book)

        bookCover = findViewById<ImageView>(R.id.bookCover)
        bookTitle = findViewById<EditText>(R.id.bookTitle)
        bookDescription = findViewById<EditText>(R.id.bookDescription)
        submitButton = findViewById<Button>(R.id.confirmEdit)
        viewModel = ViewModelProvider(this)[BookViewModel::class.java]



    }

}