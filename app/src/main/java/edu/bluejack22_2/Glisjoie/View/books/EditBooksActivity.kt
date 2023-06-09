package edu.bluejack22_2.Glisjoie.View.books

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import edu.bluejack22_2.Glisjoie.View.HomeActivity
import edu.bluejack22_2.Glisjoie.ViewModel.BookViewModel
import edu.bluejack22_2.Glisjoie.ViewModel.ImageSelectionViewModel2
import edu.bluejack22_2.glisjoie.R
import java.util.*

class EditBooksActivity : AppCompatActivity() {

    private lateinit var bookCover: ImageView
    private lateinit var bookTitle: TextView
    private lateinit var bookDescription: TextView
    private lateinit var deleteButton: Button

    private lateinit var bookViewModel: BookViewModel
    private lateinit var imageViewModel: ImageSelectionViewModel2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_book)

        var bookId = intent.getStringExtra("bookID")

        bookCover = findViewById(R.id.bookCover)
        bookTitle = findViewById(R.id.bookTitle)
        bookDescription = findViewById(R.id.bookDescription)
        deleteButton = findViewById(R.id.deleteBook)
        bookViewModel = ViewModelProvider(this)[BookViewModel::class.java]
        imageViewModel =  ViewModelProvider(this, ImageSelectionViewModel2.Factory(this as EditBooksActivity)).get(ImageSelectionViewModel2::class.java);

        val imageLayout = LinearLayout(this)
        imageLayout.orientation = LinearLayout.VERTICAL
        val image = ImageView(this)
        imageLayout.addView(image)
        val alertDialogImageBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        var alertDialogImage: AlertDialog? = null

        val titleLayout = LinearLayout(this)
        titleLayout.orientation = LinearLayout.VERTICAL
        val newTitle = EditText(this)
        newTitle.hint = "New Title"
        newTitle.inputType = InputType.TYPE_CLASS_TEXT
        titleLayout.addView(newTitle)
        val alertDialogTitleBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        var alertDialogTitle: AlertDialog? = null

        val descriptionLayout = LinearLayout(this)
        descriptionLayout.orientation = LinearLayout.VERTICAL
        val newDescription = EditText(this)
        newDescription.hint = "New Description"
        newDescription.inputType = InputType.TYPE_CLASS_TEXT
        descriptionLayout.addView(newDescription)
        val alertDialogDescriptionBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        var alertDialogDescription: AlertDialog? = null

        imageViewModel.imageData.observe(this) {
            imageViewModel.setView(image)
        }
        val bookModel = edu.bluejack22_2.Glisjoie.Model.BookModel(
            "",
            "",
            "",
            "",
            "",
            "",
            null,
            null,
            Date(),
            0f,
            ""
        )
        bookCover.setOnClickListener {
            if (alertDialogImage == null || !alertDialogImage!!.isShowing) {
                if (imageLayout.parent != null) {
                    (imageLayout.parent as ViewGroup).removeView(imageLayout)
                }
                image.setImageDrawable(null)
                alertDialogImageBuilder.setView(imageLayout)

                alertDialogImageBuilder.setPositiveButton("Update") { dialog, _ ->
                    bookModel.imageURI = imageViewModel.imageData.value?.imageUri
                    bookViewModel.bookData.value?.imageURI = bookModel.imageURI
                    Picasso.get()
                        .load(bookModel.imageURI)
                        .into(bookCover)
                    bookModel.uploadImage { link ->
                        bookModel.updateBookByID(bookId!!, "imageLink", link!!) { message ->
                            Toast.makeText(this, "Image update success", Toast.LENGTH_SHORT).show()
                            deleteButton.isEnabled = true
                        }
                    }
                }
                alertDialogImageBuilder.setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                    deleteButton.isEnabled = true
                }
                alertDialogImageBuilder.setCancelable(false)
                alertDialogImage = alertDialogImageBuilder.create()
                alertDialogImage?.show()
                imageViewModel.selectImage()
            }
        }

        deleteButton.setOnClickListener {
            if (bookId != null) {
                bookModel.deleteBookByID(bookId){it ->
                    if(it == 200) {
                        Toast.makeText(this, "Delete success", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                    }

                    if(it == 500) Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }
        }

        imageViewModel.imageData.observe(this) { imageData ->
            imageData?.let {
                image.setImageURI(it.imageUri)
            }
        }


        bookTitle.setOnClickListener {
                if (titleLayout.parent != null) {
                    (titleLayout.parent as ViewGroup).removeView(titleLayout)
                }
                // Clear the EditText
                newTitle.text.clear()
                alertDialogTitleBuilder.setView(titleLayout)
                alertDialogTitleBuilder.setPositiveButton("Update") { dialog, _ ->
                    val enteredTitle = newTitle.text.toString()
                    if (titleValid(enteredTitle)) {
                        bookModel.updateBookByID(bookId!!, "bookTitle", enteredTitle) { message ->
                            AlertDialog.Builder(this).setMessage("Title Updated").create().show()
                            bookTitle.text = enteredTitle;
                        }
                        dialog.dismiss()
                    } else {
                        AlertDialog.Builder(this).setMessage("Title Invalid").create().show()
                    }
                }
                alertDialogTitleBuilder.setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                alertDialogTitleBuilder.setCancelable(false)
                alertDialogTitle = alertDialogTitleBuilder.create()
                alertDialogTitle?.show()
        }

        bookDescription.setOnClickListener {
            newDescription.text.clear()
            alertDialogDescriptionBuilder.setView(descriptionLayout)
            alertDialogDescriptionBuilder.setPositiveButton("Update") { dialog, _ ->
                val enteredDescription = newDescription.text.toString()
                if (enteredDescription.length < 1500) {
                    bookModel.updateBookByID(bookId!!, "description", enteredDescription) { message ->
                        AlertDialog.Builder(this).setMessage("Description Updated").create().show()
                        bookDescription.text = enteredDescription;
                    }
                } else {
                    AlertDialog.Builder(this).setMessage("Description Invalid").create().show()
                }
                dialog.dismiss()
            }
            alertDialogDescriptionBuilder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            alertDialogDescriptionBuilder.setCancelable(false)
            alertDialogDescription = alertDialogDescriptionBuilder.create()
            alertDialogDescription?.show()
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