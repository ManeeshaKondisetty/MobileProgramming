package com.example.vijaya.androidhardware

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.vijaya.androidhardware.AudioRecordingActivity
import com.example.vijaya.androidhardware.CameraActivity

class MainActivity : AppCompatActivity() {
    var button_map: Button? = null
    var button_photo: Button? = null
    var button_record: Button? = null
    var button_storage: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button_map = findViewById<View>(R.id.main_btn_map) as Button
        button_photo = findViewById<View>(R.id.main_btn_photo) as Button
        button_record = findViewById<View>(R.id.main_btn_record) as Button
        button_storage = findViewById<View>(R.id.main_btn_storage) as Button
    }

    fun onLocationClick(v: View?) {
        //This code redirects the from main page to the maps page.
        val redirect = Intent(this@MainActivity, LocationActivity::class.java)
        startActivity(redirect)
    }

    fun onPhotoClick(v: View?) {
        //This code redirects to the photo activity.
        val redirect = Intent(this@MainActivity, CameraActivity::class.java)
        startActivity(redirect)
    }

    fun onAudioClick(v: View?) {
        val redirect = Intent(this@MainActivity, AudioRecordingActivity::class.java)
        startActivity(redirect)
    }

    fun onStorageClick(v: View?) {
        val redirect = Intent(this@MainActivity, StorageActivity::class.java)
        startActivity(redirect)
    }
}