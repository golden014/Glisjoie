package com.bluecactus.glisjoie.Repository

import android.util.Log
import com.bluecactus.glisjoie.Model.BookPreviewModel
import com.bluecactus.glisjoie.Model.ViewHistoryModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HistoryRepository {
    val db = Firebase.firestore
    val usersCollection = db.collection("users")
    val auth = Firebase.auth


    fun updateViewHistory(userID: String, bookID: String) {
        val viewHistoryRef =
            db.collection("users")
                .document(userID)
                .collection("viewHistory")
        val query = viewHistoryRef.whereEqualTo("bookID", bookID)
        query.get().addOnSuccessListener { querySnapshot ->
            if (querySnapshot.isEmpty) {
                // belum pernah liat buku ini, jadi buat document baru dengan
                // bookID yg dikasih
                val viewHistory = hashMapOf(
                    "bookID" to bookID,
                    "date" to FieldValue.serverTimestamp(), //use server time stamp
                    "isDeleted" to "false"
                )
                viewHistoryRef
                    .add(viewHistory)
                    .addOnSuccessListener { documentReference ->
                        Log.d("viewHistory", "DocumentSnapshot added with ID: ${documentReference.id}")
                    }
                    .addOnFailureListener { e ->
                        Log.w("viewHistory", "Error adding document", e)
                    }

            } else {
                // sudah pernah liat buku ini, jadi update date nya jadi date
                // sekarang
                val docID = querySnapshot.documents[0].id

                viewHistoryRef
                    .document(docID)
                    .update("date", FieldValue.serverTimestamp()) // use server timestamp
                    .addOnSuccessListener {
                        Log.d("viewHistoryUpdate", "DocumentSnapshot successfully updated!")
                    }
                    .addOnFailureListener { e ->
                        Log.w("viewHistoryUpdate", "Error updating document", e)
                    }

                val status = querySnapshot.documents[0].getString("isDeleted")

                //kalau udah dihapus, kan wktu dilihat lagi pasti masuk lagi ke view history
                //jadi cek kalau dia udah dihapus atau engga, kalau iya, ganti jadi false isDeleted nya
                if (status == "true") {
                    viewHistoryRef
                        .document(docID)
                        .update("isDeleted", "false") // use server timestamp
                        .addOnSuccessListener {
                            Log.d("viewHistoryUpdate", "DocumentSnapshot successfully updated!")
                        }
                        .addOnFailureListener { e ->
                            Log.w("viewHistoryUpdate", "Error updating document", e)
                        }
                }
            }
        }
    }

    fun getAllViewHistory(userID: String, callback: (Array<ViewHistoryModel>) -> Unit) {
        val booksHistory = mutableListOf<ViewHistoryModel>()

        val viewHistoryRef =
            db.collection("users")
                .document(userID)
                .collection("viewHistory")

        viewHistoryRef.whereEqualTo("isDeleted", "false").get().addOnSuccessListener { querySnapshot ->
            for (doc in querySnapshot.documents) {
                val bookID = doc.getString("bookID")
                val date = doc.getDate("date").toString()
                var cover: String
                var bookTitle: String

                db.collection("books")
                    .document(bookID as String)
                    .get()
                    .addOnSuccessListener{ bookDoc ->
                        bookTitle = bookDoc.getString("bookTitle").toString()

                        if (bookTitle != "null") {
                            cover = bookDoc.getString("imageLink").toString()

                            booksHistory.add(ViewHistoryModel(bookTitle, cover, date, doc.id))
                            callback(booksHistory.toTypedArray())
                        }
                    }
            }
        }
    }


    fun searchViewHistory(userID: String, subString: String, callback: (Array<ViewHistoryModel>) -> Unit) {
        val booksHistory = mutableListOf<ViewHistoryModel>()

        val viewHistoryRef =
            db.collection("users")
                .document(userID)
                .collection("viewHistory")

        viewHistoryRef.whereEqualTo("isDeleted", "false").get().addOnSuccessListener { querySnapshot ->
            for (doc in querySnapshot.documents) {
                val bookID = doc.getString("bookID")
                val date = doc.getDate("date").toString()
                var cover: String
                var bookTitle: String

                db.collection("books")
                    .document(bookID as String)
                    .get()
                    .addOnSuccessListener{ bookDoc ->

                        bookTitle = bookDoc.getString("bookTitle").toString()
                        //kalau misalnya title nya itu match dengan substring nya
                        if (bookTitle.contains(subString)) {
                            cover = bookDoc.getString("imageLink").toString()
                            booksHistory.add(ViewHistoryModel(bookTitle, cover, date, doc.id))
                            callback(booksHistory.toTypedArray())
                        }

                    }
            }

            if (booksHistory.isEmpty()) {
                callback(emptyArray())
            }
        }
    }

    //utk yg filtered
    //ambil filtered dlu, for each elemen yg di filtered ganti status isDeleted
    fun deleteFiltered(
        userID: String,
        historyIDs:
        Array<String>,
        callback: (Int) -> Unit
    ) {

        // perlu mutable list wktu pake where in nya
        val temp: MutableList<String>
        temp = historyIDs.toMutableList()

        val viewHistoryRef =
            db.collection("users")
                .document(userID)
                .collection("viewHistory")

        viewHistoryRef
            .whereIn(FieldPath.documentId(), temp)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val batch = db.batch()
                for (doc in querySnapshot.documents) {
                    val documentRef = doc.reference
                    batch.update(documentRef, "isDeleted", "true")
                }

                batch.commit()
                    .addOnSuccessListener {
                        callback(200)
                    }
                    .addOnFailureListener {
                        callback(500)
                    }
            }
            .addOnFailureListener {
                callback(500)
            }
    }

    //delete single
    fun deleteSingleViewHistory(userID: String, viewHistoryID: String, callback: (Int) -> Unit) {
        val viewHistoryRef =
            db.collection("users")
                .document(userID)
                .collection("viewHistory")

        viewHistoryRef
            .document(viewHistoryID)
            .update("isDeleted", "true")
            .addOnSuccessListener {
                callback(200)
            }
            .addOnFailureListener{
                callback(500)
            }
    }

    fun deleteAllHistory(userID: String, callback: (Int) -> Unit) {
        val viewHistoryRef =
            db.collection("users")
                .document(userID)
                .collection("viewHistory")

        viewHistoryRef.get().addOnSuccessListener { querySnapshot ->
            for (doc in querySnapshot.documents) {
                val docRef = doc.reference
                docRef.update("isDeleted", "true")
                    .addOnSuccessListener {
                        callback(200)
                    }
                    .addOnFailureListener {
                        callback(500)
                    }
            }
        }
    }

    fun getTotalHistory(userID: String, callback: (Int) -> Unit) {
        val booksHistory = mutableListOf<ViewHistoryModel>()

        val viewHistoryRef =
            db.collection("users")
                .document(userID)
                .collection("viewHistory")

        viewHistoryRef
            .get()
            .addOnSuccessListener {
                val totalDocs = it.size()
                callback(totalDocs)
            }
            .addOnFailureListener {
                callback(0)
            }


    }

}