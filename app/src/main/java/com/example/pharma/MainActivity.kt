package com.example.pharma

import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.example.pharma.Services.DataService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import com.shashank.sony.fancytoastlib.FancyToast
import java.io.IOException
class MainActivity : AppCompatActivity() {


    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_all -> {
                loadAllFragment(AllFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_near -> {
                loadNearFragment(NearFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_request -> {
                if (FirebaseAuth.getInstance().currentUser == null) {
                    FancyToast.makeText(this, "Veuillez vous connecter pour consulter les commandes", FancyToast.LENGTH_SHORT,
                        FancyToast.WARNING, false).show()

                    return@OnNavigationItemSelectedListener  false
                }
                    loadRequestFragment(RequestFragment())
                    return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /*DataService.fillPharmacies()
        DataService.fillPharmacieDatabase()*/
        initView()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        loadAllFragment(AllFragment())  // Load the AllFragment in the beggining
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

    }

    private fun initView() {
        //This method will use for fetching Token
        Thread(Runnable {
            try {
                Log.i("azerty", "the token" + FirebaseInstanceId.getInstance().getToken("sender", "FCM"))
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }).start()
    }

    // Loading the fragment of all pharmacies
    private fun loadAllFragment(allFragment: AllFragment) {
        val fm = supportFragmentManager.beginTransaction()
        fm.replace(R.id.fragments_holder, allFragment)
        fm.commit()
    }

    private fun loadNearFragment(nearFragment: NearFragment) {
        val fm = supportFragmentManager.beginTransaction()
        fm.replace(R.id.fragments_holder, nearFragment)
        fm.commit()
    }

    private fun loadRequestFragment(requestFragment: RequestFragment) {
        val fm = supportFragmentManager.beginTransaction()
        fm.replace(R.id.fragments_holder, requestFragment)
        fm.commit()
    }
}

