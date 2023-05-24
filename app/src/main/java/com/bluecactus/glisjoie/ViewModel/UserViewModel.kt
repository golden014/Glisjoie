package com.bluecactus.glisjoie.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bluecactus.glisjoie.Model.UserModel
import com.bluecactus.glisjoie.Repository.BookRepository
import com.bluecactus.glisjoie.Repository.UserRepository
import javax.security.auth.callback.Callback

class UserViewModel: ViewModel() {

    private val _currUser = MutableLiveData<UserModel?>()
    val currUser: LiveData<UserModel?> = _currUser
    val userRepo = UserRepository()

    fun updateCurrUser(user: UserModel) {
        _currUser.postValue(user)
    }
//    lateinit var currUser: UserModel
//
//    fun updateCurrUser(user: UserModel) {
//        currUser = user
//    }



    fun getCurrUser(callback: (result: UserModel) -> Unit) {
        userRepo.getCurrUser() { user ->
            callback(user)
        }
    }

    fun getUserByID(userID: String, callback: (UserModel) -> Unit) {
        userRepo.getUserByID(userID) { user->
            callback(user!!)
        }
    }

    fun updateEmail(newEmail: String, callback: (Int) -> Unit) {
        userRepo.updateEmail(newEmail) {
            callback(it)
        }
    }

    fun updatePassword(currPassword: String, newPassword: String, confNewPassword: String,callback: (Int) -> Unit) {
        if (newPassword != confNewPassword) {
            callback(400)
        } else {
            userRepo.updatePassword(currPassword, newPassword) {
                callback(it)
            }
        }

    }

    fun actionBanUser(userID: String, newStatus: String, callback: (Int) -> Unit) {
        userRepo.actionBanUser(userID, newStatus) {
            callback(it)
        }
    }

}