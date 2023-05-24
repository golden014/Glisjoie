package com.bluecactus.glisjoie.View.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bluecactus.glisjoie.R
import com.bluecactus.glisjoie.View.HomeActivity
import com.bluecactus.glisjoie.ViewModel.HomeViewModel
import com.bluecactus.glisjoie.ViewModel.UserViewModel
import org.w3c.dom.Text

class ProfileActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var pagerAdapter: PagerAdapter
    private lateinit var userEmail: TextView
    private lateinit var userName: TextView
    private lateinit var settingIcon: ImageView
    private lateinit var totalReview: TextView
    private lateinit var totalBook: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        viewPager = findViewById(R.id.profileViewPager)
        settingIcon = findViewById(R.id.settingIcon)

        pagerAdapter = PagerAdapter(this)
        viewPager.adapter = pagerAdapter

        userEmail = findViewById(R.id.profileEmail)
        userName = findViewById(R.id.profileUsername)

        UserViewModel().getCurrUser { user ->
            userEmail.text = user.email
            userName.text = user.username
        }

        UserViewModel().getCurrUser { user ->



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
}