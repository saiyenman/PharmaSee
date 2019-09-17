package com.example.pharma

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import com.example.pharma.Model.TokenData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.shashank.sony.fancytoastlib.FancyToast
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        FirebaseMessaging.getInstance().isAutoInitEnabled = true
        // If the user is already logged in, go to main activity
        if (FirebaseAuth.getInstance().uid != null) {
            launchMainActivity()
        }

        login_button.setOnClickListener {
            performLogin()
        }

        login_create_account.setOnClickListener {
            val registerIntent = Intent(this, RegisterActivity::class.java)
            startActivity(registerIntent)
        }

        login_ignore.setOnClickListener {
            val mainIntent = Intent(this, MainActivity::class.java)
            startActivity(mainIntent)
        }

        help_button.setOnClickListener {
            startActivity(Intent(this, HelpActivity::class.java))
        }

        login_phone.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(10))
    }

    fun performLogin() {
        val phone = login_phone.text.toString()
        val password = login_password.text.toString()
        Log.d("PASS", password)
        Log.i("Login","${phone}@gmail.com")
        Log.i("Login",password)
        val bmSuccess : Bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.checked)
        val bmFail : Bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.xmark)
        login_button.startAnimation()
        if (phone.isNotEmpty() && password.isNotEmpty()) {
            FirebaseAuth.getInstance().signInWithEmailAndPassword("${phone}@gmail.com", password)
                .addOnCompleteListener {
                    if (!it.isSuccessful) {
                        login_button.doneLoadingAnimation(Color.WHITE, bmFail)
                        login_button.revertAnimation {
                            login_button.setBackgroundResource(R.drawable.rounded_button_red)
                            login_button.setTextColor(Color.WHITE)
                        }
                        return@addOnCompleteListener
                    }
                    login_button.doneLoadingAnimation(Color.WHITE, bmSuccess)
                    saveUserTokenToDatabase()
                    launchMainActivity()
                }
                .addOnFailureListener {
                    //Toast.makeText(this, "La Connection a échouée", Toast.LENGTH_SHORT).show()
                    FancyToast.makeText(this, "La connexion a échoué, veuillez vérifier vos informations", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show()

                }
        } else {
            login_button.doneLoadingAnimation(Color.WHITE, bmFail)
            login_button.revertAnimation {
                login_button.setBackgroundResource(R.drawable.rounded_button_red)
                login_button.setTextColor(Color.WHITE)
            }
            FancyToast.makeText(this, "Veuillez remplire les champs vides", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show()
        }
    }

    private fun saveUserTokenToDatabase() {
        //val tokenKey = FirebaseDatabase.getInstance().getReference("/userTokens")
        val userRef = FirebaseAuth.getInstance().uid
        val tokenRef = FirebaseDatabase.getInstance().getReference("/userTokens/$userRef")
        val tokenData = TokenData(getSharedPreferences("TOKEN", Context.MODE_PRIVATE).getString("TOKEN", ""), userRef!!)

        tokenRef.setValue(tokenData)
            .addOnSuccessListener {

            }

        /*tokenRef.setValue(tokenData)
            .addOnSuccessListener {
                Log.i("TOKEN", "Token saved to database successfully")
            }*/
    }

    fun launchMainActivity() {
        val mainActivityIntent = Intent(this, MainActivity::class.java)
        mainActivityIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(mainActivityIntent)
    }
}
