package com.bluecactus.glisjoie.View.books

import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bluecactus.glisjoie.R
import com.bluecactus.glisjoie.ViewModel.BookViewModel
import com.bluecactus.glisjoie.ViewModel.ImageSelectionViewModel
import com.bluecactus.glisjoie.ViewModel.ImageSelectionViewModel2

class EditBooksActivity : AppCompatActivity() {

    private lateinit var bookCover: ImageView
    private lateinit var bookTitle: TextView
    private lateinit var bookDescription: TextView
    private lateinit var submitButton: Button

    private lateinit var bookViewModel: BookViewModel
    private lateinit var imageViewModel: ImageSelectionViewModel2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_book)

        var bookId = intent.getStringExtra("bookID")

        bookCover = findViewById<ImageView>(R.id.bookCover)
        bookTitle = findViewById<TextView>(R.id.bookTitle)
        bookDescription = findViewById<TextView>(R.id.bookDescription)
        submitButton = findViewById<Button>(R.id.deleteBook)
        bookViewModel = ViewModelProvider(this)[BookViewModel::class.java]
        imageViewModel =  ViewModelProvider(this, ImageSelectionViewModel2.Factory(this as EditBooksActivity)).get(ImageSelectionViewModel2::class.java);



        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL


        val currPassword = EditText(this)
        currPassword.hint = "Enter your current password"
        currPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        layout.addView(currPassword)

        val newPassword = EditText(this)
        newPassword.hint = "Enter your new password"
        newPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        layout.addView(newPassword)

        val confNewPass = EditText(this)
        confNewPass.hint = "Reenter your password to confirm"
        confNewPass.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        layout.addView(confNewPass)

        bookCover.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Update Password")
                .setView(layout)
                .setPositiveButton("Update") { dialog, _ ->

                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") {dialog, _ ->
                    dialog.dismiss()
                }
                .setCancelable(false)
                .create()
                .show()
        }

        bookViewModel.bookData.observe(this) {
            bookViewModel.updateData(bookCover, bookTitle, bookDescription)
        }

        bookViewModel.getBookByID(bookId)
    }

}