package com.tele.teleflow

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast

class LoginActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btn_signup = findViewById<Button>(R.id.btn_signup)
        btn_signup.setOnClickListener {
            Log.e("CSIT 284", "Button is Clicked!")
            Toast.makeText(this,"Button is Clicked!",Toast.LENGTH_LONG).show()

            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        val btn_login = findViewById<Button>(R.id.btn_login)
        btn_login.setOnClickListener {
            Log.e("CSIT 284", "Button is Clicked!")
            Toast.makeText(this,"Button is Clicked!",Toast.LENGTH_LONG).show()

            val intent = Intent(this, LandingActivity::class.java)
            startActivity(intent)
        }

    }
}
