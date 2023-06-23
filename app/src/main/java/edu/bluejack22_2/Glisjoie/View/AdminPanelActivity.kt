package edu.bluejack22_2.Glisjoie.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack22_2.Glisjoie.ViewModel.AdminViewModel
import edu.bluejack22_2.Glisjoie.ViewModel.UserProfileAdapter
import edu.bluejack22_2.glisjoie.R

class AdminPanelActivity : AppCompatActivity() {

    lateinit var adminViewModel: AdminViewModel

    lateinit var recyclerView: RecyclerView
    lateinit var adapter: UserProfileAdapter

    lateinit var searchText: EditText
    lateinit var searchButton: ImageButton
    lateinit var searchMessage: TextView

    lateinit var bannedFilter: Button
    lateinit var activeFilter: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_panel)

        //gkepake


        searchText = findViewById(R.id.search_user_admin_page)
        searchButton = findViewById(R.id.search_button_admin)
        searchMessage = findViewById(R.id.search_message_admin_page)
        bannedFilter = findViewById(R.id.filter_banned_button)
        activeFilter = findViewById(R.id.filter_active_button)

        adminViewModel = ViewModelProvider(this).get(AdminViewModel::class.java)

        adminViewModel.getAllCustomer { users ->
            recyclerView = findViewById(R.id.admin_recycler_view)
            recyclerView.layoutManager = LinearLayoutManager(this)


            adapter = UserProfileAdapter(
                R.layout.list_item_user,
                users.toList(),
                this
            )
            recyclerView.adapter = adapter
        }

        searchButton.setOnClickListener {
            val subString = searchText.text.toString()
            adminViewModel.searchCustomer(subString) { usersArray ->
                if (usersArray.isEmpty()) {
                    Log.e("searchViewHistory", "array empty, clear data should be executed")
                    adapter.clearData()
                    searchMessage.text = "No user found !"
                } else {
                    adapter.updateData(usersArray.toList())
                    searchMessage.text = ""
                }
            }
        }

        bannedFilter.setOnClickListener {
            adminViewModel.filterStatusUser("Banned") { usersArray ->
                adapter.updateData(usersArray.toList())
            }
        }

        activeFilter.setOnClickListener {
            adminViewModel.filterStatusUser("Active") { usersArray ->
                adapter.updateData(usersArray.toList())
            }
        }
    }
}