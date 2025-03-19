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
import android.widget.Toast

class ProfileActivity : Activity() {

    private lateinit var profilePicture: ImageView
    private lateinit var userNameTextView: TextView
    private lateinit var userEmailTextView: TextView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var logoutButton: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        profilePicture = findViewById(R.id.profile_picture)
        userNameTextView = findViewById(R.id.user_name)
        userEmailTextView = findViewById(R.id.user_email)
        logoutButton = findViewById(R.id.logout_button)


        sharedPreferences = getSharedPreferences("User Profile", Context.MODE_PRIVATE)

        loadProfileData()

        logoutButton.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Yes") { _, _ ->
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("No", null)
                .show()
        }

        val editProfile = findViewById<TextView>(R.id.edit_profile)
        editProfile.setOnClickListener {
            startActivity(Intent(this, ProfilePageActivity::class.java))
        }

        val goToHome = findViewById<LinearLayout>(R.id.btn_home)
        goToHome.setOnClickListener {
            startActivity(Intent(this, LandingActivity::class.java))
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