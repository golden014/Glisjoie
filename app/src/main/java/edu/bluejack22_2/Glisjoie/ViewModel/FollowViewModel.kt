package edu.bluejack22_2.Glisjoie.ViewModel

import androidx.lifecycle.ViewModel
import edu.bluejack22_2.Glisjoie.Repository.FollowRepository

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

    //akan return true kalau sudah di follow
    //akan return false kalau blm di follow
    //buat bikin follow button dynamic
    fun checkFollowed(currUserID: String, targetUserID: String, callback: (Boolean) -> Unit) {
        followRepo.checkFollowed(currUserID, targetUserID) {
            callback(it)
        }
    }
}