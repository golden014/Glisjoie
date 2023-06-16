package com.bluecactus.glisjoie.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
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

            if (layout.parent != null) {
                (layout.parent as ViewGroup).removeView(layout)
            }

            val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
            val alertDialog: AlertDialog = alertDialogBuilder.setTitle("Update Password")
                .setView(layout)
                .setPositiveButton("Update") { dialog, _ ->
                    val currPass = currPassword.text.toString()
                    val newPass = newPassword.text.toString()
                    val confNew = confNewPass.text.toString()

                    userViewModel.updatePassword(currPass, newPass, confNew) { responseCode ->
                        val message = when (responseCode) {
                            200 -> "Update Password Success!"
                            400 -> "Confirm Password Don't Match"
                            else -> "Update Password Failed!"
                        }
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()

            alertDialog.show()
        }

    }
}