package com.crewq

import android.app.Application
import com.google.android.libraries.places.api.Places
import com.google.firebase.FirebaseApp

class JobTrackerApplication : android.app.Application() {
    override fun onCreate() {
        super.onCreate()
        Places.initialize(applicationContext, "AIzaSyAkOPe1SSa6tO3ZqIOJ1EVw5zSJqBUi50g")
        FirebaseApp.initializeApp(this)
    }
}