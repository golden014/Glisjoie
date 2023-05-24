package com.bluecactus.glisjoie.View.books

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bluecactus.glisjoie.Model.BookModel
import com.bluecactus.glisjoie.Model.CommentModel
import com.bluecactus.glisjoie.Model.UserModel
import com.bluecactus.glisjoie.R
import com.bluecactus.glisjoie.Repository.BookRepository
import com.bluecactus.glisjoie.Repository.CommentRepository
import com.bluecactus.glisjoie.View.profile.ProfileActivity
import com.bluecactus.glisjoie.ViewModel.*
import de.hdodenhof.circleimageview.CircleImageView
import org.w3c.dom.Text
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
    private lateinit var commentViewModel: CommentViewModel
    private lateinit var viewHistoryViewModel: ViewHistoryViewModel
    private lateinit var bookCommentRating: RatingBar
    private lateinit var bookReviewCount : TextView

    //Data
    private var arr : ArrayList<CommentModel> = ArrayList<CommentModel>();


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_detail)

        var bookId = intent.getStringExtra("bookID")
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        bookViewModel = ViewModelProvider(this).get(BookViewModel::class.java)
        commentViewModel = ViewModelProvider(this).get(CommentViewModel::class.java)
        viewHistoryViewModel = ViewModelProvider(this).get(ViewHistoryViewModel::class.java)

        recyclerView = findViewById(R.id.detailCommentRecyclerView)
        CommentRepository().getCommentByBookID(
            BookModel("",
            "",
                bookId,
            "",
            "",
            "",
            null,
                null,
                null,
                null,
                "")){ resCall ->
            for(i in 0 until resCall.size){ Log.d("GOLDWASSER", "onCreate: "+ resCall.get(i).description) }
            arr = resCall
            adapter = CommentAdapter(R.layout.item_comment, mutableListOf());
            recyclerView.layoutManager = LinearLayoutManager(this@BookDetailActivity)
            recyclerView.adapter = adapter;
            adapter.updateData(arr)
        };

        var authorId = intent.getStringExtra("authorID")
        userViewModel.getCurrUser() { it ->
            currUser = it

            viewHistoryViewModel.updateHistory(currUser.userDocumentID, bookId as String)

            if (currUser.userDocumentID == authorId) {
                Log.e("bookDetail1", "authorIDIntent: " + authorId)
                Log.e("bookDetail1", "authorID: " + currUser.userDocumentID)

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
        bookReviewCount = findViewById(R.id.reviewCount)
        bookRating.setIsIndicator(true)

        bookCommentField = findViewById(R.id.detailCommentContent)
        bookCommentRating = findViewById(R.id.detailCommentRating)
        bookCommentIcon = findViewById(R.id.detailCommentPicture)
        bookCommentButton = findViewById(R.id.bookDetailCommentBtn)

        commentViewModel.response.observe(this) { message ->
            Log.d("TESTSADAW", "onCreate:" )
            if(message != null && message != ""){
                Log.d("BookDetailActivity", "onCreate: $message")
                AlertDialog.Builder(this)
                    .setMessage(message)
                    .setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface, i ->
                            if(message == "Success Comment"){
                                finish();
                                startActivity(getIntent());
                            }
                    })
                    .create()
                    .show()
            }
        }

        bookCommentButton.setOnClickListener {
            commentViewModel.validateComment(bookCommentField.text.toString(), bookCommentRating.rating, currUser, bookViewModel.bookData.value!!)
        };

//      var bookId = intent.getStringExtra("bookID")
//      TODO: IMPORTANT!! uncomment when receiving put extra
//      bookId = intent.getStringExtra("KEYNAME")

        bookViewModel.countData.observe(this){
            bookReviewCount.text = bookViewModel.countData.value.toString() + " Reviews"
        }

        bookViewModel.bookData.observe(this) {
            bookViewModel.updateData(bookCover, bookTitle, bookAuthor, bookRating, bookDescription)
        }

        //TODO: IMPORTANT!! replace this with the bookId provided

//        userViewModel.getCurrUser {currUs->
            bookAuthor.setOnClickListener{ it ->
                Log.d("GETADAW", "onCreate: ${authorId}")
                val intent = Intent(this, ProfileActivity::class.java).putExtra("userId", authorId)
                startActivity(intent)
//            }
        }
        bookViewModel.getBookByID(bookId)



    }

}