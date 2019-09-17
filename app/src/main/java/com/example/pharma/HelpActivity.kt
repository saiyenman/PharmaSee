package com.example.pharma

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class HelpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)

        /*val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "Aide"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)*/

    }

   /* override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }*/


}
