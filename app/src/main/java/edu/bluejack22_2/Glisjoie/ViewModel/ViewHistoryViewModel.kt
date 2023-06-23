package edu.bluejack22_2.Glisjoie.ViewModel

import androidx.lifecycle.ViewModel
import edu.bluejack22_2.Glisjoie.Model.ViewHistoryModel
import edu.bluejack22_2.Glisjoie.Repository.HistoryRepository
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

    //delete filtered
    fun deleteFiltered(userID: String, day: Int,callback: (Int) -> Unit) {
        //ambil semua
        viewHistoryRepository.getAllViewHistory(userID) { historyArray ->

            //filter by date, return array
            var filtered = filterRecentDates(day, historyArray)

            //extract docID dari array of objects
            var extractedFields: MutableList<String> = arrayListOf()

            for (obj in filtered) {
                extractedFields.add(obj.documentID)
            }
            //delete semua document yg match dgn isi array (docsID)
            viewHistoryRepository.deleteFiltered(userID, extractedFields.toTypedArray()) {
                callback(it)
            }
        }
    }

    fun deleteAllHistory(userID: String, callback: (Int) -> Unit) {
        viewHistoryRepository.deleteAllHistory(userID) {
            callback(it)
        }
    }

    //ambil total history dari user
    fun getTotalHistory(userID: String, callback: (Int) -> Unit) {
        viewHistoryRepository.getTotalHistory(userID) {
            callback(it)
        }
    }


}