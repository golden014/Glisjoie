package com.bluecactus.glisjoie.Model

interface BookRepository {
    fun getTopBooks(callback: (Array<BookPreviewModel>) -> Unit)
    fun performSearch(keyword: String, callback: (Array<BookPreviewModel>) -> Unit)
    fun getBookByID(bookID: String?, callback: (BookModel?) -> Unit)
}

