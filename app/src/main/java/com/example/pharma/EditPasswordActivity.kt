package com.example.pharma

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pharma.Utils.GENERATED_PASSWORD
import com.google.firebase.auth.FirebaseAuth
import com.shashank.sony.fancytoastlib.FancyToast
import kotlinx.android.synthetic.main.activity_edit_password.*
import kotlinx.android.synthetic.main.activity_edit_password.edit_pass_new_password
import kotlinx.android.synthetic.main.activity_edit_password.edit_pass_old_password
import kotlinx.android.synthetic.main.activity_edit_password.update_pass_button


class EditPasswordActivity : AppCompatActivity() {
    lateinit var generatedPasswrd: String
    lateinit var newPassword : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_password)

        generatedPasswrd = intent.getStringExtra(GENERATED_PASSWORD)

        update_pass_button.setOnClickListener {
            performCheck(generatedPasswrd)
        }
    }

    fun performCheck(generatedPasswrd: String) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (generatedPasswrd == edit_pass_old_password.text.toString()) {
            if (edit_pass_new_password.text.toString() == edit_pass_new_password_confirm.text.toString()) {
                newPassword = edit_pass_new_password.text.toString()
                // Update the password of user in firebase Auth
                currentUser?.updatePassword(newPassword)
                FirebaseAuth.getInstance().signOut()
            }
            val loginActivityIntent = Intent(this, LoginActivity::class.java)
            loginActivityIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK) // can't go back
            startActivity(loginActivityIntent)
        } else {
            FancyToast.makeText(this, "Mot de passe actuel non valid, veuillez réessayer", FancyToast.LENGTH_SHORT,
                FancyToast.ERROR, false).show()

        }
    }
}
