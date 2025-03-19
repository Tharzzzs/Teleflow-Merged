package com.tele.teleflow

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.LinearLayout
import com.tele.teleflow.utils.toast

class AboutDevelopersActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_developers)


        val btn_back = findViewById<ImageButton>(R.id.btn_back)
        btn_back.setOnClickListener {
            Log.e("Back button clicked", "Button is Clicked!")
            this.toast("Button is Clicked!")

            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        val btn_home = findViewById<LinearLayout>(R.id.btn_home)
        btn_home.setOnClickListener {
            Log.e("Home button clicked", "Button is Clicked!")
            this.toast("Button is Clicked!")

            val intent = Intent(this, LandingActivity::class.java)
            startActivity(intent)
        }
    }
}