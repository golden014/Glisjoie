package edu.bluejack22_2.Glisjoie.ViewModel

import android.app.Activity
import android.widget.Button
import androidx.lifecycle.ViewModel

class ReturnButtonViewModel: ViewModel() {

    fun onBackButtonClicked(button: Button, activity: Activity) {
        button.setOnClickListener {
            activity.finish()
        }
    }
}