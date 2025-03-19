package com.tele.teleflow

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

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

        editTextUsername = findViewById(R.id.edittext_username)
        editTextEmail = findViewById(R.id.edittext_email)
        editTextPassword = findViewById(R.id.edittext_password)
        editTextConfirmPassword = findViewById(R.id.edittext_confirm_password)
        buttonRegister = findViewById(R.id.button_login)
        backButton = findViewById(R.id.back_button)
        buttonRegister.isEnabled = false

        backButton.setOnClickListener {
            finish()
        }

        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val username = editTextUsername.text.toString().trim()
                val email = editTextEmail.text.toString().trim()
                val password = editTextPassword.text.toString().trim()
                val confirmPassword = editTextConfirmPassword.text.toString().trim()

                buttonRegister.isEnabled = username.isNotEmpty() && email.isNotEmpty() &&
                        password.isNotEmpty() && confirmPassword.isNotEmpty()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        editTextUsername.addTextChangedListener(textWatcher)
        editTextEmail.addTextChangedListener(textWatcher)
        editTextPassword.addTextChangedListener(textWatcher)
        editTextConfirmPassword.addTextChangedListener(textWatcher)

        buttonRegister.setOnClickListener {
            val password = editTextPassword.text.toString()
            val confirmPassword = editTextConfirmPassword.text.toString()

            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Log.e("Sample Project", "Register Button Clicked")
            Toast.makeText(this, "Registered Successfully", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}
