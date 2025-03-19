package com.tele.teleflow

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import com.tele.teleflow.utils.toast

class SettingsActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val btn_devs = findViewById<Button>(R.id.btn_about_devs)
        btn_devs.setOnClickListener {
            Log.e("About button clicked", "Button is Clicked!")
            this.toast("Button is Clicked!")

            val intent = Intent(this, AboutDevelopersActivity::class.java)
            startActivity(intent)
        }

        val btn_back = findViewById<ImageButton>(R.id.btn_back)
        btn_back.setOnClickListener {
            Log.e("Back button clicked", "Button is Clicked!")
            this.toast("Button is Clicked!")

            val intent = Intent(this, LandingActivity::class.java)
            startActivity(intent)
        }


    }
}