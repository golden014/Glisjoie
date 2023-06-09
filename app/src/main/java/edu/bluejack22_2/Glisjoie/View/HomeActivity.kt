package edu.bluejack22_2.Glisjoie.View

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import edu.bluejack22_2.Glisjoie.Model.BookPreviewModel
import edu.bluejack22_2.Glisjoie.Model.UserModel
import edu.bluejack22_2.Glisjoie.Repository.UserRepository
import edu.bluejack22_2.Glisjoie.View.books.CreateBookFragment
import edu.bluejack22_2.Glisjoie.View.profile.ProfileActivity
import edu.bluejack22_2.Glisjoie.ViewModel.BookPreviewAdapter
import edu.bluejack22_2.Glisjoie.ViewModel.HomeViewModel
import edu.bluejack22_2.Glisjoie.ViewModel.SearchViewModel
import edu.bluejack22_2.glisjoie.R

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
    lateinit var homeViewModel: HomeViewModel
    lateinit var botNavView: BottomNavigationView
    val homeFragment: HomeFragment = HomeFragment()
    val createBookFragment: CreateBookFragment = CreateBookFragment();
    val profileFragment: ProfileFragment = ProfileFragment()
    val settingsFragment: SettingsFragment = SettingsFragment()
    val viewHistoryFragment: ViewHistoryFragment = ViewHistoryFragment()
    lateinit var currentUser: UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java);
        botNavView = findViewById(R.id.bottom_nav)

        val auth = Firebase.auth

        UserRepository().getCurrUser { user ->
            currentUser = user
        }

        //push user ke login kalau blm login
        if (auth.currentUser == null) {
            val intent = Intent(this@HomeActivity, LoginActivity::class.java)
            startActivity(intent)
        }



//        val tempImage = "https://firebasestorage.googleapis.com/v0/b/glisjoie.appspot.com/o/images%2F74006291-a2e4-496a-8afd-b6860c9d0511.jpg?alt=media&token=06955569-cd08-49be-ab36-32cdfd879296"
//
//        val imageView: ImageView = findViewById(R.id.center_image)
//        Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/glisjoie.appspot.com/o/images%2F74006291-a2e4-496a-8afd-b6860c9d0511.jpg?alt=media&token=06955569-cd08-49be-ab36-32cdfd879296")
//            .into(imageView)

//        Picasso.get()
//            .load(tempImage)
//            .centerCrop()
//            .resize(24, 24)
//            .into(object : com.squareup.picasso.Target {
//                override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {
////                    val drawable = BitmapDrawable(resources, bitmap)
////                    drawable.setTintList(null) // remove tint
////                    botNavView.menu.findItem(R.id.profile_nav).icon = drawable
//                    val drawable = BitmapDrawable(resources, bitmap)
//                    drawable.setTintList(null) // remove tint here
//                    val menuIcon = botNavView.menu.findItem(R.id.profile_nav)
//                    menuIcon.icon = drawable
//                    (menuIcon.icon as BitmapDrawable).setTintList(null) // and remove tint here
//                }
//
//                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
//                    // handle failure here
//                }
//
//                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
//                    // you can use a placeholder here
//                }
//            })

        supportFragmentManager.beginTransaction().replace(R.id.container_nav, homeFragment).commit()

        if(intent.getStringExtra("Fragment").equals("Setting")){
            supportFragmentManager.beginTransaction().replace(R.id.container_nav, settingsFragment).commit()
        }

        botNavView.setOnItemSelectedListener( object: NavigationBarView.OnItemSelectedListener{
            override fun onNavigationItemSelected(item: MenuItem): Boolean {

                when (item.itemId) {
                    R.id.home_nav -> {
                        supportFragmentManager.beginTransaction().replace(R.id.container_nav, homeFragment).commit()
                        return true
                    }
                    R.id.add_book_nav -> {
                        supportFragmentManager.beginTransaction().replace(R.id.container_nav, createBookFragment).commit()
                        return true
                    }
                    R.id.profile_nav -> {
//                        supportFragmentManager.beginTransaction().replace(R.id.container_nav, viewHistoryFragment).commit()
                        val intent = Intent(this@HomeActivity, ProfileActivity::class.java).putExtra("userId", currentUser.userDocumentID)
                        startActivity(intent)
                        finish()
                        return true
                    }
                    R.id.settings_nav -> {
                        supportFragmentManager.beginTransaction().replace(R.id.container_nav, settingsFragment).commit()
                        return true
                    }
                }
                return false
            }

        })

        if(intent.getBooleanExtra("fromProfile", false)) {
            botNavView.selectedItemId = R.id.home_nav
        }

        message = findViewById<TextView>(R.id.noBooksMessage)
//
////        val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
//        recyclerView = findViewById(R.id.recyclerView)
//        recyclerView.layoutManager = LinearLayoutManager(this)
//
////        listView = findViewById(R.id.list_view)
//
//        homeViewModel.currBooks.observe(this) { bookPreviewModels ->
//            emptyData = false
//            firstData = bookPreviewModels
//            adapter = BookPreviewAdapter(R.layout.list_item_book_preview, bookPreviewModels.toList())
//            recyclerView.adapter = adapter
//        }
//        homeViewModel.getTopBooks { bookPreviewModels ->
//            emptyData = false
//            firstData = bookPreviewModels
//            adapter = BookPreviewAdapter(R.layout.list_item_book_preview, bookPreviewModels.toList())
//            recyclerView.adapter = adapter
//        }


    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu, menu)

//        tes  = findViewById<TextView>(R.id.tes_name);

//        val user = Firebase.auth.currentUser
//        if (user != null) {
//            tes.text = user.email
//        } else {
//            tes.text = "loading"
//        }

//        val tempPicture: MenuItem? = menu?.findItem(R.id.profile_nav)
//        if (tempPicture != null) {
//            val actionView = LayoutInflater.from(this).inflate(R.layout.custom_profile, null)
//            tempPicture.actionView = actionView
//
//            val iconImageView = tempPicture.actionView?.findViewById<ImageView>(R.id.custom_profile_image_view)
//
//            Picasso
//                .get()
//                .load("https://firebasestorage.googleapis.com/v0/b/glisjoie.appspot.com/o/images%2F74006291-a2e4-496a-8afd-b6860c9d0511.jpg?alt=media&token=06955569-cd08-49be-ab36-32cdfd879296")
//                .into(iconImageView)
//        }

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
                        Log.e("homeActivity", "onquerytextsubmit trigger")


                        val searchViewModel = ViewModelProvider(this@HomeActivity).get(
                            SearchViewModel::class.java)
                        recyclerView = findViewById(R.id.recyclerView)
                        recyclerView.layoutManager = LinearLayoutManager(this@HomeActivity)

                        val isEmpty = searchViewModel.performSearch(query.toString()) { bookPreviewModels ->
                            Log.e("isEmptyDebug", "bookPreviewModel start")
                            for (book in bookPreviewModels) {
                                Log.e("performSearchDebug", book.title)
                            }


                            Log.e("awikwok1", bookPreviewModels.size.toString())
                            if (bookPreviewModels.isEmpty()) {
//                                emptyData = true
                                Log.e("awikwok1", "bookPreviewModels is empty")
                                homeViewModel.clearBooks()
                                Log.e("nav_books", "No books found, clear book executed1")
                                message.setText("No books found !")
                            } else {
                                homeViewModel.updateBooks(bookPreviewModels)
                                Log.e("fragment3", "updated ")
                            }
//                            homeViewModel.updateBooks(bookPreviewModels)
//                            adapter.updateData(bookPreviewModels.toList())
                        }

//                        if (emptyData) {
////                            adapter.clearData()
//                            homeViewModel.clearBooks()
//                            Log.e("nav_books", "No books found, clear book executed")
//                            message.setText("No books found !")
//                        }

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
//                    adapter.updateData(firstData.toList())
                    message.text = ""
//                    homeViewModel.currBooks.observe(this@HomeActivity) { bookPreviewModels ->
//                        emptyData = false
//                        firstData = bookPreviewModels
//                        adapter = BookPreviewAdapter(R.layout.list_item_book_preview, bookPreviewModels.toList())
//                        recyclerView.adapter = adapter
//                    }
                    homeViewModel.getTopBooks()
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

                homeViewModel.getTopBooks()
//                adapter.updateData(firstData.toList())
                Log.e("return", "lmaoo")
                onBackPressed() // This will navigate back to the previous activity/fragment in the back stack
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


}