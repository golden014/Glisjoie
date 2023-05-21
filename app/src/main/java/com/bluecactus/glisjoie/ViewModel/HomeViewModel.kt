package com.bluecactus.glisjoie.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.bluecactus.glisjoie.Model.BookPreviewModel
import com.bluecactus.glisjoie.Repository.BookRepository

class HomeViewModel: ViewModel() {
    val bookRepo = BookRepository()

    fun getTopBooks(callback: (Array<BookPreviewModel>) -> Unit) {
        Log.e("home view model",  "hello")

       bookRepo.getTopBooks { books ->
           val sortedBooks = books.sortedByDescending { it.rating }.toTypedArray()
           callback(sortedBooks)

       }
    }
}