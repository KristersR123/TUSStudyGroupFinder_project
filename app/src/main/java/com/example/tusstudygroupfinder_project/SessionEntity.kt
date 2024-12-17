package com.example.tusstudygroupfinder_project

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * TUS Study Group Finder Project
 * File: SessionEntity.kt
 * Description: Data model for the "sessions" table in the Room database.
 *              Represents a session with attributes like sessionId, groupId, title, date, time, and location.
 * Author: Kristers Rakstins - K00273773
 */

// Entity for Sessions
@Entity(tableName = "sessions")
data class SessionEntity(
    @PrimaryKey val sessionId: String,
    val groupId: String,
    val title: String,
    val date: String,
    val time: String,
    val location: String,
    val description: String?
)