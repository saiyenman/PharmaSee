package com.example.pharma

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.WindowManager
import com.example.pharma.Utils.IMAGE_MODAL_URL
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_request_modal.*

class RequestModalActivity : AppCompatActivity() {
    lateinit var imageUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_modal)

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

        imageUrl = intent.getStringExtra(IMAGE_MODAL_URL)
        Picasso.get().load(imageUrl).into(image_modal_image)
    }
}
