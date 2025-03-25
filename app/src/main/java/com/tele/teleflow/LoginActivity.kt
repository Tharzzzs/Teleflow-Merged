package com.tele.teleflow

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.tele.teleflow.utils.toast

class LoginActivity : Activity() {
    private lateinit var editUsername: EditText
    private lateinit var editPassword: EditText
    private lateinit var buttonLogin: Button
    private lateinit var buttonRegister: Button
    
    // Store registered credentials
    private var registeredUsername: String? = null
    private var registeredPassword: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initializeViews()
        setupClickListeners()
        setupTextWatchers()

        // Check if we have data from registration and pre-fill fields
        intent.getStringExtra("username")?.let { username ->
            registeredUsername = username
            editUsername.setText(username)
            intent.getStringExtra("password")?.let { password ->
                registeredPassword = password
                editPassword.setText(password)
            }
        }
    }

    private fun initializeViews() {
        editUsername = findViewById(R.id.edittext_username)
        editPassword = findViewById(R.id.edittext_password)
        buttonLogin = findViewById(R.id.button_login)
        buttonRegister = findViewById(R.id.button_register)

        buttonLogin.isEnabled = false
        buttonRegister.isEnabled = true
    }

    private fun setupClickListeners() {
        buttonLogin.setOnClickListener {
            val username = editUsername.text.toString().trim()
            val password = editPassword.text.toString().trim()

            when {
                username.isEmpty() -> {
                    editUsername.error = "Please enter your username"
                }
                password.length < 6 -> {
                    editPassword.error = "Password must be at least 6 characters"
                }
                else -> {
                    if ((username == registeredUsername && password == registeredPassword) || 
                        (username == "admin" && password == "admin123")) {
                        Log.d("LoginActivity", "Login Successful")
                        toast("Login Successful")
                        startActivity(Intent(this, LandingActivity::class.java))
                        finish()
                    } else {
                        Log.d("LoginActivity", "Login Failed")
                        toast("Invalid username or password")
                    }
                }
            }
        }

        buttonRegister.setOnClickListener {
            Log.d("LoginActivity", "Register Button Clicked")
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun setupTextWatchers() {
        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                validateFields()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        editUsername.addTextChangedListener(textWatcher)
        editPassword.addTextChangedListener(textWatcher)
    }

    private fun validateFields() {
        val username = editUsername.text.toString().trim()
        val password = editPassword.text.toString().trim()
        
        buttonLogin.isEnabled = username.isNotEmpty() && password.isNotEmpty()
    }
}

