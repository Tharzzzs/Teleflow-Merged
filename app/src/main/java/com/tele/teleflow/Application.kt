package com.tele.teleflow

import android.app.Application
import com.google.firebase.FirebaseApp

class Application : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize Firebase
        FirebaseApp.initializeApp(this)
    }
}
