package edu.bluejack22_2.Glisjoie.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.bluejack22_2.Glisjoie.Model.CommentModel
import edu.bluejack22_2.Glisjoie.Model.UserModel
import edu.bluejack22_2.Glisjoie.Repository.CommentRepository
import java.util.Date

class CommentViewModel : ViewModel(){

    var response: MutableLiveData<String> = MutableLiveData<String>("")

    fun validateComment(comment : String, rating : Float, user : UserModel, book : edu.bluejack22_2.Glisjoie.Model.BookModel){
        var message = ""
        var currRating = rating
        if(comment.isBlank()){
            message = "Comment is empty"
        }
        if(currRating == null){
            currRating = 0f
        }

        Log.d("A", "validateComment: " + message)
        if(message.isBlank()){
            CommentRepository().createCommentRepository(CommentModel(user.userDocumentID, book.bookID, Date(), rating, comment)){str ->
                response.value = str
            }
        }else{
            response.value = message
        }
    }

}