package com.bluecactus.glisjoie.ViewModel

import androidx.lifecycle.ViewModel
import com.bluecactus.glisjoie.Model.ViewHistoryModel
import com.bluecactus.glisjoie.Repository.HistoryRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter

//view model utk view history
class ViewHistoryViewModel : ViewModel() {
    private val viewHistoryRepository= HistoryRepository()

    fun getViewHistory(userID: String, callback: (Array<ViewHistoryModel>) -> Unit) {
        viewHistoryRepository.getAllViewHistory(userID, callback)
    }

    fun updateHistory(userID: String, bookID: String) {
        val historyRepo = HistoryRepository()
        historyRepo.updateViewHistory(userID, bookID)
    }

    fun filterRecentDates(day: Int ,objects: Array<ViewHistoryModel>): List<ViewHistoryModel> {
        val formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss 'GMT'xxx uuuu")
        val today = LocalDate.now()
        val fiveDaysAgo = today.minusDays(day.toLong())

        return objects.filter { obj ->
            val date = LocalDate.parse(obj.date, formatter)
            date.isAfter(fiveDaysAgo) && !date.isAfter(today)
        }
    }
}