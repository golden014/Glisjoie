package edu.bluejack22_2.Glisjoie.Model

interface BookRepository {
    fun getTopBooks(callback: (Array<BookPreviewModel>) -> Unit)
    fun performSearch(keyword: String, callback: (Array<BookPreviewModel>) -> Unit)
    fun getBookByID(bookID: String?, callback: (edu.bluejack22_2.Glisjoie.Model.BookModel?) -> Unit)
}

