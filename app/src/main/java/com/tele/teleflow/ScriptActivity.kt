package com.tele.teleflow

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.Toast
import com.tele.teleflow.adapters.ScriptAdapter
import com.tele.teleflow.data.Script

class ScriptActivity : Activity(), ScriptAdapter.OnBookmarkClickListener {

    private lateinit var scriptAdapter: ScriptAdapter
    private val scripts = mutableListOf<Script>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_script)

        // Sample data
        scripts.addAll(listOf(
            Script("Project Pitch", "2 hours ago"),
            Script("Meeting Notes", "Yesterday"),
            Script("Product Demo", "2 days ago"),
            Script("Team Update", "1 week ago")
        ))

        // Setup ListView with custom adapter
        val listView = findViewById<ListView>(R.id.scripts_list)
        scriptAdapter = ScriptAdapter(this, scripts)
        scriptAdapter.bookmarkClickListener = this
        listView.adapter = scriptAdapter

        // Handle list item clicks
        listView.setOnItemClickListener { _, _, position, _ ->
            val script = scripts[position]
            Toast.makeText(this, "Selected: ${script.title}", Toast.LENGTH_SHORT).show()
        }

        // Settings button click listener
        findViewById<ImageButton>(R.id.btn_settings).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        // Create script button click listener
        findViewById<Button>(R.id.btn_create_script).setOnClickListener {
            Toast.makeText(this, "Create new script", Toast.LENGTH_SHORT).show()
        }

        // Navigation bar setup
        findViewById<LinearLayout>(R.id.btn_home).setOnClickListener {
            finish()
        }

        findViewById<LinearLayout>(R.id.btn_script).setOnClickListener {
            // We're already in the script screen
        }

        findViewById<LinearLayout>(R.id.btn_profile).setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }

    override fun onBookmarkClick(position: Int, script: Script) {
        Toast.makeText(
            this,
            "${script.title} ${if (script.isBookmarked) "bookmarked" else "unbookmarked"}",
            Toast.LENGTH_SHORT
        ).show()
    }
} 