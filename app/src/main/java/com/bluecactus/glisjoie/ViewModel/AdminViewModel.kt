package com.bluecactus.glisjoie.ViewModel

import androidx.lifecycle.ViewModel
import com.bluecactus.glisjoie.Model.UserModel
import com.bluecactus.glisjoie.Repository.UserRepository

class AdminViewModel: ViewModel() {
    private val userRepo = UserRepository()

    fun getAllCustomer(callback: (Array<UserModel>) -> Unit) {
        userRepo.getAllCustomer(callback)
    }

    //search customer
    fun searchCustomer(name: String,callback: (Array<UserModel>) -> Unit) {
        userRepo.searchCustomer(name, callback)
    }

    //filter by status (banned/unbanned)
    fun filterStatusUser(status: String, callback: (Array<UserModel>) -> Unit) {
        userRepo.filterStatusUser(status, callback)
    }

}