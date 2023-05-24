package com.bluecactus.glisjoie.Repository

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FollowRepository {

    val db = Firebase.firestore

    val usersRef = db.collection("users")

    fun addFollowing(currUserID: String, targetUserID: String, callback: (Int) -> Unit) {
        val following = hashMapOf(
            "userID" to targetUserID
        )

        val follower = hashMapOf(
            "userID" to currUserID
        )

        //masukin target user ke collection following curr user
        usersRef
            .document(currUserID)
            .collection("following")
            .add(following)
            .addOnSuccessListener {
                // masukin curr user ke target user's follower
                usersRef
                    .document(targetUserID)
                    .collection("followers")
                    .add(follower)
                    .addOnSuccessListener {
                        callback(200)
                    }
                    .addOnFailureListener{
                        callback(500)
                    }
            }
            .addOnFailureListener{
                callback(500)
            }


    }


    fun unFollow(currUserID: String, targetUserID: String, callback: (Int) -> Unit) {
        //delete dari following nya curr user
        usersRef
            .document(currUserID)
            .collection("following")
            .whereEqualTo("userID", targetUserID)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (doc in querySnapshot.documents) {
                    doc.reference.delete()
                        .addOnSuccessListener {

                            //delete dari followernya target user
                            usersRef
                                .document(targetUserID)
                                .collection("followers")
                                .whereEqualTo("userID", currUserID)
                                .get()
                                .addOnSuccessListener { querySnapshot1 ->
                                    for (doc1 in querySnapshot1.documents) {
                                        doc1.reference.delete()
                                            .addOnSuccessListener {
                                                callback(200)
                                            }
                                            .addOnFailureListener{
                                                callback(500)
                                            }
                                    }

                                }
                        }
                        .addOnFailureListener {
                            callback(500)
                        }
                }

            }

    }

    //return true kalau sudah di follow,
    //return false kalau blm di follow
    fun checkFollowed(currUserID: String, targetUserID: String, callback: (Boolean) -> Unit) {
        usersRef
            .document(currUserID)
            .collection("following")
            .whereEqualTo("userID", targetUserID)
            .limit(1)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val docExist = !querySnapshot.isEmpty
                if (docExist) {
                    callback(true)
                } else {
                    callback(false)
                }
            }
            .addOnFailureListener{
                callback(false)
            }
    }

}