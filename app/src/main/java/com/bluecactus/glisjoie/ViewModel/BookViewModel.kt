package com.bluecactus.glisjoie.ViewModel

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bluecactus.glisjoie.Model.BookModel
import java.util.*

class BookViewModel : ViewModel() {

    lateinit var message: String
    private lateinit var book: BookModel
    var response: MutableLiveData<String> = MutableLiveData<String>("")

    fun createBook(imageURI: Uri?, bookTitle: String, bookDescription: String) {
        //TODO: UPDATE THIS BookModel Constructor

        message = ""

        book = BookModel(null, bookTitle, null, null, bookDescription, null, null, imageURI, Date(), null)
        Log.wtf("BookModel", "This is running")
        Log.wtf("BookModel", book.imageURI.toString())
        validateString(bookTitle)
        validateDescription(bookDescription)
        validateUri(imageURI)

        if(message.isBlank()){
            message = uploadBookRequest()
        }else{
            response.value = message
        }

        Log.wtf("BookModel", "BookViewModel: $message")
        if(message.isNotBlank()){
            response.value = message
        }
    }

    private fun validateDescription(bookDescription: String){
        if(bookDescription.isBlank()){
            message = "No description"

            Log.wtf("BookModel",message)
        }else if (bookDescription.length > 1500){
            message = "Description to long (over 1500)"

            Log.wtf("BookModel",message)
        }
    }

    private fun validateString(bookTitle: String) {
        if(bookTitle.isBlank()) {
            message = "No Title"

            Log.wtf("BookModel",message)
        }
    }

    private fun validateUri(imageURI: Uri?) {
        if (imageURI == null){
            message = "No Image Selected"

            Log.wtf("BookModel",message)
        }
    }

    fun uploadBookRequest(): String{
       book.createNewBook(book)
        return message
    }

}