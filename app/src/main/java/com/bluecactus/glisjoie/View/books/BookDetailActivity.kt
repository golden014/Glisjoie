package com.bluecactus.glisjoie.View.books

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bluecactus.glisjoie.Model.BookModel
import com.bluecactus.glisjoie.Model.CommentModel
import com.bluecactus.glisjoie.Model.UserModel
import com.bluecactus.glisjoie.R
import com.bluecactus.glisjoie.Repository.CommentRepository
import com.bluecactus.glisjoie.ViewModel.*
import de.hdodenhof.circleimageview.CircleImageView
import kotlin.math.log

class BookDetailActivity : AppCompatActivity() {

    //general component
    private lateinit var bookCover:ImageView
    private lateinit var bookTitle:TextView
    private lateinit var bookAuthor:TextView
    private lateinit var bookDescription:TextView
    private lateinit var bookRating:RatingBar
    private lateinit var recyclerView:RecyclerView
    private lateinit var adapter: CommentAdapter

    //comment component
    private lateinit var bookCommentField:EditText
    private lateinit var bookCommentIcon:CircleImageView
    private lateinit var bookCommentButton:Button

    //curr user to check author or not
    private lateinit var currUser: UserModel

    //view models
    private lateinit var bookViewModel: BookViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var viewHistoryViewModel: ViewHistoryViewModel

    //Data
    private var arr : ArrayList<CommentModel> = ArrayList<CommentModel>();


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_detail)

        var bookId = intent.getStringExtra("bookID")
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        bookViewModel = ViewModelProvider(this).get(BookViewModel::class.java)
        viewHistoryViewModel = ViewModelProvider(this).get(ViewHistoryViewModel::class.java)

        recyclerView = findViewById(R.id.detailCommentRecyclerView)
        CommentRepository().getCommentByBookID(
            BookModel("",
            "",
            "8jmeFAsRMDQsCDJ90gYu",
            "",
            "",
            "",
            null,
                null,
                null,
                null,
                "")){ resCall ->
            for(i in 0 until resCall.size){
                Log.d("ASDWAWDD", "onCreate: "+ resCall.get(i).description)
            }
            arr = resCall
            adapter = CommentAdapter(R.layout.item_comment, mutableListOf());
            recyclerView.layoutManager = LinearLayoutManager(this@BookDetailActivity)
            recyclerView.adapter = adapter;
            adapter.updateData(arr)
        };

        userViewModel.getCurrUser() { it ->
            currUser = it
            var authorId = intent.getStringExtra("authorID")

            viewHistoryViewModel.updateHistory(currUser.userDocumentID, bookId as String)

            if (currUser.userDocumentID == authorId) {
                Log.e("bookDetail1", "authorIDIntent: " + authorId)
                Log.e("bookDetail1", "authorID: " + currUser.userDocumentID)
        //        TODO: IMPORTANT!! masukin intent filter ke edit books activity
                //atau mau buat satu icon edit gtu yg bakalan visible kalau dia adalah author yg redirect ke
                //edit books activity

            } else {
                Log.e("bookDetail1", "bukan punya curr user")
                Log.e("bookDetail1", "authorIDIntent: " + authorId)
                Log.e("bookDetail1", "authorID: " + currUser.userDocumentID)
            }
        }


        bookCover = findViewById(R.id.bookCover)
        bookTitle = findViewById(R.id.bookTitle)
        bookAuthor = findViewById(R.id.bookAuthor)
        bookRating = findViewById(R.id.ratingBar)
        bookDescription = findViewById(R.id.bookDescription)
        bookRating.setIsIndicator(true)

//        var bookId = intent.getStringExtra("bookID")
//        TODO: IMPORTANT!! uncomment when receiving put extra
        //bookId = intent.getStringExtra("KEYNAME")

        bookViewModel.bookData.observe(this) {
            bookViewModel.updateData(bookCover, bookTitle, bookAuthor, bookRating, bookDescription)
        }

        //TODO: IMPORTANT!! replace this with the bookId provided
        bookViewModel.getBookByID(bookId)

    }

}