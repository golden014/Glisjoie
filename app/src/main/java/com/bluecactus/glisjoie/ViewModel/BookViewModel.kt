package com.bluecactus.glisjoie.ViewModel

import android.net.Uri
import android.os.Message
import android.util.Log
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bluecactus.glisjoie.Model.BookModel
import com.bluecactus.glisjoie.Repository.BookRepository
import com.google.firebase.firestore.auth.User
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.math.log

class BookViewModel : ViewModel() {

    lateinit var message: String
    private lateinit var book: BookModel
    var response: MutableLiveData<String> = MutableLiveData<String>("")
    var bookData: MutableLiveData<BookModel> = MutableLiveData<BookModel>(null)
    var countData: MutableLiveData<Int> = MutableLiveData<Int>(0)
    fun getBookByID(bookID: String?) {
        val bookRepo = BookRepository()
        return bookRepo.getBookByID(bookID) { bookModel ->
            if (bookModel != null) {
                Log.d("JEVCON", "getBookByID: $bookID")
                BookRepository().getCommentsAndRatingForBook(bookID!!){ sum, average ->
                    Log.d("JEVCON12312", "getBookByID: $sum $average")
                    bookModel.rating = average
                    bookData.value = bookModel
                    countData.value = sum
                    Log.d("HAPPY", bookData.value!!.bookTitle)
                }
            } else {
                println("Error retrieving book details.")
            }
        }
    }

    fun createBook(imageURI: Uri?, bookTitle: String, bookDescription: String) {
        //TODO: UPDATE THIS BookModel Constructor
        val userViewModel = UserViewModel();
        message = ""
        userViewModel.getCurrUser(){ it ->
            book = BookModel(
                null,
                bookTitle,
                null,
                it.userDocumentID,
                bookDescription,
                null,
                null,
                imageURI,
                Date(),
                0f,
                null
            )
            Log.wtf("BookModel", "This is running")
            Log.wtf("BookModel", book.imageURI.toString())
            validateString(bookTitle)
            validateDescription(bookDescription)
            validateUri(imageURI)

            if (message.isBlank()) {
                uploadBookRequest(){ str->
                    response.value = str
                }
                Log.d("BookViewModel", "createBook: with message$message")
            } else {
                response.value = message
            }

            Log.wtf("BookModel", "BookViewModel: $message")
            if (message.isNotBlank()) {
                response.value = message
            }
        }
    }

    private fun validateDescription(bookDescription: String) {
        if (bookDescription.isBlank()) {
            message = "No description"

            Log.wtf("BookModel", message)
        } else if (bookDescription.length > 1500) {
            message = "Description to long (over 1500)"

            Log.wtf("BookModel", message)
        }
    }

    private fun validateString(bookTitle: String) {
        if (bookTitle.isBlank()) {
            message = "No Title"

            Log.wtf("BookModel", message)
        }
    }

    private fun validateUri(imageURI: Uri?) {
        if (imageURI == null) {
            message = "No Image Selected"

            Log.wtf("BookModel", message)
        }
    }

    fun uploadBookRequest(callback:(String) -> Unit) {
        book.createNewBook(book){str ->
            callback(str!!)
        }
    }

    fun updateData(bookCover: ImageView, bookTitle: TextView, bookAuthor: TextView, bookRating: RatingBar, bookDescription: TextView) {
        if(bookData.value != null){
            val book = bookData.value ?: return
            Picasso.get()
                .load(book.imageLink)
                .into(bookCover)
            bookTitle.text = book.bookTitle
            bookAuthor.text = book.author
            bookRating.rating = book.rating?.toFloat()!!
            bookDescription.text = book.description
        }else{
            Log.e("BookModelView", "bookData is null")
        }
    }



}