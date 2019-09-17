package com.example.pharma

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.example.pharma.Utils.MAP_ACTIVITY_LATITUDE
import com.example.pharma.Utils.MAP_ACTIVITY_LONGITUDE

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class PharmDetailMap : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener{

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    private lateinit var map: GoogleMap
    private var longitude = 0.0
    private var latitude = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pharm_detail_map)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        latitude = intent.getDoubleExtra(MAP_ACTIVITY_LATITUDE, 0.0)
        longitude = intent.getDoubleExtra(MAP_ACTIVITY_LONGITUDE, 0.0)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.mapType = GoogleMap.MAP_TYPE_TERRAIN

        map.getUiSettings().setZoomControlsEnabled(true)
        map.setOnMarkerClickListener(this)
        setUpMap()  // To grant the 'user location' permission
    }

    override fun onMarkerClick(p0: Marker?)= false

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }

        val location = LatLng(latitude, longitude)
        map.addMarker(MarkerOptions().position(location).title("Position"))
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 16.0f)) // to center the map
    }


}
