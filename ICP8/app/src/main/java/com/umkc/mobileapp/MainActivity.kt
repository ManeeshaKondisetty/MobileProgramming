package com.umkc.mobileapp

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton

class MainActivity : AppCompatActivity() {
    lateinit var username:EditText
    lateinit var password:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        username=findViewById(R.id.username)
        username.setText("")

        password=findViewById(R.id.password)
        password.setText("")
    }

    @SuppressLint("WrongViewCast")
    override fun onResume() {
        super.onResume()
        var loginButton:Button=findViewById(R.id.button)
        loginButton.setOnClickListener {
            var usernamecheck:String=username.text.toString()
            var passwordCheck:String=password.text.toString()
            if (usernamecheck=="Maneesha" && passwordCheck=="password"){
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                var navigate:Intent= Intent(this,HomeScreen::class.java)
                startActivity(navigate)
            }
            else {
                Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show()
            }
        }
        var hint:TextView=findViewById(R.id.hint)
        hint.setOnClickListener {
            Toast.makeText(this, "Enter code sent to registered Email ID", Toast.LENGTH_SHORT).show()
        }
        var signup:TextView=findViewById(R.id.sign_up)
        signup.setOnClickListener {
            var url:String="https://umkc.umsystem.edu/psp/csprdk/?cmd=login&languageCd=ENG&"
            var signupurl:Intent= Intent(Intent.ACTION_VIEW)
            signupurl.setData(Uri.parse(url))
            startActivity(signupurl)
        }
    }
}