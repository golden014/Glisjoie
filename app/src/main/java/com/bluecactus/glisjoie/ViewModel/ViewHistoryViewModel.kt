package com.bluecactus.glisjoie.ViewModel

import android.view.View
import androidx.lifecycle.ViewModel
import com.bluecactus.glisjoie.Model.ViewHistoryModel
import com.bluecactus.glisjoie.Repository.HistoryRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter

//view model utk view history
class ViewHistoryViewModel : ViewModel() {
    private val viewHistoryRepository= HistoryRepository()

    fun getViewHistory(userID: String, callback: (Array<ViewHistoryModel>) -> Unit) {
        viewHistoryRepository.getAllViewHistory(userID) { historyArray ->
           callback(sortObjectsByDate(historyArray).toTypedArray())
        }
    }

    fun updateHistory(userID: String, bookID: String) {
        val historyRepo = HistoryRepository()
        historyRepo.updateViewHistory(userID, bookID)
    }

    fun getFilteredHistory(userID: String, day: Int,callback: (Array<ViewHistoryModel>) -> Unit) {
        getViewHistory(userID) { arrayModels ->
            var filtered = filterRecentDates(day, arrayModels)
            filtered = sortObjectsByDate(filtered.toTypedArray())
            callback(filtered.toTypedArray())
        }
    }

    fun filterRecentDates(day: Int ,objects: Array<ViewHistoryModel>): List<ViewHistoryModel> {
        val formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss 'GMT'xxx uuuu")
        val today = LocalDate.now()
        val fiveDaysAgo = today.minusDays(day.toLong())

        return objects.filter { obj ->
            val date = LocalDate.parse(obj.date, formatter)
            date.isAfter(fiveDaysAgo) && !date.isAfter(today)
        }.sortedByDescending { obj ->
            LocalDate.parse(obj.date, formatter)
        }
    }

    //nge sort array nya
    fun sortObjectsByDate(objects: Array<ViewHistoryModel>): List<ViewHistoryModel> {
        val formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss 'GMT'xxx uuuu")

        return objects.sortedByDescending { obj ->
            LocalDate.parse(obj.date, formatter)
        }
    }

    //search
    fun searchViewHistory(userID: String, subString: String,callback: (Array<ViewHistoryModel>) -> Unit) {
        viewHistoryRepository.searchViewHistory(userID, subString) { historyArray ->
            if (historyArray.isNotEmpty()) {
                callback(sortObjectsByDate(historyArray).toTypedArray())
            } else {
                callback(emptyArray())
            }

        }
    }

    fun deleteSingleViewHistory(userID: String, viewHistoryID: String, callback: (Int) -> Unit) {
        viewHistoryRepository.deleteSingleViewHistory(userID, viewHistoryID) {
            callback(it)
        }
    }




}