package com.tele.teleflow

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import com.tele.teleflow.utils.toast

class LandingActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)

        val btn_settings = findViewById<ImageButton>(R.id.btn_settings)
        btn_settings.setOnClickListener {
            Log.e("Settings button clicked", "Button is Clicked!")
            this.toast("Button is Clicked!") // 02/26/2024 (changed to toast function from utils instead of makeText() method from Toast class)

            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        val btn_home = findViewById<ImageView>(R.id.btn_home)
        btn_home.setOnClickListener {
            Log.e("Home button clicked", "Button is Clicked!")
            this.toast("Button is Clicked!")

            val intent = Intent(this, LandingActivity::class.java)
            startActivity(intent)
        }

        val btn_profile = findViewById<LinearLayout>(R.id.btn_profile)
        btn_profile.setOnClickListener {
            Log.e("Profile button clicked", "Button is Clicked!")
            this.toast("Button is Clicked!")

            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

    }
}