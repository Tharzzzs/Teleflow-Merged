package com.tele.teleflow

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ListView
import android.widget.LinearLayout
import com.tele.teleflow.utils.toast

class LandingActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)

        val scriptTitles = listOf(
            "Project Pitch",
            "YouTube Intro",
            "Podcast Episode 3"
        )

        val listView = findViewById<ListView>(R.id.recent_scripts_list)
        val adapter = ArrayAdapter(
            this,
            R.layout.simple_list_item,
            android.R.id.text1,
            scriptTitles
        )
        
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            toast("Selected: ${scriptTitles[position]}")
        }

        val settingsButton = findViewById<ImageButton>(R.id.btn_settings)
        settingsButton.setOnClickListener {
            toast("Button is Clicked!")
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.btn_home).setOnClickListener {
            toast("Button is Clicked!")
            // We're already in the home screen
        }

        findViewById<LinearLayout>(R.id.btn_script).setOnClickListener {
            toast("Button is Clicked!")
            startActivity(Intent(this, ScriptActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.btn_profile).setOnClickListener {
            toast("Button is Clicked!")
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }
}