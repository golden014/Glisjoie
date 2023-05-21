package com.bluecactus.glisjoie.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bluecactus.glisjoie.Model.BookPreviewModel
import com.bluecactus.glisjoie.Repository.BookRepository

class HomeViewModel: ViewModel() {
    val bookRepo = BookRepository()
    val _currBooks = MutableLiveData<Array<BookPreviewModel>>()
    val currBooks: LiveData<Array<BookPreviewModel>> = _currBooks


    init {
        getTopBooks()
    }

    fun getTopBooks() {
        bookRepo.getTopBooks { booksArray ->
            val sorted = booksArray.sortedByDescending { it.rating }.toTypedArray()
            _currBooks.value = sorted
        }
    }

    fun updateBooks(newBooks: Array<BookPreviewModel>) {
        Log.e("fragment", "book updated")
//        Log.e("fragment", newBooks[0].title)
        _currBooks.value = newBooks
    }

    fun clearBooks() {
        Log.e("fragment5", "clearBooks executed asdasd")
        _currBooks.value = arrayOf<BookPreviewModel>()
    }

//    fun getTopBooks(callback: (Array<BookPreviewModel>) -> Unit) {
//        Log.e("home view model",  "hello")
//
//       bookRepo.getTopBooks { books ->
//           val sortedBooks = books.sortedByDescending { it.rating }.toTypedArray()
//           callback(sortedBooks)
//
//       }
//    }
}