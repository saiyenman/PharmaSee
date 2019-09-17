package com.example.pharma

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.provider.Settings
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.example.pharma.Services.DataService

class NearFragment : Fragment() {

    lateinit var locationManager: LocationManager
    private var hasGps = false
    private var hasNetwork = false
    private var locationGps: Location? = null
    //private var locationNetwork: Location? = null
    var latitude = 0.0
    var longitude = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_near, container, false)
        Log.d("Lifecycle", "OnCreateView")



        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.frg) as SupportMapFragment?
        getLocation(mapFragment!!)

    }

    @SuppressLint("MissingPermission")
    private fun getLocation(mapFragment: SupportMapFragment) {
        locationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if (hasGps || hasNetwork) {

            if (hasGps) {
                Log.d("CodeAndroidLocation", "hasGps")
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0F, object :
                    LocationListener {
                    override fun onLocationChanged(location: Location?) {
                        if (location != null) {
                            Log.d("great", "${location.latitude} + ${location.longitude}")
                            latitude = location.latitude
                            longitude = location.longitude
                            locationManager.removeUpdates(this)

                            mapFragment!!.getMapAsync { mMap ->
                                mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                                mMap.clear() //clear old markers

                                DataService.fetchClosePharmacies(latitude, longitude, mMap, context!!)

                                val googlePlex = CameraPosition.builder()
                                    .target(LatLng(latitude, longitude))
                                    .zoom(10f)
                                    .bearing(0f)
                                    .tilt(45f)
                                    .build()
                                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 1000, null)

                            }

                        }
                    }

                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
                    override fun onProviderEnabled(provider: String?) {}
                    override fun onProviderDisabled(provider: String?) {}
                })

                val localGpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (localGpsLocation != null)
                    locationGps = localGpsLocation
            }

            /*if (hasNetwork) {
                Log.d("CodeAndroidLocation", "hasNetwork")
                locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    500,
                    0F,
                    object : LocationListener {
                        override fun onLocationChanged(location: Location?) {
                            if (location != null) {
                                locationNetwork = location
                                latitude = location!!.latitude
                                longitude = location!!.longitude
                            }
                            DataService.fetchClosePharmacies(latitude, longitude)
                            for (p in closePharmacies) Log.d("close", p.adresse)
                        }

                        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
                        override fun onProviderEnabled(provider: String?) {}
                        override fun onProviderDisabled(provider: String?) {}
                    })

                val localNetworkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                if (localNetworkLocation != null)
                    locationNetwork = localNetworkLocation
            }*/

        } else {
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
    }

}
