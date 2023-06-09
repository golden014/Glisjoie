package edu.bluejack22_2.Glisjoie.View.books

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
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
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import edu.bluejack22_2.Glisjoie.Model.CommentModel
import edu.bluejack22_2.Glisjoie.Model.UserModel
import edu.bluejack22_2.Glisjoie.Repository.CommentRepository
import edu.bluejack22_2.Glisjoie.View.profile.ProfileActivity
import edu.bluejack22_2.Glisjoie.ViewModel.*
import edu.bluejack22_2.glisjoie.R
import java.lang.Exception

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
    private lateinit var editBookButton:ImageView

    //curr user to check author or not
    private lateinit var currUser: UserModel
    //view models
    private lateinit var bookViewModel: BookViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var commentViewModel: CommentViewModel
    private lateinit var viewHistoryViewModel: ViewHistoryViewModel
    private lateinit var bookCommentRating: RatingBar
    private lateinit var bookReviewCount : TextView

    private lateinit var returnButton: ImageView

    //Data
    private var arr : ArrayList<CommentModel> = ArrayList<CommentModel>();

    override fun onResume() {
        super.onResume()

        val bookId = getFromSharedPref(this, "bookId", "null");

        bookViewModel.getBookByID(bookId);
        bookViewModel.updateData(bookCover, bookTitle, bookAuthor, bookRating, bookDescription)
    }

    fun saveToSharedPref(context: Context, key: String, value: String) {
        val sharedPreferences = context.getSharedPreferences("BookPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getFromSharedPref(context: Context, key: String, defaultValue: String): String? {
        val sharedPreferences = context.getSharedPreferences("BookPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString(key, defaultValue)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_detail)

        returnButton = findViewById(R.id.return_button_action_bar)

        returnButton.setOnClickListener() {
            Log.e("return", "oke")
            onBackPressed()
        }

        var bookId = intent.getStringExtra("bookID")
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        bookViewModel = ViewModelProvider(this).get(BookViewModel::class.java)
        commentViewModel = ViewModelProvider(this).get(CommentViewModel::class.java)
        viewHistoryViewModel = ViewModelProvider(this).get(ViewHistoryViewModel::class.java)

        recyclerView = findViewById(R.id.detailCommentRecyclerView)
        CommentRepository().getCommentByBookID(
            edu.bluejack22_2.Glisjoie.Model.BookModel(
                "",
                "",
                bookId,
                "",
                "",
                "",
                null,
                null,
                null,
                null,
                ""
            )
        ){ resCall ->
            for(i in 0 until resCall.size){ Log.d("GOLDWASSER", "onCreate: "+ resCall.get(i).description) }
            arr = resCall
            adapter = CommentAdapter(R.layout.item_comment, mutableListOf());
            recyclerView.layoutManager = LinearLayoutManager(this@BookDetailActivity)
            recyclerView.adapter = adapter;
            adapter.updateData(arr)
        };

        bookCommentIcon = findViewById(R.id.detailCommentPicture)
        Picasso.get().load(R.drawable.logoglisjoie).into(bookCommentIcon)

        editBookButton = findViewById(R.id.editButton)

        var authorId = intent.getStringExtra("authorID")
        userViewModel.getCurrUser() { it ->
            currUser = it

            viewHistoryViewModel.updateHistory(currUser.userDocumentID, bookId as String)

            if (currUser.userDocumentID == authorId) {
                Log.e("bookDetail1", "authorIDIntent: " + authorId)
                Log.e("bookDetail1", "authorID: " + currUser.userDocumentID)
                editBookButton.visibility = View.VISIBLE
            } else {
                editBookButton.visibility = View.INVISIBLE
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

        editBookButton.setOnClickListener {
            val intent = Intent(this, EditBooksActivity::class.java).putExtra("bookID", bookId)
            startActivity(intent)
        }

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

        bookViewModel.countData.observe(this) {
            val rating = bookViewModel.bookData.value?.rating
            val ratingText = if (rating != null && rating.isNaN()) "0" else rating?.toString() ?: ""

            bookReviewCount.text = "($ratingText)${bookViewModel.countData.value} Reviews"
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

        if (bookId != null) {
            saveToSharedPref(this, "bookId", bookId)
        }

        bookViewModel.getBookByID(bookId)



    }

}