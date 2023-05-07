package com.bluecactus.glisjoie.Model

interface BookRepository {
    fun getTopBooks(callback: (Array<BookPreviewModel>) -> Unit)
}