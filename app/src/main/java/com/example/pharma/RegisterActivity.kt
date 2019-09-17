package com.example.pharma

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.pharma.Model.Password
import com.example.pharma.Model.TokenData
import com.example.pharma.Model.User
import com.example.pharma.Services.RetrofitService
import com.example.pharma.Utils.GENERATED_PASSWORD
import com.example.pharma.Utils.MAX_GENERATED_PASSWORD_LENGTH
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        register_button.setOnClickListener {
            performRegister()
        }

        register_have_account.setOnClickListener {
            val loginActivityIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginActivityIntent)
        }
    }

    fun performRegister() {
        val email = register_phone.text.toString() + "@gmail.com"
        val password = generatePassword()
        val nss = register_nss.text.toString()
        val nom = register_nom.text.toString()
        val prenom = register_prenom.text.toString()
        val addresse = register_addresse.text.toString()

        val bmSuccess : Bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.checked)
        val bmFail : Bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.xmark)
        register_button.startAnimation()

        if (email.isEmpty() || nss.isEmpty() || nom.isEmpty() || prenom.isEmpty() || addresse.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir les champs manquants !", Toast.LENGTH_SHORT).show()
            register_button.doneLoadingAnimation(Color.WHITE, bmFail)
            register_button.revertAnimation {
                register_button.setBackgroundResource(R.drawable.rounded_button_red)
                register_button.setTextColor(Color.WHITE)
            }
        } else { /* Using Firebase now */
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener {
                            register_button.doneLoadingAnimation(Color.WHITE, bmSuccess)
                            // Sending the password to the users phone
                            sendPasswordMessage(password, register_phone.text.toString())
                            saveUserToDatabase(password)
                            saveUserTokenToDatabase()
                        }
                }
        }
    }


    fun saveUserToDatabase(password: String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/user/$uid")

        val user = User(
            uid,
            register_phone.text.toString(),
            register_nss.text.toString(),
            register_nom.text.toString(),
            register_prenom.text.toString(),
            register_addresse.text.toString()
        )
        ref.setValue(user)
            .addOnSuccessListener {
                Log.i("Register", "Saved user to Database")
                val editPasswordActivity = Intent(this, EditPasswordActivity::class.java)
                editPasswordActivity.putExtra(GENERATED_PASSWORD, password)
                editPasswordActivity.flags =
                    Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK) // can't go back
                startActivity(editPasswordActivity)
            }
            .addOnFailureListener {
                Log.i("Register", "Failed saving user to Database")
            }
    }

    private fun saveUserTokenToDatabase() {
        val userRef = FirebaseAuth.getInstance().uid
        val tokenRef = FirebaseDatabase.getInstance().getReference("/userTokens/$userRef")
        val tokenData = TokenData(getSharedPreferences("TOKEN", Context.MODE_PRIVATE).getString("TOKEN", ""), userRef!!)

        tokenRef.setValue(tokenData)
            .addOnSuccessListener {
                Log.i("TOKEN", "Token saved to database successfully")
            }
    }

    fun sendPasswordMessage(password: String, phone: String) {
        val passToSend = Password(password, phone)
        val call = RetrofitService.endpoint.sendPassword(passToSend)
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                Log.i("Retrofit", response.toString())
                if (response.isSuccessful) {
                    Toast.makeText(this@RegisterActivity, "Mot de passe envoyé avec succes !", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("Retrofit", t.toString())
                Toast.makeText(this@RegisterActivity, "Une erreur est survenue ! !", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun generatePassword(): String {
        val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        val randomString = (1..MAX_GENERATED_PASSWORD_LENGTH)
            .map { i -> kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
        return randomString
    }
}
