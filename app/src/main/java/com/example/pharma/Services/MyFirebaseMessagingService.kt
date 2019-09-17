package com.example.pharma.Services

import android.content.Context
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

const val TAG = "Received notification"

class MyFirebaseMessagingService: FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage?) {
        super.onMessageReceived(message)
        if (message?.data != null) {
            Log.d("FBnotification", "Data " + message.data.toString())
        }
        if (message?.notification != null) {
            Log.d("FBnotification", "Data " + message.notification.toString())
        }
    }

    override fun onNewToken(token: String?) {
        super.onNewToken(token)
        Log.i("azerty", "new token here")
        Log.i("azerty", token.toString())

        val sharedPreferences = getSharedPreferences("TOKEN", Context.MODE_PRIVATE)
        var editor = sharedPreferences.edit()
        editor.putString("TOKEN", token)
        editor.commit()
    }
}