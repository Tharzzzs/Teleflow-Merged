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

class ScriptActivity : Activity(), ScriptAdapter.OnBookmarkClickListener {

    private lateinit var scriptAdapter: ScriptAdapter
    private val scripts = mutableListOf<Script>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_script)

        scripts.addAll(listOf(
            Script("Project Pitch", "2 hours ago"),
            Script("Meeting Notes", "Yesterday"),
            Script("Product Demo", "2 days ago"),
            Script("Team Update", "1 week ago")
        ))

        val listView = findViewById<ListView>(R.id.scripts_list)
        scriptAdapter = ScriptAdapter(this, scripts)
        scriptAdapter.bookmarkClickListener = this
        listView.adapter = scriptAdapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val script = scripts[position]
            toast("Selected: ${script.title}")
        }

        findViewById<ImageButton>(R.id.btn_settings).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        findViewById<Button>(R.id.btn_create_script).setOnClickListener {
            toast("Create new script")
        }

        // Navigation bar setup
        findViewById<LinearLayout>(R.id.btn_home).setOnClickListener {
            finish()
        }

        findViewById<LinearLayout>(R.id.btn_script).setOnClickListener {
        }

        findViewById<LinearLayout>(R.id.btn_profile).setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
            finish()
        }
    }

    override fun onBookmarkClick(position: Int, script: Script) {
        toast("${script.title} ${if (script.isBookmarked) "bookmarked" else "unbookmarked"}")
    }
} 