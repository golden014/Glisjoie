package com.bluecactus.glisjoie.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.bluecactus.glisjoie.R
import com.bluecactus.glisjoie.ViewModel.UserViewModel

class CredentialSettingsActivity : AppCompatActivity() {

    lateinit var updateEmailLayout: RelativeLayout
    lateinit var updatePasswordLayout: RelativeLayout
    lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_credential_settings)

        updateEmailLayout = findViewById(R.id.edit_email_setting_layout)
        updatePasswordLayout = findViewById(R.id.update_password_setting_layout)

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        updateEmailLayout.setOnClickListener {

            val editText = EditText(this)
            editText.hint = "Enter your email"

            AlertDialog.Builder(this)
                .setTitle("Update Email")
                .setView(editText)
                .setPositiveButton("Update") { dialog, _ ->
                    val input = editText.text.toString()
                    userViewModel.updateEmail(input) {
                        if (it == 200) {
                            AlertDialog.Builder(this).setMessage("Update Email Success !").create().show()
                        } else if (it == 400){
                            AlertDialog.Builder(this).setMessage("Email Not Unique, Failed !").create().show()
                        } else {
                            AlertDialog.Builder(this).setMessage("Update Email Failed (Make sure it's unique !").create().show()

                        }
                    }
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") {dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL


        val currPassword = EditText(this)
        currPassword.hint = "Enter your current password"
        currPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        layout.addView(currPassword)

        val newPassword = EditText(this)
        newPassword.hint = "Enter your new password"
        newPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        layout.addView(newPassword)

        val confNewPass = EditText(this)
        confNewPass.hint = "Reenter your password to confirm"
        confNewPass.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        layout.addView(confNewPass)

        updatePasswordLayout.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Update Password")
                .setView(layout)
                .setPositiveButton("Update") { dialog, _ ->
                    val currPass = currPassword.text.toString()
                    val newPass = newPassword.text.toString()
                    val confNew = confNewPass.text.toString()

                    //TODO:panggil viewModel utk update password
                    userViewModel.updatePassword(currPass, newPass, confNew) {
                        if (it == 200) {
                            AlertDialog.Builder(this).setMessage("Update Password Success !").create().show()
                        } else if (it == 400) {
                            AlertDialog.Builder(this).setMessage("Confirm Password Don't Match").create().show()
                        } else {
                        AlertDialog.Builder(this).setMessage("Update Password Failed !").create().show()
                    }
                    }
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") {dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }
    }
}