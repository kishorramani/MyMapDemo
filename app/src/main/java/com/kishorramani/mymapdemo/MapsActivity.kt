package com.kishorramani.mymapdemo

import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.kishorramani.mymapdemo.model.LocationModel
import com.kishorramani.mymapdemo.utils.Utils
import com.kishorramani.mymapdemo.utils.Utils.Companion.LOCATION_REQ_CODE
import com.nishidhpatel.currentLocation.viewModel.LocationViewModel


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var locationViewModel: LocationViewModel
    private var lastLocation: LocationModel? = null
    private var currentLocation: LocationModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //initViewModel
        locationViewModel = ViewModelProvider(this).get(LocationViewModel::class.java)

        //checkpermission
        if (Utils(this).checkPermissionForLocation()) {
            startLocation()
        }
    }

    private fun startLocation() {
        if (Utils(this).checkGPS()) {
            locationViewModel.getLocationData().observe(this, Observer {
                lastLocation = currentLocation
                currentLocation = it

                val myLocation = LatLng(it.latitude, it.longitude)
                mMap.addMarker(MarkerOptions().position(myLocation).title(""))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 13f))

                if (lastLocation != null && currentLocation != null) {
                    drawLine(lastLocation!!, currentLocation!!)
                }
//                tvLatLong.text = resources.getString(R.string.strLatLong) + it.latitude + " " + it.longitude
            })
        }
    }

    private fun drawLine(l1: LocationModel, l2: LocationModel) {
        val line: Polyline = mMap.addPolyline(
            PolylineOptions()
                .add(LatLng(l1.latitude, l1.longitude), LatLng(l2.latitude, l2.longitude))
                .width(5f)
                .color(Color.RED)
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_REQ_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocation()
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onRestart() {
        super.onRestart()
        startLocation()
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
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        /*val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))*/
    }
}