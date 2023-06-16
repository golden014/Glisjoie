package com.bluecactus.glisjoie.View

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModelProvider
import com.bluecactus.glisjoie.R
import com.bluecactus.glisjoie.ViewModel.LoginViewModel
import com.bluecactus.glisjoie.ViewModel.UserViewModel

class LoginActivity : AppCompatActivity() {


    private lateinit var login_button: Button
    private lateinit var error_text: TextView

//    val error_text = findViewById<TextView>(R.id.error_message)

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_login)

            login_button = findViewById<Button>(R.id.login_button)
            error_text = findViewById<TextView>(R.id.error_message)
            error_text.text = " "


            val loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
            val userViewModel = ViewModelProvider((this)).get(UserViewModel::class.java)
//            ViewModelProvider((this.applicationContext as YourApplicationClass)).get(UserViewModel::class.java)

//            userViewModel.currUser.value?.let { Log.e("tes curr user", it.userDocumentID) }


            login_button.setOnClickListener {
                val email_text= findViewById<EditText>(R.id.textEmail).text.toString()
                val password_text = findViewById<EditText>(R.id.passwordField).text.toString()

                loginViewModel.loginValidation(this, email_text, password_text) { result ->
                    if (result == "Success") {
//                        Log.e("loginValidation", "auth success")
                        val currUser = loginViewModel.bindUserInfo(email_text) {result ->

                            userViewModel.updateCurrUser(result)
                            userViewModel.currUser.observe(this) { user ->
                                if (user != null) {
                                    if (user.status == "Banned") {
                                        error_text.text = "You are banned"

                                    } else if (user.status == "Active"){
                                        Log.e("ban", user.userDocumentID + " " + user.status)
//                                        error_text.text = user.userDocumentID ?: "No user"
                                        triggerNotification()
                                        val intent = Intent(this, HomeActivity::class.java)
                                        startActivity(intent)
                                    }
                                }


                            }
                        }
//                        error_text.text = userViewModel.currUser.value?.userDocumentID ?: "asdasdad"



                    } else {
                        error_text.text = "Wrong credential"
                    }

                }

            }


            val button = findViewById<Button>(R.id.register_button)
            button.setOnClickListener {
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            }
    }

    private fun triggerNotification() {
//        val context = this

//        context?.let {
            // Create a notification channel for Android Oreo and later
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = "Login Success !"
                val description = "Enjoy our app !"
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel("notifyId", name, importance).apply {
                    this.description = description
                }

                // Register the channel with the system
                val notificationManager = getSystemService(NotificationManager::class.java)
                notificationManager?.createNotificationChannel(channel)
//            }
        Log.e("Notification", "Notification channel created") // Log after channel is created


        // Create a notification
            val builder = NotificationCompat.Builder(this, "notifyId")
                .setSmallIcon(R.drawable.ic_baseline_menu_book_24) // Set the icon
                .setContentTitle("Login Successful") // Set the title
                .setContentText("Welcome to Glisjoie!") // Set the text
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            // Get the NotificationManager
            val notificationManagerCompat = NotificationManagerCompat.from(this)

            // Send the notification
//            notificationManagerCompat.notify(1, builder.build())
        try {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notificationManagerCompat.notify(1, builder.build())
            Log.d("Notification", "Notification sent") // Log after notification is sent
        } catch (e: Exception) {
            Log.e("Notification", "Failed to send notification", e) // Log if an exception is thrown
        }
//        }
    }


}