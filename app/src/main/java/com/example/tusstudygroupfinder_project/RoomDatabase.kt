package com.example.tusstudygroupfinder_project

import androidx.room.Database
import androidx.room.RoomDatabase

// Room Database Android Studio Help : https://www.youtube.com/watch?v=-LNg-K7SncM&ab_channel=CodeWithCal

@Database(entities = [GroupEntity::class, SessionEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun groupDao(): GroupDao
    abstract fun sessionDao(): SessionDao
}