package com.tele.teleflow

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast

class RegisterActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val btn_signup = findViewById<Button>(R.id.btn_register)
        btn_signup.setOnClickListener {
            Log.e("CSIT 284", "Button is Clicked!")
            Toast.makeText(this,"Button is Clicked!", Toast.LENGTH_LONG).show()
        }

        val btn_login = findViewById<Button>(R.id.btn_login)
        btn_login.setOnClickListener {
            Log.e("CSIT 284", "Button is Clicked!")
            Toast.makeText(this,"Button is Clicked!",Toast.LENGTH_LONG).show()

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}