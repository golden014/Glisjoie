package com.bluecactus.glisjoie.ViewModel

import androidx.lifecycle.ViewModel
import com.bluecactus.glisjoie.Model.BookPreviewModel
import com.bluecactus.glisjoie.Repository.BookRepository

class SearchViewModel: ViewModel() {
    val bookRepo = BookRepository()

    fun performSearch(query: String, callback: (Array<BookPreviewModel>) -> Unit) {

    }
}