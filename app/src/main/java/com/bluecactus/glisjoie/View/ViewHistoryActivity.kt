package com.bluecactus.glisjoie.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bluecactus.glisjoie.Model.UserModel
import com.bluecactus.glisjoie.R
import com.bluecactus.glisjoie.ViewModel.UserViewModel
import com.bluecactus.glisjoie.ViewModel.ViewHistoryAdapter
import com.bluecactus.glisjoie.ViewModel.ViewHistoryViewModel

class ViewHistoryActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: ViewHistoryAdapter
    lateinit var historyViewModel: ViewHistoryViewModel
    lateinit var userViewModel: UserViewModel
    lateinit var currUser: UserModel
    lateinit var searchText: EditText
    lateinit var searchButton: ImageButton
    lateinit var searchMessage: TextView

//    //filter menu
//    lateinit var todayHistory:

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_history)

        val toolbar: Toolbar = findViewById(R.id.toolbar_history_page)
        setSupportActionBar(toolbar)

        searchText = findViewById(R.id.search_text_view_history)
        searchButton = findViewById(R.id.search_button_view_history)
        searchMessage = findViewById(R.id.search_message_view_history)

        historyViewModel = ViewModelProvider(this).get(ViewHistoryViewModel::class.java)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        Log.e("viewHistory", "masuk1")
        userViewModel.getCurrUser { it ->
            currUser = it
            Log.e("viewHistory", currUser.username)

            historyViewModel.getViewHistory(currUser.userDocumentID) {historyArray ->
                recyclerView = findViewById(R.id.history_recycler_view)
                recyclerView.layoutManager = LinearLayoutManager(this)


                adapter = ViewHistoryAdapter(
                    R.layout.list_item_history_view,
                    historyArray.toList(),
                    this
                )
                recyclerView.adapter = adapter
                if (historyArray.isEmpty()) {
                    Log.e("viewHistory", "empty")
                } else {
                    Log.e("viewHistory", historyArray[0].title)
                }

            }
        }


        searchButton.setOnClickListener {
            val subString = searchText.text.toString()
            historyViewModel.searchViewHistory(currUser.userDocumentID, subString) { historyArray ->
                if (historyArray.isEmpty()) {
                    Log.e("searchViewHistory", "array empty, clear data should be executed")
                    adapter.clearData()
                    searchMessage.text = "No books found !"
                } else {
                    adapter.updateData(historyArray.toList())
                    searchMessage.text = ""
                }
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_view_history, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        //saat menu yg filter diklik
        when (item.itemId) {
            R.id.view_today_history -> {
                historyViewModel.getFilteredHistory(currUser.userDocumentID, 1) {historyArray ->
                    adapter.updateData(historyArray.toList())
                }
                searchMessage.text = ""
            }
            R.id.view_last_5_days -> {
                historyViewModel.getFilteredHistory(currUser.userDocumentID, 5) {historyArray ->
                    adapter.updateData(historyArray.toList())
                }
                searchMessage.text = ""
            }
            R.id.view_1_week -> {
                historyViewModel.getFilteredHistory(currUser.userDocumentID, 7) {historyArray ->
                    adapter.updateData(historyArray.toList())
                }
                searchMessage.text = ""
            }
            R.id.view_3_week -> {
                historyViewModel.getFilteredHistory(currUser.userDocumentID, 21) {historyArray ->
                    adapter.updateData(historyArray.toList())
                }
                searchMessage.text = ""
            }
        }

        return super.onOptionsItemSelected(item)
    }
}