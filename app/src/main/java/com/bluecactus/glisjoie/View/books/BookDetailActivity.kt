package com.bluecactus.glisjoie.View.books

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bluecactus.glisjoie.R
import com.bluecactus.glisjoie.ViewModel.BookViewModel
import com.bluecactus.glisjoie.ViewModel.ImageSelectionViewModel
import de.hdodenhof.circleimageview.CircleImageView

class BookDetailActivity : AppCompatActivity() {

    //general component
    private lateinit var bookCover:ImageView
    private lateinit var bookTitle:TextView
    private lateinit var bookAuthor:TextView
    private lateinit var bookDescription:TextView
    private lateinit var bookRating:RatingBar
    private lateinit var bookComments:RecyclerView

    //comment component
    private lateinit var bookCommentField:EditText
    private lateinit var bookCommentIcon:CircleImageView
    private lateinit var bookCommentButton:Button

    //view models
    private lateinit var bookViewModel: BookViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_detail)

        bookViewModel = ViewModelProvider(this).get(BookViewModel::class.java)

        bookCover = findViewById(R.id.bookCover)
        bookTitle = findViewById(R.id.bookTitle)
        bookAuthor = findViewById(R.id.bookAuthor)
        bookRating = findViewById(R.id.ratingBar)
        bookDescription = findViewById(R.id.bookDescription)
        bookRating.setIsIndicator(true)

        var bookId = ""
        //TODO: IMPORTANT!! uncomment when receiving put extra
        //bookId = intent.getStringExtra("KEYNAME")

        bookViewModel.bookData.observe(this) {
            bookViewModel.updateData(bookCover, bookTitle, bookAuthor, bookRating, bookDescription)
        }

        //TODO: IMPORTANT!! replace this with the bookId provided
        bookViewModel.getBookByID("8jmeFAsRMDQsCDJ90gYu")

    }

}