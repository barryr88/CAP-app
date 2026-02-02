package com.example.jobtracker

import android.app.Application
import com.google.firebase.FirebaseApp

class JobTrackerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}