package edu.bluejack22_2.Glisjoie.View.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import edu.bluejack22_2.Glisjoie.Model.UserModel
import edu.bluejack22_2.Glisjoie.Repository.CommentRepository
import edu.bluejack22_2.Glisjoie.Repository.HistoryRepository
import edu.bluejack22_2.Glisjoie.View.HomeActivity
import edu.bluejack22_2.Glisjoie.ViewModel.FollowViewModel
import edu.bluejack22_2.Glisjoie.ViewModel.UserViewModel
import edu.bluejack22_2.glisjoie.R

class ProfileActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var pagerAdapter: PagerAdapter
    private lateinit var userEmail: TextView
    private lateinit var userName: TextView
    private lateinit var settingIcon: ImageView
    private lateinit var totalReview: TextView
    private lateinit var totalBook: TextView
    private lateinit var currUser: UserModel
    private lateinit var followButton: Button
    private var isSelf = false

    private lateinit var returnButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        var userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        var followViewModel = ViewModelProvider(this).get(FollowViewModel::class.java)
        var profileID = intent.getStringExtra("userId")

        returnButton = findViewById(R.id.return_button_action_bar)

        returnButton.setOnClickListener() {
            Log.e("return", "oke")
            onBackPressed()
        }



        viewPager = findViewById(R.id.profileViewPager)
        settingIcon = findViewById(R.id.settingIcon)

        followButton = findViewById(R.id.followButton)

        totalReview = findViewById(R.id.totalReviewCounter)
        totalBook = findViewById(R.id.totalVisitCounter)

        totalBook.text = "Book visited: 0"
        totalReview.text = "Book reviewed: 0"

        pagerAdapter = PagerAdapter(this)
        viewPager.adapter = pagerAdapter

        userEmail = findViewById(R.id.profileEmail)
        userName = findViewById(R.id.profileUsername)

        userViewModel.getCurrUser() { it ->
            currUser = it

            Log.d("DASDAWS", "onCreate: ${currUser.userDocumentID} and $profileID")
            isSelf = currUser.userDocumentID.equals(profileID)
            if(currUser != null){
                followButton.setOnClickListener{
                    Log.d("Clicked", "onCreate: ")
                    if(followButton.text == "Unfollow"){
                        followButton.text = "Follow"
                        followViewModel.unFollow (currUser.userDocumentID, profileID!!){};
                    }else{
                        followButton.text = "Unfollow"
                        followViewModel.addFollowing(currUser.userDocumentID, profileID!!){};
                    }
                }

                if(isSelf){
                    followButton.visibility = View.INVISIBLE
                    settingIcon.visibility = View.VISIBLE;
                }else{
                    settingIcon.visibility = View.INVISIBLE
                    followViewModel.checkFollowed(currUser.userDocumentID, profileID!!){isFollowed ->
                        Log.d("ISFOLLOWING", "$isFollowed")
                        if(isFollowed){
                            followButton.text = "Unfollow"
                        }else{
                            followButton.text = "Follow"
                        }
                    }
                }

            }
        }

        UserViewModel().getUserByID(profileID!!) { user ->
            userEmail.text = user.email
            userName.text = user.username
        }

        UserViewModel().getUserByID(profileID!!) { user ->

            HistoryRepository().getTotalHistory(user.userDocumentID){ histories->
                totalBook.text = "Unique Books Visited: ${histories}"
            }

            CommentRepository().getCommentByUserID(user.userDocumentID){ books ->
                totalReview.text = "Books Review: ${books.size}"
            }

        }

        settingIcon.setOnClickListener {
            val intent = Intent(this@ProfileActivity, HomeActivity::class.java).putExtra("Fragment", "Setting")
            startActivity(intent)
        }



    }

    private inner class PagerAdapter(fragmentActivity: FragmentActivity) :
        FragmentStateAdapter(fragmentActivity) {
        override fun getItemCount(): Int {
            return 2
        }

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> ProfileBookFragment()
                1 -> CommentBookFragment()
                else -> throw IllegalArgumentException("Invalid position")
            }
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this@ProfileActivity, HomeActivity::class.java).putExtra("fromProfile", true)
        startActivity(intent)
    }
}