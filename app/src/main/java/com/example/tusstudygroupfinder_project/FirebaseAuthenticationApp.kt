package com.example.tusstudygroupfinder_project

import android.app.Application
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FirebaseAuthenticationApp: Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize Firebase Firestore
        Firebase.firestore
    }
}