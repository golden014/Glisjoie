package com.bluecactus.glisjoie.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
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
//            error_text.setText("dsfsdfsdf")




            val loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
            val userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

//            userViewModel.currUser.value?.let { Log.e("tes curr user", it.userDocumentID) }


            login_button.setOnClickListener {
                val email_text= findViewById<EditText>(R.id.textEmail).text.toString()
                val password_text = findViewById<EditText>(R.id.passwordField).text.toString()

                loginViewModel.loginValidation(this, email_text, password_text) { result ->
                    if (result == "Success") {
//                        Log.e("loginValidation", "auth success")
                        val currUser = loginViewModel.bindUserInfo(email_text) {result ->
                            userViewModel.updateCurrUser(result)

                            userViewModel.currUser.observe(this, { user ->
//                                Log.e("document dsfdsf", user?.userDocumentID?: "asdasdasd")
                                error_text.setText(user?.userDocumentID ?: "No user")
                            })
                        }

//                        userViewModel.currUser.value?.let { it1 -> Log.e("currUser", it1.userDocumentID) }


                        error_text.text = userViewModel.currUser.value?.userDocumentID ?: "asdasdad"

                    } else {
                        error_text.setText(result)
                    }

                }

            }



    }


}