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
import com.bluecactus.glisjoie.Model.BookModel
import com.bluecactus.glisjoie.R
import com.bluecactus.glisjoie.Repository.BookRepository
import com.bluecactus.glisjoie.ViewModel.BookViewModel
import com.bluecactus.glisjoie.ViewModel.ImageSelectionViewModel
import com.bluecactus.glisjoie.ViewModel.ImageSelectionViewModel2
import com.squareup.picasso.Picasso
import java.util.*

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

        bookCover = findViewById(R.id.bookCover)
        bookTitle = findViewById(R.id.bookTitle)
        bookDescription = findViewById(R.id.bookDescription)
        submitButton = findViewById(R.id.deleteBook)
        bookViewModel = ViewModelProvider(this)[BookViewModel::class.java]
        imageViewModel =  ViewModelProvider(this, ImageSelectionViewModel2.Factory(this as EditBooksActivity)).get(ImageSelectionViewModel2::class.java);

        val imageLayout = LinearLayout(this)
        imageLayout.orientation = LinearLayout.VERTICAL
        val image = ImageView(this)
        imageLayout.addView(image)

        val titleLayout = LinearLayout(this)
        titleLayout.orientation = LinearLayout.VERTICAL
        val newTitle = EditText(this)
        newTitle.hint = "New Title"
        newTitle.inputType = InputType.TYPE_CLASS_TEXT
        titleLayout.addView(newTitle)

        val descriptionLayout = LinearLayout(this)
        descriptionLayout.orientation = LinearLayout.VERTICAL
        val newDescription = EditText(this)
        newDescription.hint = "New Description"
        newDescription.inputType = InputType.TYPE_CLASS_TEXT
        descriptionLayout.addView(newDescription)

        imageViewModel.imageData.observe(this) {
            imageViewModel.setView(image)
        }
        val bookModel = BookModel("", "", "", "", "", "", null, null, Date(), 0f, "")
        bookCover.setOnClickListener {
            imageViewModel.selectImage()
            AlertDialog.Builder(this)
                .setTitle("Update Image")
                .setView(imageLayout)
                .setPositiveButton("Update") { dialog, _ ->
                    bookModel.imageURI = imageViewModel.imageData.value?.imageUri
                    bookViewModel.bookData.value?.imageURI = bookModel.imageURI
                    Picasso.get()
                        .load(bookModel.imageURI)
                        .into(bookCover)
                    bookModel.uploadImage(){link ->

                        bookModel.updateBookByID(bookId!!, "imageLink", link!!){
                            message ->
                            AlertDialog.Builder(this).setMessage("Image Updated").create().show()
                        }
                    }
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") {dialog, _ ->
                    dialog.dismiss()
                }
                .setCancelable(false)
                .create()
                .show()
        }

        bookTitle.setOnClickListener(){
            AlertDialog.Builder(this)
                .setTitle("Update Title")
                .setView(titleLayout)
                .setPositiveButton("Update") { dialog, _ ->
                    if(titleValid(newTitle.text.toString())){
                        bookModel.updateBookByID(bookId!!, "bookTitle", newTitle.text.toString()){
                                message ->
                            AlertDialog.Builder(this).setMessage("Title Updated").create().show()
                        }
                        dialog.dismiss()
                    }else{
                        AlertDialog.Builder(this).setMessage("Title Invalid").create().show()
                    }
                }
                .setNegativeButton("Cancel") {dialog, _ ->
                    dialog.dismiss()
                }
                .setCancelable(false)
                .create()
                .show()
        }

        bookDescription.setOnClickListener(){
            AlertDialog.Builder(this)
                .setTitle("Update Description")
                .setView(descriptionLayout)
                .setPositiveButton("Update") { dialog, _ ->
                    if(newDescription.text.toString().length < 1500){
                    bookModel.updateBookByID(bookId!!, "description", newDescription.text.toString()){
                            message ->
                        AlertDialog.Builder(this).setMessage("Description Updated").create().show()
                    }
                    }else{
                        AlertDialog.Builder(this).setMessage("Description Invalid").create().show()
                    }
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

    fun titleValid(username: String): Boolean {
        val pattern = "^[a-zA-Z0-9]+$".toRegex()
        return pattern.matches(username)
    }


}