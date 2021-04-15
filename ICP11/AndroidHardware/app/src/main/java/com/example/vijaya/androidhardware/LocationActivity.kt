package com.example.vijaya.androidhardware

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*

class LocationActivity : AppCompatActivity(), OnMapReadyCallback {
    private var mMap: GoogleMap? = null
    var geocoder: Geocoder? = null
    val LOCATION_PERMISSION_RESULT_CODE = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
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
        geocoder = Geocoder(this)
        val userAddress = StringBuilder()
        val userCurrentLocation = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val userCurrentLocationListener: LocationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {}
            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }
        var userCurrentLocationCorodinates: LatLng? = null
        var latitude = 0.0
        var longitude = 0.0
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat
                        .checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //show message or ask permissions from the user.
            Log.d("Maneesha", "Requesting permission")
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), LOCATION_PERMISSION_RESULT_CODE)
            return
        }

        //Getting the current location of the user.
               val locationManager:LocationManager=(this.getSystemService(Context.LOCATION_SERVICE) as LocationManager)
               val location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
               latitude=location.latitude
               longitude=location.longitude
               userCurrentLocationCorodinates= LatLng(latitude,longitude)



        // ICP Task1: Write the code to get the current location of the user

        //Getting the address of the user based on latitude and longitude.
        try {
            val addresses = geocoder!!.getFromLocation(latitude, longitude, 1)
            val city = addresses[0].locality
            val state = addresses[0].adminArea
            val country = addresses[0].countryName
            val postalCode = addresses[0].postalCode
            val knownName = addresses[0].featureName
            userAddress.append("$knownName, $city, $state, $country $postalCode").append("\t")
            Toast.makeText(this, " $userAddress", Toast.LENGTH_SHORT).show()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        //Setting our image as the marker icon.
        mMap!!.addMarker(MarkerOptions().position(userCurrentLocationCorodinates!!)
                .title("Your current address.").snippet(userAddress.toString())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_maps)))
        //Setting the zoom level of the map.
        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(userCurrentLocationCorodinates, 7f))
    }



    override fun onPointerCaptureChanged(hasCapture: Boolean) {}
    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        Log.d("Maneesha", "onRequestPermissionResult")
        if (requestCode == LOCATION_PERMISSION_RESULT_CODE) {
            if (grantResults.size <= 0){
                Log.d("Maneesha", "User interaction was cancelled.")
                Toast.makeText(this,"User cancelled permission",Toast.LENGTH_LONG).show()

            }
            else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //Getting the current location of the user.
                val locationManager:LocationManager=(this.getSystemService(Context.LOCATION_SERVICE) as LocationManager)
                val location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                var latitude=location.latitude
                var longitude=location.longitude
                var userCurrentLocationCorodinates= LatLng(latitude,longitude)

                val userAddress = StringBuilder()


                // ICP Task1: Write the code to get the current location of the user

                //Getting the address of the user based on latitude and longitude.
                try {
                    val addresses = geocoder!!.getFromLocation(latitude, longitude, 1)
                    val city = addresses[0].locality
                    val state = addresses[0].adminArea
                    val country = addresses[0].countryName
                    val postalCode = addresses[0].postalCode
                    val knownName = addresses[0].featureName
                    userAddress.append("$knownName, $city, $state, $country $postalCode").append("\t")
                    Toast.makeText(this, " $userAddress", Toast.LENGTH_SHORT).show()
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
                //Setting our image as the marker icon.
                mMap!!.addMarker(MarkerOptions().position(userCurrentLocationCorodinates!!)
                        .title("Your current address.").snippet(userAddress.toString())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_maps)))
                //Setting the zoom level of the map.
                mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(userCurrentLocationCorodinates, 7f))
            }
        } else {
            Toast.makeText(this,"User denied permission",Toast.LENGTH_LONG).show()
            finish()
        }


    }
}