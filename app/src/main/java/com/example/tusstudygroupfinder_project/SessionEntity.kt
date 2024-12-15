package com.example.tusstudygroupfinder_project

import androidx.room.Entity
import androidx.room.PrimaryKey

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