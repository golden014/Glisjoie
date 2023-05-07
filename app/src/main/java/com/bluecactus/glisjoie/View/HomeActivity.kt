package com.bluecactus.glisjoie.View

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bluecactus.glisjoie.Model.BookPreviewModel
import com.bluecactus.glisjoie.R
import com.bluecactus.glisjoie.ViewModel.HomeViewModel
import com.google.firebase.firestore.DocumentSnapshot

class HomeActivity:AppCompatActivity() {

    lateinit var listView: ListView
    var names = arrayOf("josua", "jevon", "aaaa")
    lateinit var arrayAdapter:ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        listView = findViewById(R.id.list_view)
        
        val books: Any = homeViewModel.getTopBooks { bookPreviewModels ->
            if (bookPreviewModels.size > 0) {
                Log.e("home activity", bookPreviewModels[0].title)
            } else {
                Log.e("home activity", "size 0")
            }


            arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, createBooksFromUnits(bookPreviewModels))
            listView.setAdapter(arrayAdapter)
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
//            val book = BookPreviewModel(
//                title = "Some Title",
//                authorName = "Some Author"
//            )
                if (unit is BookPreviewModel) {
                    val title = unit.title
                    val authorName = unit.authorName
                    Log.e("debug", unit.title)

                    // Create BookPreviewModel object and add to list
                    val book = BookPreviewModel(title = title, authorName = authorName)

                    books.add(unit.title)
                }
        }

        }
        return books
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu, menu)

        val menuItem: MenuItem? = menu?.findItem(R.id.action_search)
        if (menuItem != null) {
            val searchView: SearchView? = menuItem?.actionView as? SearchView
            if (searchView != null) {
                searchView.queryHint = "Search here"
                (object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        // This method is called when the user submits the search query
                        // Handle the search query here
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        // This method is called when the user changes the search query text
                        // Handle the search query text change here
                        //masukin function search disini
                        arrayAdapter.filter.filter(newText)


                        return false
                    }
                })
            }
        }
        return true


        return super.onCreateOptionsMenu(menu)
    }
    

}