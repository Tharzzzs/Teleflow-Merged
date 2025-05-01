package com.tele.teleflow

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.tele.teleflow.repository.AuthRepository
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    private lateinit var usernameLayout: TextInputLayout
    private lateinit var emailLayout: TextInputLayout
    private lateinit var passwordLayout: TextInputLayout
    private lateinit var confirmPasswordLayout: TextInputLayout
    private lateinit var editTextUsername: TextInputEditText
    private lateinit var editTextEmail: TextInputEditText
    private lateinit var editTextPassword: TextInputEditText
    private lateinit var editTextConfirmPassword: TextInputEditText
    private lateinit var buttonRegister: MaterialButton
    private lateinit var buttonBack: MaterialButton
    private lateinit var progressView: View

    private val authRepository = AuthRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        initializeViews()
        setupClickListeners()
        setupTextWatchers()
    }

    private fun initializeViews() {
        usernameLayout = findViewById(R.id.username_layout)
        emailLayout = findViewById(R.id.email_layout)
        passwordLayout = findViewById(R.id.password_layout)
        confirmPasswordLayout = findViewById(R.id.confirm_password_layout)
        editTextUsername = findViewById(R.id.edittext_username)
        editTextEmail = findViewById(R.id.edittext_email)
        editTextPassword = findViewById(R.id.edittext_password)
        editTextConfirmPassword = findViewById(R.id.edittext_confirm_password)
        buttonRegister = findViewById(R.id.button_register)
        buttonBack = findViewById(R.id.button_back)
        progressView = findViewById(R.id.progress_overlay)

        buttonRegister.isEnabled = false
        progressView.visibility = View.GONE
    }

    // Implement the missing setupClickListeners method
    private fun setupClickListeners() {
        buttonRegister.setOnClickListener {
            if (validateInputs()) {
                registerUser()
            }
        }

        buttonBack.setOnClickListener {
            finish()
        }
    }

    // Implement the missing setupTextWatchers method
    private fun setupTextWatchers() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                validateInputsRealTime()
            }
        }

        editTextUsername.addTextChangedListener(textWatcher)
        editTextEmail.addTextChangedListener(textWatcher)
        editTextPassword.addTextChangedListener(textWatcher)
        editTextConfirmPassword.addTextChangedListener(textWatcher)
    }

    private fun validateInputsRealTime() {
        val username = editTextUsername.text.toString().trim()
        val email = editTextEmail.text.toString().trim()
        val password = editTextPassword.text.toString()
        val confirmPassword = editTextConfirmPassword.text.toString()

        // Clear previous errors
        usernameLayout.error = null
        emailLayout.error = null
        passwordLayout.error = null
        confirmPasswordLayout.error = null

        // Enable register button only if all fields are valid
        buttonRegister.isEnabled = username.isNotEmpty() &&
                Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
                password.length >= 6 &&
                password == confirmPassword
    }

    private fun validateInputs(): Boolean {
        val username = editTextUsername.text.toString().trim()
        val email = editTextEmail.text.toString().trim()
        val password = editTextPassword.text.toString()
        val confirmPassword = editTextConfirmPassword.text.toString()

        var isValid = true

        if (username.isEmpty()) {
            usernameLayout.error = "Username is required"
            isValid = false
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.error = "Valid email is required"
            isValid = false
        }

        if (password.length < 6) {
            passwordLayout.error = "Password must be at least 6 characters"
            isValid = false
        }

        if (password != confirmPassword) {
            confirmPasswordLayout.error = "Passwords do not match"
            isValid = false
        }

        return isValid
    }

    private fun registerUser() {
        val username = editTextUsername.text.toString().trim()
        val email = editTextEmail.text.toString().trim()
        val password = editTextPassword.text.toString()

        showLoading(true)

        lifecycleScope.launch {
            try {
                authRepository.registerUser(email, password, username)
                Toast.makeText(this@RegisterActivity, "Registration successful. Please log in.", Toast.LENGTH_SHORT).show()

                // Go back to login screen
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            } catch (e: Exception) {
                Log.e("RegisterActivity", "Registration failed", e)
                Toast.makeText(this@RegisterActivity, "Registration failed: ${e.localizedMessage ?: e.message}", Toast.LENGTH_LONG).show()

                showLoading(false)
            }
        }
    }


    private fun showLoading(show: Boolean) {
        progressView.visibility = if (show) View.VISIBLE else View.GONE
        buttonRegister.isEnabled = !show
        buttonBack.isEnabled = !show
    }
}