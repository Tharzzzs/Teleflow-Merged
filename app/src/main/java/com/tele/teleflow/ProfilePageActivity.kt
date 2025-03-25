package com.tele.teleflow

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.tele.teleflow.utils.toast

class ProfilePageActivity : Activity() {

    private lateinit var profilePicture: ImageView
    private lateinit var userNameTextView: TextView
    private lateinit var userEmailTextView: TextView
    private lateinit var editUserName: EditText
    private lateinit var editEmail: EditText
    private lateinit var saveButton: TextView
    private lateinit var backButton: TextView
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_page)

        profilePicture = findViewById(R.id.profile_picture)
        userNameTextView = findViewById(R.id.user_name)
        userEmailTextView = findViewById(R.id.user_email)
        editUserName = findViewById(R.id.edit_user_name)
        editEmail = findViewById(R.id.edit_email)
        saveButton = findViewById(R.id.save_button)
        backButton = findViewById(R.id.back_button)

        sharedPreferences = getSharedPreferences("User Profile", Context.MODE_PRIVATE)

        loadProfileData()

        saveButton.setOnClickListener {
            saveProfileChanges()
        }

        backButton.setOnClickListener {
            finish()
        }
    }

    private fun loadProfileData() {
        val savedUserName = sharedPreferences.getString("username", "Myron Alia")
        val savedUserEmail = sharedPreferences.getString("useremail", "myronalia@gmail.com")

        userNameTextView.text = savedUserName
        userEmailTextView.text = savedUserEmail
    }

    private fun saveProfileChanges() {
        val newUserName = editUserName.text.toString().trim()
        val newUserEmail = editEmail.text.toString().trim()

        if (newUserName.isEmpty() || newUserEmail.isEmpty()) {
            toast("Please fill in all fields")
            return
        }

        val editor = sharedPreferences.edit()
        editor.putString("username", newUserName)
        editor.putString("useremail", newUserEmail)
        editor.apply()

        userNameTextView.text = newUserName
        userEmailTextView.text = newUserEmail

        editUserName.text.clear()
        editEmail.text.clear()

        toast("Profile updated successfully")
    }
}
