package com.tele.teleflow

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.tele.teleflow.utils.toast

class ProfileActivity : Activity() {

    private lateinit var profilePicture: ImageView
    private lateinit var userNameTextView: TextView
    private lateinit var userEmailTextView: TextView
    private lateinit var editProfileButton: LinearLayout
    private lateinit var logoutButton: LinearLayout
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        initializeViews()
        setupClickListeners()
        loadProfileData()
    }

    private fun initializeViews() {
        profilePicture = findViewById(R.id.profile_picture)
        userNameTextView = findViewById(R.id.user_name)
        userEmailTextView = findViewById(R.id.user_email)
        editProfileButton = findViewById(R.id.edit_profile_button)
        logoutButton = findViewById(R.id.logout_button)
        sharedPreferences = getSharedPreferences("User Profile", Context.MODE_PRIVATE)
    }

    private fun setupClickListeners() {
        // Edit profile button
        editProfileButton.setOnClickListener {
            startActivity(Intent(this, ProfilePageActivity::class.java))
        }

        // Logout button
        logoutButton.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Yes") { _, _ ->
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    toast("Logged out successfully")
                }
                .setNegativeButton("No", null)
                .show()
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
            // We're already in the profile screen
        }
    }

    override fun onResume() {
        super.onResume()
        loadProfileData()
    }

    private fun loadProfileData() {
        val savedUserName = sharedPreferences.getString("username", "Myron Alia")
        val savedUserEmail = sharedPreferences.getString("useremail", "myronalia@gmail.com")

        userNameTextView.text = savedUserName
        userEmailTextView.text = savedUserEmail
    }
}
