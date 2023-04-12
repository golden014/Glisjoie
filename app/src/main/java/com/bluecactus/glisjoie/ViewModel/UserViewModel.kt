package com.bluecactus.glisjoie.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bluecactus.glisjoie.Model.UserModel

class UserViewModel: ViewModel() {

    private val _currUser = MutableLiveData<UserModel?>()
    val currUser: LiveData<UserModel?> = _currUser

    fun updateCurrUser(user: UserModel) {
        _currUser.postValue(user)
    }
//    lateinit var currUser: UserModel
//
//    fun updateCurrUser(user: UserModel) {
//        currUser = user
//    }
}