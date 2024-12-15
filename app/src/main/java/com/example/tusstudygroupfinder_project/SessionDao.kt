package com.example.tusstudygroupfinder_project

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

// Sessions Operations
@Dao
interface SessionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSessions(sessions: List<SessionEntity>)

    @Query("SELECT * FROM sessions WHERE groupId = :groupId")
    suspend fun getSessionsForGroup(groupId: String): List<SessionEntity>
}