package com.bluecactus.glisjoie.View.books

import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bluecactus.glisjoie.ViewModel.BookViewModel

class EditBooksActivity : AppCompatActivity() {

    private lateinit var bookCover: ImageView
    private lateinit var bookTitle: EditText
    private lateinit var bookDescription: EditText
    private lateinit var submitButton: Button

    private lateinit var viewModel: BookViewModel

}