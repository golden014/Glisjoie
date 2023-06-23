package edu.bluejack22_2.Glisjoie.ViewModel

import androidx.lifecycle.ViewModel
import edu.bluejack22_2.Glisjoie.Model.BookPreviewModel
import edu.bluejack22_2.Glisjoie.Repository.BookRepository

class SearchViewModel: ViewModel() {
    val bookRepo = BookRepository()

    fun performSearch(query: String, callback: (Array<BookPreviewModel>) -> Unit) {
        bookRepo.performSearch(query, callback)
    }
}