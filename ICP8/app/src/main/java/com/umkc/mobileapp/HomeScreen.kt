package com.umkc.mobileapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton

class HomeScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        var dataBundle:Bundle=intent.extras!!
        var username:String=dataBundle.getString("alpha","")
        var welcome_msg:TextView=findViewById(R.id.message)
        welcome_msg.setText("Hello "+ username)
    }

    override fun onResume() {
        super.onResume()
        var logoutclick: AppCompatButton =findViewById(R.id.logout_button)
        logoutclick.setOnClickListener {
            finish()
        }
    }
}