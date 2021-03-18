package com.umkc.mobileapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton

class HomeScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }

    override fun onResume() {
        super.onResume()
        var logoutclick: AppCompatButton =findViewById(R.id.logout_button)
        logoutclick.setOnClickListener {
            finish()
        }
    }
}