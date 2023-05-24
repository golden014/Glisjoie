package com.bluecactus.glisjoie.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bluecactus.glisjoie.Model.BookModel
import com.bluecactus.glisjoie.Model.BookPreviewModel
import com.bluecactus.glisjoie.Model.CommentModel
import com.bluecactus.glisjoie.Model.UserModel
import com.bluecactus.glisjoie.Repository.BookRepository
import com.bluecactus.glisjoie.Repository.CommentRepository
import java.util.Date

class CommentViewModel : ViewModel(){

    var response: MutableLiveData<String> = MutableLiveData<String>("")

    fun validateComment(comment : String, rating : Float, user : UserModel, book : BookModel){
        response = MutableLiveData<String>();
        var message = ""
        var currRating = rating
        if(comment.isBlank()){
            message = "Comment is empty"
        }
        if(currRating == null){
            currRating = 0f
        }
        if(message.isBlank()){
            CommentRepository().createCommentRepository(CommentModel(user.userDocumentID, book.bookID, Date(), rating, comment)){str ->
                response.value = str
            }
        }else{
            response.value = message
        }
    }

}