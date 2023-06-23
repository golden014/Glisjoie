package edu.bluejack22_2.Glisjoie.ViewModel

import android.net.Uri
import android.util.Log
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.bluejack22_2.Glisjoie.Repository.BookRepository
import com.squareup.picasso.Picasso
import java.util.*

class BookViewModel : ViewModel() {

    lateinit var message: String
    private lateinit var book: edu.bluejack22_2.Glisjoie.Model.BookModel
    var response: MutableLiveData<String> = MutableLiveData<String>("")
    var bookData: MutableLiveData<edu.bluejack22_2.Glisjoie.Model.BookModel> = MutableLiveData<edu.bluejack22_2.Glisjoie.Model.BookModel>(null)
    var countData: MutableLiveData<Int> = MutableLiveData<Int>(0)
    fun getBookByID(bookID: String?) {
        val bookRepo = BookRepository()
        return bookRepo.getBookByID(bookID) { bookModel ->
            if (bookModel != null) {
                Log.d("JEVCON", "getBookByID: $bookID")
                BookRepository().getCommentsAndRatingForBook(bookID!!){ sum, average ->
                    Log.d("JEVCON12312", "getBookByID: $sum $average")
                    bookModel.rating =  "%.2f".format(average).toFloat()
                    bookData.value = bookModel
                    countData.value = sum
                    Log.d("HAPPY", bookData.value!!.bookTitle)
                }
            } else {
                println("Error retrieving book details.")
            }
        }
    }

    fun createBook(imageURI: Uri?, bookTitle: String, bookDescription: String, callback: (String) -> Unit) {
        //TODO: UPDATE THIS BookModel Constructor
        val userViewModel = UserViewModel();
        message = ""
        userViewModel.getCurrUser(){ it ->
            book = edu.bluejack22_2.Glisjoie.Model.BookModel(
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
                    callback(str)
                    response.value = str
                }
                //success
                Log.d("BookViewModel", "createBook: with message$message")
            } else {
                callback(message)
                response.value = message
            }

            Log.wtf("BookModel", "BookViewModel: $message")
//            if (message.isNotBlank()) {
//                callback(message)
//                response.value = message
//            }
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

    fun updateData(bookCover: ImageView, bookTitle: TextView, bookDescription: TextView) {
        if(bookData.value != null){
            val book = bookData.value ?: return
            Picasso.get()
                .load(book.imageLink)
                .into(bookCover)
            bookTitle.text = book.bookTitle
            bookDescription.text = book.description
        }else{
            Log.e("BookModelView", "bookData is null")
        }
    }



}