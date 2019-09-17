package com.example.pharma

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.content.PermissionChecker
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pharma.AdapterItems.PharmacieItem
import com.example.pharma.AdapterItems.VilleItem
import com.example.pharma.Services.DataService
import com.example.pharma.Services.DataService.villeList
import com.example.pharma.Utils.PHARMACIE_PARCEL
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_all.*
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.pharmacie_item.view.*

private const val PERMISSION_REQUEST = 10

class AllFragment : Fragment() {
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    companion object {
        val villeAdapter = GroupAdapter<ViewHolder>()
        val pharmaciesAdapter = GroupAdapter<ViewHolder>()
        lateinit var pharmListProgressBar: ProgressBar
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var perm = checkPermission(permissions)
            if (perm != true)
                requestPermissions(permissions, PERMISSION_REQUEST)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_all, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pharmListProgressBar = list_pharmacie_progress
        getPharmaciesByVilleId(1)  // Be default get the pharmacie of Adrar(1)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        list_ville_recycler.layoutManager = linearLayoutManager

        list_ville_recycler.adapter = villeAdapter
        list_pharmacie_recycle.adapter = pharmaciesAdapter


        villeAdapter.setOnItemClickListener { item, view ->
            view.isSelected = true
            val villeItem = item as VilleItem
            getPharmaciesByVilleId(villeItem.ville.id)  // get the pharm corresp to the city code
            //view.setBackgroundResource(R.drawable.rounded_button_red)
        }

        pharmaciesAdapter.setOnItemClickListener { item, view ->
            val pharmacie = (item as PharmacieItem).pharmacie
            val pharmModalIntent = Intent(activity, PharmacieModalActivity::class.java)
            pharmModalIntent.putExtra(PHARMACIE_PARCEL, pharmacie)
            startActivity(pharmModalIntent)
        }


        for (ville in villeList) {
            villeAdapter.add(VilleItem(ville))
        }

        val loginIntent = Intent(activity, LoginActivity::class.java)
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            loginLogoutButton.setOnClickListener {
                startActivity(loginIntent)
            }
        } else {
            Picasso.get().load(R.drawable.logout).into(loginLogoutButton)
            loginLogoutButton.backgroundTintList = ColorStateList.valueOf(Color.TRANSPARENT)
            loginLogoutButton.setOnClickListener {
                FirebaseAuth.getInstance().signOut()
                startActivity(loginIntent)
                activity?.finish()
            }
        }
    }

    fun getPharmaciesByVilleId(id: Int) {
        DataService.fetchPharmaciesByVilleId(id)
    }

    private fun checkPermission(permissionArray: Array<String>): Boolean {
        var allSuccess = true
        for (i in permissionArray.indices) {
            if (PermissionChecker.checkCallingOrSelfPermission(activity!!, permissionArray[i]) == PermissionChecker.PERMISSION_DENIED)
                allSuccess = false
        }
        return allSuccess
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST) {
            var allSuccess = true
            for (i in permissions.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    allSuccess = false
                    val requestAgain = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && shouldShowRequestPermissionRationale(permissions[i])
                    if (requestAgain) {
                        Toast.makeText(activity, "Permission denied", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(activity, "Go to settings and enable the permission", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            if (allSuccess) {}

        }
    }

}
