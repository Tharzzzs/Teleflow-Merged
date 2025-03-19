package com.tele.teleflow

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class LoginActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val editUsername = findViewById<EditText>(R.id.edittext_username)
        val editPassword = findViewById<EditText>(R.id.edittext_password)
        val buttonLogin = findViewById<Button>(R.id.button_login)
        val buttonRegister = findViewById<Button>(R.id.button_register)


        buttonLogin.isEnabled = false
        buttonRegister.isEnabled = true


        fun checkFields() {
            val isFilled = editUsername.text.toString().trim().isNotEmpty() &&
                    editPassword.text.toString().trim().isNotEmpty()

            buttonLogin.isEnabled = isFilled
            buttonRegister.isEnabled = isFilled
        }


        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                checkFields()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        editUsername.addTextChangedListener(textWatcher)
        editPassword.addTextChangedListener(textWatcher)


        buttonLogin.setOnClickListener {
            val username = editUsername.text.toString().trim()
            val password = editPassword.text.toString().trim()

            if (username == "Myron" && password == "123") {
                Log.e("LoginActivity", "Login Successful")
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LandingActivity::class.java))
                finish()
            } else {
                Log.e("LoginActivity", "Login Failed")
                Toast.makeText(this, "Invalid Username or Password", Toast.LENGTH_SHORT).show()
            }
        }


        buttonRegister.setOnClickListener {
            Log.e("LoginActivity", "Register Button Clicked")
            Toast.makeText(this, "Opening Registration...", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}

