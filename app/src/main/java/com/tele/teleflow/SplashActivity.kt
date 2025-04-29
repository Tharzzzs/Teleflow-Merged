package com.tele.teleflow

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Use a handler to delay loading the main app
        Handler(Looper.getMainLooper()).postDelayed({
            // Check if user is already logged in
            if (auth.currentUser != null) {
                // User is logged in, go to MainActivity
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                // User is not logged in, go to LoginActivity
                startActivity(Intent(this, LoginActivity::class.java))
            }
            // Close this activity
            finish()
        }, 1500) // 1.5 seconds delay
    }
}
