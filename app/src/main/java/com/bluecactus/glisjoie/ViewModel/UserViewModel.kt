package com.bluecactus.glisjoie.ViewModel

import androidx.lifecycle.ViewModel
import com.bluecactus.glisjoie.Model.UserModel

class UserViewModel: ViewModel() {
    var currUser: UserModel? = null

    fun updateCurrUser(user: UserModel) {
        currUser = user
    }
}