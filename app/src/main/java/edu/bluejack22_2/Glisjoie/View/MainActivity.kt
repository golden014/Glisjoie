package edu.bluejack22_2.Glisjoie.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import edu.bluejack22_2.glisjoie.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)


// Add a new document with a generated ID
//        db.collection("users")
//            .add(user)
//            .addOnSuccessListener { documentReference ->
//                Log.d("awikwok", "DocumentSnapshot added with ID: ${documentReference.id}")
//            }
//            .addOnFailureListener { e ->
//                Log.w("no awikwok", "Error adding document", e)
//            }
//        val auth: FirebaseAuth = FirebaseAuth.getInstance()
//        auth.createUserWithEmailAndPassword("josuagolden404@gmail.com", "passwordjosua404")
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    // Sign in success, update UI with the signed-in user's information
//                    Log.d("okeoce", "createUserWithEmail:success")
//                    val user = auth.currentUser
////                    updateUI(user)
//                } else {
//                    // If sign in fails, display a message to the user.
//                    Log.w("okeoce", "createUserWithEmail:failure", task.exception)
////                    Toast.makeText(baseContext, "Authentication failed.",
////                        Toast.LENGTH_SHORT).show()
////                    updateUI(null)
//                }
//            }
//            .addOnFailureListener(this) {
//                Log.e("okeoce", it.localizedMessage)
//            }
//
//        Log.d("tessss", "asdasdasdasds")
    }
}