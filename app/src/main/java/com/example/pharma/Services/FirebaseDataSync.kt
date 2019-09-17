package com.example.pharma.Services

import android.app.Application
import com.google.firebase.database.FirebaseDatabase

class FirebaseDataSync: Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }
}