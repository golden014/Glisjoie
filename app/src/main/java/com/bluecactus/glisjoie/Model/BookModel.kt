package com.bluecactus.glisjoie.Model

import java.util.Date

class BookModel (
    val bookID: String,
    val userID: String,
    val date: Date,
    val rating: Float,
    val description: String
) {}