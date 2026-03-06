package com.capapp.jobtracker

import android.app.Application
import com.google.android.libraries.places.api.Places
import com.google.firebase.FirebaseApp

class JobTrackerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Places.initialize(applicationContext, "AIzaSyAkOPe1SSa6tO3ZqIOJ1EVw5zSJqBUi50g")
        FirebaseApp.initializeApp(this)
    }
}