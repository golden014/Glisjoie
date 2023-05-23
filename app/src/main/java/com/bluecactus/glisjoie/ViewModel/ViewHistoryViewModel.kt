package com.bluecactus.glisjoie.ViewModel

import androidx.lifecycle.ViewModel
import com.bluecactus.glisjoie.Repository.HistoryRepository

//view model utk view history
class ViewHistoryViewModel : ViewModel() {


    fun updateHistory(userID: String, bookID: String) {
        val historyRepo = HistoryRepository()

        historyRepo.updateViewHistory(userID, bookID)
    }
}