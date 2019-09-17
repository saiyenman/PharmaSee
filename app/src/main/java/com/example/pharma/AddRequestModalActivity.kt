package com.example.pharma

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.WindowManager
import android.widget.Toast
import br.com.simplepass.loading_button_lib.interfaces.OnAnimationEndListener
import com.example.pharma.AdapterItems.RequestItem
import com.example.pharma.Model.Request
import com.example.pharma.RequestFragment.Companion.requestAdapter
import com.example.pharma.Utils.IMAGE_SELECT_REQUEST_CODE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.shashank.sony.fancytoastlib.FancyToast
import kotlinx.android.synthetic.main.activity_add_request_modal.*
import java.util.*

class AddRequestModalActivity : AppCompatActivity() {
    var selectedPhotoUri: Uri? = null  // So that we can upload it to Firebase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_request_modal)

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

        request_add_image.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.setType("image/*")
            startActivityForResult(intent, IMAGE_SELECT_REQUEST_CODE)
        }

        val bm : Bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.checked);
        request_send.setOnClickListener {
            request_send.startAnimation()
            request_send.doneLoadingAnimation(Color.WHITE, bm)
            uploadImageAndSaveRequest()
        }
    }

    /* Here we receive the result of choosing a photo */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_SELECT_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            selectedPhotoUri = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)

            request_add_image.setImageBitmap(bitmap)
            //request_add_image.alpha = 0f // to hide the button because it hides the loaded image
        }
    }

    private fun uploadImageAndSaveRequest() {
        if (selectedPhotoUri == null) return

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d("Upload", "Successfully uploaded image ${it.metadata?.path}")
                ref.downloadUrl.addOnSuccessListener {
                    saveRequestToDatabse(it.toString())  // Once upload successfull we save the request
                }
            }
            .addOnFailureListener {
            }
    }

    private fun saveRequestToDatabse(requestImage: String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val requestKey = FirebaseDatabase.getInstance().getReference("/requests").push().key
        val ref = FirebaseDatabase.getInstance().getReference("/requests/$requestKey") // We get the user specific collection

        val request = Request(requestKey!!, uid, requestImage, request_description.text.toString(), "Reçu")
        ref.setValue(request)
            .addOnSuccessListener {
                Log.d("Upload", "Saved request to Firebase Database")
                updateRequestList()
                request_add_image.setBackgroundResource(R.drawable.rounded_button_ville_list)
                request_description.text!!.clear()

                /*request_send.revertAnimation(object: OnAnimationEndListener {
                    override fun onAnimationEnd() {
                        request_send.background = this@AddRequestModalActivity.getDrawable(R.drawable.rounded_button_red)
                    }
                })*/

                /*Toast.makeText(this, "La commande a été envoyé avec succes", Toast.LENGTH_SHORT).show()*/
                FancyToast.makeText(this, "La commande a été envoyé avec succes", FancyToast.LENGTH_SHORT,FancyToast.SUCCESS, false).show()
            }
            .addOnFailureListener {
                Log.d("Upload", "could not save request to Firebase Database")
            }
    }

    private fun updateRequestList() {
        val requestRef = FirebaseDatabase.getInstance().getReference("/requests")
        val uid = FirebaseAuth.getInstance().uid
        requestRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                requestAdapter.clear()
                var i = 0
                p0.children.forEach {
                    val request = it.getValue(Request::class.java)
                    if (request != null && request.uid == uid) {
                        requestAdapter.add(RequestItem(request))
                        requestAdapter.notifyItemInserted(i)
                        i++
                    }
                }
            }
            override fun onCancelled(p0: DatabaseError) {
                Log.d(ContentValues.TAG, "Cannot load pharmacie list")
            }
        })
    }


}
