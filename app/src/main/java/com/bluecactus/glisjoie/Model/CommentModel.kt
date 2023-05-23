package com.bluecactus.glisjoie.Model

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class CommentModel(
    var userID: String?,
    var bookID: String?,
    var date: Date?,
    var rating: Float?,
    var description: String?
) {}