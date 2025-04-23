package com.tele.teleflow

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import com.tele.teleflow.utils.toast

class ProfilePageActivity : Activity() {

    private lateinit var usernameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var currentPasswordEditText: EditText
    private lateinit var newPasswordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var pushNotificationsSwitch: Switch
    private lateinit var emailNotificationsSwitch: Switch
    private lateinit var profileVisibilitySwitch: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_page)

        initializeViews()
        loadUserData()
        setupClickListeners()
    }

    private fun initializeViews() {
        // Initialize EditTexts
        usernameEditText = findViewById(R.id.edit_username)
        emailEditText = findViewById(R.id.edit_email)
        currentPasswordEditText = findViewById(R.id.edit_current_password)
        newPasswordEditText = findViewById(R.id.edit_new_password)
        confirmPasswordEditText = findViewById(R.id.edit_confirm_password)

        // Initialize Switches
        pushNotificationsSwitch = findViewById(R.id.push_notifications_switch)
        emailNotificationsSwitch = findViewById(R.id.email_notifications_switch)
        profileVisibilitySwitch = findViewById(R.id.profile_visibility_switch)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("User Profile", Context.MODE_PRIVATE)
    }

    private fun loadUserData() {
        // Load saved user data
        val savedUsername = sharedPreferences.getString("username", "")
        val savedEmail = sharedPreferences.getString("useremail", "")
        val pushNotificationsEnabled = sharedPreferences.getBoolean("push_notifications", true)
        val emailNotificationsEnabled = sharedPreferences.getBoolean("email_notifications", true)
        val profileVisible = sharedPreferences.getBoolean("profile_visible", true)

        // Set the data to the views
        usernameEditText.setText(savedUsername)
        emailEditText.setText(savedEmail)
        pushNotificationsSwitch.isChecked = pushNotificationsEnabled
        emailNotificationsSwitch.isChecked = emailNotificationsEnabled
        profileVisibilitySwitch.isChecked = profileVisible
    }

    private fun setupClickListeners() {
        // Save username button
        findViewById<Button>(R.id.save_username_button).setOnClickListener {
            saveUsername()
        }

        // Save email button
        findViewById<Button>(R.id.save_email_button).setOnClickListener {
            saveEmail()
        }

        // Save password button
        findViewById<Button>(R.id.save_password_button).setOnClickListener {
            changePassword()
        }

        // Change profile photo
        findViewById<TextView>(R.id.change_photo_text).setOnClickListener {
            // In a real app, you would implement photo selection here
            toast("Photo selection would open here")
        }

        // Delete account button
        findViewById<Button>(R.id.delete_account_button).setOnClickListener {
            showDeleteAccountConfirmation()
        }

        // Navigation bar setup
        findViewById<LinearLayout>(R.id.btn_home).setOnClickListener {
            startActivity(Intent(this, LandingActivity::class.java))
            finish()
        }

        findViewById<LinearLayout>(R.id.btn_script).setOnClickListener {
            startActivity(Intent(this, ScriptActivity::class.java))
            finish()
        }

        findViewById<LinearLayout>(R.id.btn_profile).setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
            finish()
        }

        // Save notification settings when toggled
        pushNotificationsSwitch.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("push_notifications", isChecked).apply()
            toast("Push notifications " + if (isChecked) "enabled" else "disabled")
        }

        emailNotificationsSwitch.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("email_notifications", isChecked).apply()
            toast("Email notifications " + if (isChecked) "enabled" else "disabled")
        }

        profileVisibilitySwitch.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("profile_visible", isChecked).apply()
            toast("Profile visibility " + if (isChecked) "public" else "private")
        }
    }

    private fun saveUsername() {
        val newUsername = usernameEditText.text.toString().trim()

        if (newUsername.isEmpty()) {
            toast("Username cannot be empty")
            return
        }

        // Save the new username
        sharedPreferences.edit().putString("username", newUsername).apply()
        toast("Username updated successfully")
    }

    private fun saveEmail() {
        val newEmail = emailEditText.text.toString().trim()

        if (newEmail.isEmpty()) {
            toast("Email cannot be empty")
            return
        }

        if (!isValidEmail(newEmail)) {
            toast("Please enter a valid email address")
            return
        }

        // Save the new email
        sharedPreferences.edit().putString("useremail", newEmail).apply()
        toast("Email updated successfully")
    }

    private fun changePassword() {
        val currentPassword = currentPasswordEditText.text.toString()
        val newPassword = newPasswordEditText.text.toString()
        val confirmPassword = confirmPasswordEditText.text.toString()

        // Get the saved password (in a real app, this would be hashed)
        val savedPassword = sharedPreferences.getString("password", "default_password")

        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            toast("All password fields are required")
            return
        }

        // Check if current password is correct
        if (currentPassword != savedPassword) {
            toast("Current password is incorrect")
            return
        }

        // Check if new passwords match
        if (newPassword != confirmPassword) {
            toast("New passwords do not match")
            return
        }

        // Check password strength
        if (newPassword.length < 6) {
            toast("Password must be at least 6 characters long")
            return
        }

        // Save the new password
        sharedPreferences.edit().putString("password", newPassword).apply()

        // Clear password fields
        currentPasswordEditText.text.clear()
        newPasswordEditText.text.clear()
        confirmPasswordEditText.text.clear()

        toast("Password changed successfully")
    }

    private fun showDeleteAccountConfirmation() {
        AlertDialog.Builder(this)
            .setTitle("Delete Account")
            .setMessage("Are you sure you want to delete your account? This action cannot be undone.")
            .setPositiveButton("Delete") { _, _ ->
                // In a real app, you would implement account deletion logic here
                toast("Account would be deleted in a real app")

                // For demo purposes, redirect to login screen
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
