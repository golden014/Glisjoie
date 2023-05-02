package com.bluecactus.glisjoie.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.bluecactus.glisjoie.R
import com.bluecactus.glisjoie.ViewModel.RegisterViewModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var email_text: String
    private lateinit var username_text: String
    private lateinit var password_text: String
    private lateinit var confirm_password_text: String
    private lateinit var register_button: Button
    private lateinit var error_text: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        register_button = findViewById(R.id.register_button)
        error_text = findViewById(R.id.register_error_message)
//        regisViewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
        val regisViewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        register_button.setOnClickListener {
            email_text = findViewById<EditText>(R.id.register_email).text.toString()
            username_text = findViewById<EditText>(R.id.register_username).text.toString()
            password_text = findViewById<EditText>(R.id.register_password).text.toString()
            confirm_password_text = findViewById<EditText>(R.id.register_confirm_password).text.toString()
            Log.e("register", email_text)

            regisViewModel.registerUser(email_text, username_text, "aaaaa", password_text, confirm_password_text) { result ->
                Log.e("register", "done ?")
                if (result == 200) {
                    error_text.setText("Regis Success")
                } else {
                    error_text.setText("errorrr")
                }
            }

        }


    }
}