package com.tele.teleflow

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.tele.teleflow.repository.AuthRepository
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var usernameLayout: TextInputLayout
    private lateinit var passwordLayout: TextInputLayout
    private lateinit var editUsername: TextInputEditText
    private lateinit var editPassword: TextInputEditText
    private lateinit var buttonLogin: MaterialButton
    private lateinit var buttonRegister: MaterialButton
    private lateinit var progressView: View

    private val authRepository = AuthRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize views safely
        initializeViews()

        // Setup click listeners
        setupClickListeners()

        // Setup text watchers
        setupTextWatchers()
    }

    private fun initializeViews() {
        // Safely initialize all views, ensuring they are not null
        usernameLayout = findViewById(R.id.username_layout) ?: return
        passwordLayout = findViewById(R.id.password_layout) ?: return
        editUsername = findViewById(R.id.edittext_username) ?: return
        editPassword = findViewById(R.id.edittext_password) ?: return
        buttonLogin = findViewById(R.id.button_login) ?: return
        buttonRegister = findViewById(R.id.button_register) ?: return
        progressView = findViewById(R.id.progress_overlay) ?: return

        // Disable login button initially
        buttonLogin.isEnabled = false
        buttonRegister.isEnabled = true
        progressView.visibility = View.GONE
    }

    private fun setupClickListeners() {
        buttonLogin.setOnClickListener {
            val email = editUsername.text.toString().trim()
            val password = editPassword.text.toString().trim()

            when {
                email.isEmpty() -> {
                    usernameLayout.error = "Please enter your email"
                }
                password.isEmpty() -> {
                    passwordLayout.error = "Please enter your password"
                }
                else -> {
                    // Show loading state
                    progressView.visibility = View.VISIBLE
                    buttonLogin.isEnabled = false
                    buttonRegister.isEnabled = false

                    lifecycleScope.launch {
                        try {
                            val user = authRepository.login(email, password)
                            Log.d("LoginActivity", "Login Successful: ${user.email}")
                            showToast("Login Successful")

                            // Navigate to MainActivity with proper flags to clear the task stack
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)

                            // Finish LoginActivity so the user cannot navigate back
                            finish()

                        } catch (e: Exception) {
                            Log.e("LoginActivity", "Login Failed", e)
                            showToast("Login Failed: ${e.message}")

                            progressView.visibility = View.GONE
                            buttonLogin.isEnabled = true
                            buttonRegister.isEnabled = true
                        }
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
        val email = editUsername.text.toString().trim()
        val password = editPassword.text.toString().trim()

        // Reset errors
        usernameLayout.error = null
        passwordLayout.error = null

        // Enable login button only if both fields are not empty
        buttonLogin.isEnabled = email.isNotEmpty() && password.isNotEmpty()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
