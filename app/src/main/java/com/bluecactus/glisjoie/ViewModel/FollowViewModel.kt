package com.bluecactus.glisjoie.ViewModel

import androidx.lifecycle.ViewModel
import com.bluecactus.glisjoie.Repository.FollowRepository

class FollowViewModel: ViewModel() {
    private val followRepo = FollowRepository()

    fun addFollowing(currUserID: String, targetUserID: String, callback: (Int) -> Unit) {
       followRepo.addFollowing(currUserID, targetUserID) {
           callback(it)
       }
    }

    fun unFollow(currUserID: String, targetUserID: String, callback: (Int) -> Unit) {
        followRepo.unFollow(currUserID, targetUserID) {
            callback(it)
        }
    }
}