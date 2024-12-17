package com.example.tusstudygroupfinder_project

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * TUS Study Group Finder Project
 * File: RoomDatabase.kt
 * Description: Configures the Room database for the app. Includes GroupEntity and SessionEntity as database tables.
 *              Provides DAOs for data access and manipulation.
 * Author: Kristers Rakstins - K00273773
 * Reference: https://www.youtube.com/watch?v=-LNg-K7SncM&ab_channel=CodeWithCal + Lab exercise
 */

// Room Database Android Studio Help : https://www.youtube.com/watch?v=-LNg-K7SncM&ab_channel=CodeWithCal

@Database(entities = [GroupEntity::class, SessionEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun groupDao(): GroupDao
    abstract fun sessionDao(): SessionDao
}