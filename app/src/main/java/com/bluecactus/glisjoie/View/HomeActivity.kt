package com.bluecactus.glisjoie.View

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bluecactus.glisjoie.Model.BookPreviewModel
import com.bluecactus.glisjoie.R
import com.bluecactus.glisjoie.ViewModel.BookPreviewAdapter
import com.bluecactus.glisjoie.ViewModel.HomeViewModel
import com.bluecactus.glisjoie.ViewModel.SearchViewModel
import com.bluecactus.glisjoie.ViewModel.UserViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text

class HomeActivity:AppCompatActivity() {

    lateinit var listView: ListView
    var names = arrayOf("josua", "jevon", "aaaa")
    lateinit var arrayAdapter:ArrayAdapter<String>
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: BookPreviewAdapter
    lateinit var firstData: Array<BookPreviewModel>
    lateinit var message: TextView
    var emptyData: Boolean = false
    lateinit var tes: TextView;



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        message = findViewById<TextView>(R.id.noBooksMessage)

        val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

//        listView = findViewById(R.id.list_view)
        
        val books: Any = homeViewModel.getTopBooks { bookPreviewModels ->
            emptyData = false
            firstData = bookPreviewModels
            adapter = BookPreviewAdapter(R.layout.list_item_book_preview, bookPreviewModels.toList())
            recyclerView.adapter = adapter
//            arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, createBooksFromUnits(bookPreviewModels))
//            listView.setAdapter(arrayAdapter)
        }


    }

//    private fun <T> ArrayAdapter(homeActivity: HomeActivity, simpleListItem1: Int, books: Array<String>): ArrayAdapter<T> {
//        return ArrayAdapter(homeActivity, simpleListItem1, books)
//    }

//    fun getBookTitles(books: Unit): Array<String> {
//        books.
//    }

    fun createBooksFromUnits(units: Any): MutableList<String> {
        val books = mutableListOf<String>()

        if (units is Array<*>) {
            units.forEach { unit ->
                Log.e("homeActivity 63", "num of units")
                if (unit is BookPreviewModel) {
                    val title = unit.title
                    val authorName = unit.authorName
                    val cover = unit.cover
                    val bookID = unit.bookID
                    Log.e("debug", unit.title)

                    // Create BookPreviewModel object and add to list
                    val book = BookPreviewModel(title = title, authorName = authorName, cover = cover, bookID = unit.bookID)
                    Log.e("homeActivity 70", title)
                    books.add(unit.title)
                }
        }

        }
        for(i in books) {
            Log.e("book", i)
        }
        return books
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu, menu)

        val userViewModel = ViewModelProvider(this@HomeActivity).get(UserViewModel::class.java)

        tes  = findViewById<TextView>(R.id.tes_name);

//        userViewModel.currUser.observe(this) { user ->
//            user?.let {
//                tes.text = it.userDocumentID
//            }
//        }

        val user = Firebase.auth.currentUser
        if (user != null) {
            tes.text = user.email
        } else {
            tes.text = "loading"
        }

        val menuItem: MenuItem? = menu?.findItem(R.id.action_search)
        if (menuItem != null) {
            Log.e("homeactivity", menuItem.toString())
            val searchView: SearchView? = menuItem?.actionView as? SearchView
            if (searchView != null) {
                Log.e("homeactivity", "search view exist")
                searchView.queryHint = "Search here"
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        // This method is called when the use r submits the search query
                        // Handle the search query here

//                        val intent = Intent(this@HomeActivity, LoginActivity::class.java)
//                        intent.putExtra("search_query", query)
//                        startActivity(intent)
                        Log.e("homeActivity", "onquerytextsubmit trigger")


                        val searchViewModel = ViewModelProvider(this@HomeActivity).get(SearchViewModel::class.java)
                        recyclerView = findViewById(R.id.recyclerView)
                        recyclerView.layoutManager = LinearLayoutManager(this@HomeActivity)

                        val isEmpty = searchViewModel.performSearch(query.toString()) { bookPreviewModels ->
                            Log.e("isEmptyDebug", "bookPreviewModel start")
                            for (book in bookPreviewModels) {
                                Log.e("performSearchDebug", book.title)
                            }
//                            recyclerView.adapter = adapter
//                            adapter = BookPreviewAdapter(R.layout.list_item_book_preview, bookPreviewModels.toList())
//                            adapter.updateData(bookPreviewModels)

                            Log.e("awikwok1", bookPreviewModels.size.toString())
                            if (bookPreviewModels.isEmpty()) {
                                emptyData = true
                                Log.e("awikwok1", "bookPreviewModels is empty")

                                message.setText("No books found !")
                            }
                            adapter.updateData(bookPreviewModels.toList())
                        }

                        if (emptyData) {
//                            adapter.updateData(arrayOfNulls<BookPreviewModel>(1).toList() as List<BookPreviewModel>)
                            adapter.clearData()
                            message.setText("No books found !")
                        }




//                        val searchResultsFragment = SearchResultsFragment.newInstance(query ?: "")
//                        supportFragmentManager.beginTransaction()
//                            .replace(R.id.fragment_container, searchResultsFragment)
//                            .addToBackStack(null)
//                            .commit()

                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        // This method is called when the user changes the search query text
                        // Handle the search query text change here
                        //masukin function search disini
//                        arrayAdapter.filter.filter(newText)

                        Log.e("homeactivity", newText.toString())

                        return false
                    }

                })
            }

            else {
                Log.e("homeactivity", "searchView null")
            }
        } else {
            Log.e("homeactivity", "menu item null")
        }
        val searchItem = menu?.findItem(R.id.action_search)

        if (searchItem != null) {
            searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {

                override fun onMenuItemActionExpand(p0: MenuItem): Boolean {
                    return true

                }

                override fun onMenuItemActionCollapse(p0: MenuItem): Boolean {
                    emptyData = false
                    adapter.updateData(firstData.toList())
                    return true
                }
            })
        }




        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // Handle the "up" button press here
                // For example, you can clear the search query, close the search view, or perform other actions as needed


                adapter.updateData(firstData.toList())
                Log.e("return", "lmaoo")
                onBackPressed() // This will navigate back to the previous activity/fragment in the back stack
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


}