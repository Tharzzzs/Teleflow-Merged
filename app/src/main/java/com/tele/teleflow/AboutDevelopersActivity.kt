package com.tele.teleflow

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tele.teleflow.adapters.DeveloperAdapter
import com.tele.teleflow.data.Developer

class AboutDevelopersActivity : AppCompatActivity() {

    private lateinit var developersRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_developers)

        // Setup toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "About Developers"

        toolbar.setNavigationOnClickListener {
            finish()
        }

        // Setup developers list
        developersRecyclerView = findViewById(R.id.developers_list)
        developersRecyclerView.layoutManager = LinearLayoutManager(this)

        val developers = listOf(
            Developer("John Doe", "Lead Developer", "john.doe@example.com"),
            Developer("Jane Smith", "UI/UX Designer", "jane.smith@example.com"),
            Developer("Alex Johnson", "Backend Developer", "alex.johnson@example.com"),
            Developer("Sarah Williams", "QA Engineer", "sarah.williams@example.com")
        )

        val adapter = DeveloperAdapter(developers)
        developersRecyclerView.adapter = adapter
    }
}
