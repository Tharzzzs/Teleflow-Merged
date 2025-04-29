package com.tele.teleflow

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.card.MaterialCardView
import com.google.android.material.switchmaterial.SwitchMaterial
import com.tele.teleflow.repository.AuthRepository
import com.tele.teleflow.utils.FirebaseUtils

class SettingsActivity : AppCompatActivity() {

    private lateinit var darkModeSwitch: SwitchMaterial
    private val authRepository = AuthRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Setup toolbar
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Settings"

        toolbar.setNavigationOnClickListener {
            finish()
        }

        // Setup dark mode switch
        darkModeSwitch = findViewById(R.id.dark_mode_switch)
        darkModeSwitch.isChecked = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES

        darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            FirebaseUtils.showToast(this, "Theme will change on app restart")
        }

        // Setup about developers button
        findViewById<MaterialCardView>(R.id.card_about_devs).setOnClickListener {
            Log.d("SettingsActivity", "About button clicked")
            startActivity(Intent(this, AboutDevelopersActivity::class.java))
        }

        // Setup other settings cards
        findViewById<MaterialCardView>(R.id.card_account).setOnClickListener {
            startActivity(Intent(this, ProfilePageActivity::class.java))
        }

        findViewById<MaterialCardView>(R.id.card_notifications).setOnClickListener {
            FirebaseUtils.showToast(this, "Notification settings")
        }

        findViewById<MaterialCardView>(R.id.card_privacy).setOnClickListener {
            FirebaseUtils.showToast(this, "Privacy settings")
        }

        findViewById<MaterialCardView>(R.id.card_help).setOnClickListener {
            FirebaseUtils.showToast(this, "Help & Support")
        }
    }
}