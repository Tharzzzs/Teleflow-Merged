package com.tele.teleflow

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ListView
import com.tele.teleflow.adapters.ScriptAdapter
import com.tele.teleflow.data.Script
import com.tele.teleflow.utils.toast

class ScriptActivity : Activity() {

    private lateinit var scriptAdapter: ScriptAdapter
    private val scripts = mutableListOf<Script>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_script)

        // Initialize sample scripts
        scripts.addAll(listOf(
            Script("Project Pitch", "2 hours ago"),
            Script("Meeting Notes", "Yesterday"),
            Script("Product Demo", "2 days ago"),
            Script("Team Update", "1 week ago")
        ))

        // Initialize ListView and adapter
        val listView = findViewById<ListView>(R.id.scripts_list)

        // Create the adapter with proper context and data
        scriptAdapter = ScriptAdapter(this, scripts)

        // Set up click listeners for the adapter
        scriptAdapter.setOnBookmarkClickListener { script ->
            script.isBookmarked = !script.isBookmarked
            scriptAdapter.notifyDataSetChanged()
            toast("${script.title} ${if (script.isBookmarked) "bookmarked" else "unbookmarked"}")
        }

        scriptAdapter.setOnDeleteClickListener { script ->
            scripts.remove(script)
            scriptAdapter.notifyDataSetChanged()
            toast("${script.title} deleted")
        }

        // Set the adapter to the ListView - this line might be causing the error
        // Make sure the ListView with ID scripts_list exists in activity_script.xml
        listView.adapter = scriptAdapter

        // Set up item click listener
        listView.setOnItemClickListener { _, _, position, _ ->
            val script = scripts[position]
            val intent = Intent(this, ScriptEditorActivity::class.java)
            intent.putExtra("scriptId", script.id)
            startActivity(intent)
        }

        // Set up button click listeners
        findViewById<ImageButton>(R.id.btn_settings).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        findViewById<Button>(R.id.btn_create_script).setOnClickListener {
            startActivity(Intent(this, ScriptEditorActivity::class.java))
        }

        // Navigation bar setup
        findViewById<LinearLayout>(R.id.btn_home).setOnClickListener {
            finish()
        }

        findViewById<LinearLayout>(R.id.btn_script).setOnClickListener {
            // Already on script screen
        }

        findViewById<LinearLayout>(R.id.btn_profile).setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
            finish()
        }
    }
}