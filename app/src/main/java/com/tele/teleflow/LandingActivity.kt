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

        // Sample data for recent scripts
        val scriptTitles = listOf(
            "Project Pitch",
            "YouTube Intro",
            "Podcast Episode 3"
        )
        
        val scriptDates = listOf(
            "Last edited: 2 hours ago",
            "Last edited: Yesterday",
            "Last edited: 3 days ago"
        )

        // Setup ListView with adapter
        val listView = findViewById<ListView>(R.id.recent_scripts_list)
        val adapter = ArrayAdapter(
            this,
            R.layout.simple_list_item,
            android.R.id.text1,
            scriptTitles
        ).apply {
            setDropDownViewResource(R.layout.simple_list_item)
        }
        
        listView.adapter = adapter

        // Set click listener for script items
        listView.setOnItemClickListener { _, _, position, _ ->
            toast("Selected: ${scriptTitles[position]}")
            // TODO: Navigate to script editor
        }

        // Settings button click listener
        val settingsButton = findViewById<ImageButton>(R.id.btn_settings)
        settingsButton.setOnClickListener {
            toast("Button is Clicked!")
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        // Navigation bar click listeners
        findViewById<LinearLayout>(R.id.btn_home).setOnClickListener {
            toast("Button is Clicked!")
            startActivity(Intent(this, LandingActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.btn_profile).setOnClickListener {
            toast("Button is Clicked!")
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }
}