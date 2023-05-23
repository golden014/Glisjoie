package com.bluecactus.glisjoie.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bluecactus.glisjoie.Model.UserModel
import com.bluecactus.glisjoie.Model.ViewHistoryModel
import com.bluecactus.glisjoie.R
import com.bluecactus.glisjoie.ViewModel.BookPreviewAdapter
import com.bluecactus.glisjoie.ViewModel.UserViewModel
import com.bluecactus.glisjoie.ViewModel.ViewHistoryAdapter
import com.bluecactus.glisjoie.ViewModel.ViewHistoryViewModel

class ViewHistoryActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: ViewHistoryAdapter
    lateinit var historyViewModel: ViewHistoryViewModel
    lateinit var userViewModel: UserViewModel
    lateinit var currUser: UserModel

//    //filter menu
//    lateinit var todayHistory:

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_history)

        val toolbar: Toolbar = findViewById(R.id.toolbar_history_page)
        setSupportActionBar(toolbar)

        historyViewModel = ViewModelProvider(this).get(ViewHistoryViewModel::class.java)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        Log.e("viewHistory", "masuk1")
        userViewModel.getCurrUser { it ->
            currUser = it
            Log.e("viewHistory", currUser.username)

            historyViewModel.getViewHistory(currUser.userDocumentID) {historyArray ->
                recyclerView = findViewById(R.id.history_recycler_view)
                recyclerView.layoutManager = LinearLayoutManager(this)


                adapter = ViewHistoryAdapter(R.layout.list_item_history_view, historyArray.toList())
                recyclerView.adapter = adapter
                if (historyArray.isEmpty()) {
                    Log.e("viewHistory", "empty")
                } else {
                    Log.e("viewHistory", historyArray[0].title)
                }

//            historyViewModel.getFilteredHistory(currUser.userDocumentID, 1) { historyArray ->
//                recyclerView = findViewById(R.id.history_recycler_view)
//                recyclerView.layoutManager = LinearLayoutManager(this)
//
//                adapter = ViewHistoryAdapter(R.layout.list_item_history_view, historyArray.toList())
//                recyclerView.adapter = adapter


//                adapter.updateData(historyArray.toList())

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
            }
            R.id.view_last_5_days -> {
                historyViewModel.getFilteredHistory(currUser.userDocumentID, 5) {historyArray ->
                    adapter.updateData(historyArray.toList())
                }
            }
            R.id.view_1_week -> {
                historyViewModel.getFilteredHistory(currUser.userDocumentID, 7) {historyArray ->
                    adapter.updateData(historyArray.toList())
                }
            }
            R.id.view_3_week -> {
                historyViewModel.getFilteredHistory(currUser.userDocumentID, 21) {historyArray ->
                    adapter.updateData(historyArray.toList())
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }
}