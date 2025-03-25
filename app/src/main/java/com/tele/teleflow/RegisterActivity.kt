package com.tele.teleflow

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.tele.teleflow.utils.toast

class RegisterActivity : Activity() {
    private lateinit var editTextUsername: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var editTextConfirmPassword: EditText
    private lateinit var buttonRegister: Button
    private lateinit var backButton: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        initializeViews()
        setupClickListeners()
        setupTextWatchers()
    }

    private fun initializeViews() {
        editTextUsername = findViewById(R.id.edittext_username)
        editTextEmail = findViewById(R.id.edittext_email)
        editTextPassword = findViewById(R.id.edittext_password)
        editTextConfirmPassword = findViewById(R.id.edittext_confirm_password)
        buttonRegister = findViewById(R.id.button_login) // Using the ID from layout
        backButton = findViewById(R.id.back_button)
        
        buttonRegister.isEnabled = false
    }

    private fun setupClickListeners() {
        backButton.setOnClickListener {
            finish()
        }

        buttonRegister.setOnClickListener {
            if (validateInputs()) {
                Log.d("RegisterActivity", "Registration Successful")
                toast("Registration Successful")
                
                // Pass credentials back to login
                val loginIntent = Intent(this, LoginActivity::class.java)
                loginIntent.putExtra("username", editTextUsername.text.toString().trim())
                loginIntent.putExtra("password", editTextPassword.text.toString().trim())
                startActivity(loginIntent)
                finish()
            }
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

        editTextUsername.addTextChangedListener(textWatcher)
        editTextEmail.addTextChangedListener(textWatcher)
        editTextPassword.addTextChangedListener(textWatcher)
        editTextConfirmPassword.addTextChangedListener(textWatcher)
    }

    private fun validateFields() {
        val username = editTextUsername.text.toString().trim()
        val email = editTextEmail.text.toString().trim()
        val password = editTextPassword.text.toString().trim()
        val confirmPassword = editTextConfirmPassword.text.toString().trim()

        buttonRegister.isEnabled = username.isNotEmpty() && email.isNotEmpty() &&
                password.isNotEmpty() && confirmPassword.isNotEmpty()
    }

    private fun validateInputs(): Boolean {
        val username = editTextUsername.text.toString().trim()
        val email = editTextEmail.text.toString().trim()
        val password = editTextPassword.text.toString().trim()
        val confirmPassword = editTextConfirmPassword.text.toString().trim()

        when {
            username.length < 3 -> {
                editTextUsername.error = "Username must be at least 3 characters"
                return false
            }
            !isValidEmail(email) -> {
                editTextEmail.error = "Please enter a valid email address"
                return false
            }
            password.length < 6 -> {
                editTextPassword.error = "Password must be at least 6 characters"
                return false
            }
            password != confirmPassword -> {
                editTextConfirmPassword.error = "Passwords do not match"
                return false
            }
        }
        return true
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
