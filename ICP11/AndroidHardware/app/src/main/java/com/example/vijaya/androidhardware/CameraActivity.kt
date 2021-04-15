package com.example.vijaya.androidhardware

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.LocationManager
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class CameraActivity : AppCompatActivity() {
    var TAKE_PHOTO_CODE = 0
    var userImage: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        val capture = findViewById<View>(R.id.btn_take_photo) as Button
        userImage = findViewById<View>(R.id.view_photo) as ImageView

        // ICP Task2: Write the code to capture the image
    }

    fun callCamera(v: View?) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED ) {
            //show message or ask permissions from the user.
            Log.d("Maneesha", "Requesting permission")
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.CAMERA), TAKE_PHOTO_CODE)
            return
        }

        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (cameraIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(cameraIntent, TAKE_PHOTO_CODE)
        }
    }

    //If the photo is captured then set the image view to the photo captured.
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TAKE_PHOTO_CODE && resultCode == Activity.RESULT_OK) {
            val photo = data!!.extras["data"] as Bitmap
            userImage!!.setImageBitmap(photo)
            Log.d("CameraDemo", "Pic saved")
        }
    }

    fun redirectToHome(v: View?) {
        val redirect = Intent(this@CameraActivity, MainActivity::class.java)
        startActivity(redirect)
    }

    companion object {
        private const val MY_CAMERA_REQUEST_CODE = 100
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        Log.d("Maneesha", "onRequestPermissionResult")
        if (requestCode == TAKE_PHOTO_CODE) {
            if (grantResults.size <= 0) {
                Log.d("Maneesha", "User interaction was cancelled.")
                Toast.makeText(this, "User cancelled permission", Toast.LENGTH_LONG).show()

            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                if (cameraIntent.resolveActivity(packageManager) != null) {
                    startActivityForResult(cameraIntent, TAKE_PHOTO_CODE)
                }


            } else {
                Toast.makeText(this, "User denied permission", Toast.LENGTH_LONG).show()
                finish()
            }

        }
    }
}