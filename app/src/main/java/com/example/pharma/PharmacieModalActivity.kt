package com.example.pharma

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.pharma.Model.Pharmacie
import com.example.pharma.Utils.*
import kotlinx.android.synthetic.main.activity_pharmacie_modal.*

class PharmacieModalActivity : AppCompatActivity() {
    var pharmacie: Pharmacie? = null
    lateinit var FACEBOOK_URL: String
    lateinit var FACEBOOK_PAGE_ID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pharmacie_modal)
        pharmacie = intent.getParcelableExtra(PHARMACIE_PARCEL)

        FACEBOOK_URL = "https://www.facebook.com/BuyBayDz"
        FACEBOOK_PAGE_ID = "BuyBayDz"

        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        val width = dm.widthPixels
        val height = dm.heightPixels
        window.setLayout((width*.8).toInt(), (height*.7).toInt())


        val params: WindowManager.LayoutParams = window.attributes
        params.gravity = Gravity.CENTER
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        params.dimAmount = 0.7f
        params.x = 0
        params.y = -20
        window.attributes = params

        // Getting the pharmacie from the intent to show its informations

        if (pharmacie != null) {
            pharm_detail_adresse.text = pharmacie?.adresse
            pharm_detail_phone.text = pharmacie?.telephone
            pharm_detail_horaires.text = pharmacie?.horaires
            pharm_detail_conventions.text = pharmacie?.caisseConv?.get(0)
            pharm_detail_facebook.text = pharmacie?.facebook

            pharm_detail_adresse.setOnClickListener {
                val mapIntent = Intent(this, PharmDetailMap::class.java)
                mapIntent.putExtra(MAP_ACTIVITY_LONGITUDE, pharmacie?.longitude)
                mapIntent.putExtra(MAP_ACTIVITY_LATITUDE, pharmacie?.latitude)
                startActivity(mapIntent)
            }
        }
        setupPermissions()
        pharm_detail_phone.setOnClickListener {
            callPhone()
        }

        pharm_detail_facebook.setOnClickListener {
            val facebookIntent = Intent(Intent.ACTION_VIEW)
            val facebookUrl = getFacebookPageURL(this)
            facebookIntent.data = Uri.parse(facebookUrl)
            startActivity(facebookIntent)
        }

    }

    private fun callPhone() {
        val callIntent = Intent(Intent.ACTION_CALL, Uri.parse("tel:${pharm_detail_phone.text}"))
        startActivity(callIntent)
    }

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i(PHONE_PERMISSION_TAG, "Permission to record denied")
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.CALL_PHONE), PHONE_CALL_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PHONE_CALL_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.i(PHONE_PERMISSION_TAG, "Permission has been denied by user")
                } else {
                    Log.i(PHONE_PERMISSION_TAG, "Permission has been granted by user")
                }
            }
        }
    }

    //method to get the right URL to use in the intent
    fun getFacebookPageURL(context: Context): String {
        val packageManager = context.getPackageManager()
        try {
            val versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode
            return if (versionCode >= 3002850) { //newer versions of fb app
                "fb://facewebmodal/f?href=$FACEBOOK_URL"
            } else { //older versions of fb app
                "fb://page/$FACEBOOK_PAGE_ID"
            }
        } catch (e: PackageManager.NameNotFoundException) {
            return FACEBOOK_URL //normal web url
        }

    }
}