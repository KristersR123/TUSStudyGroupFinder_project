package com.example.tusstudygroupfinder_project

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * TUS Study Group Finder Project
 * File: FirebaseAuthenticationApp.kt
 * Description: Application class for initialising Hilt dependency injection.
 * Author: Kristers Rakstins
 * Copied From Last Year's Project (Tus Campus Connect)
 */

@HiltAndroidApp
class FirebaseAuthenticationApp: Application() {

    override fun onCreate() {
        super.onCreate()
        
    }
}