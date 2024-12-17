package com.example.tusstudygroupfinder_project

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * TUS Study Group Finder Project
 * File: SessionDao.kt
 * Description: DAO for managing database operations related to sessions using Room.
 *              Supports insertion and querying of sessions by groupId.
 * Author: Kristers Rakstins - K00273773
 */

// Sessions Operations
@Dao
interface SessionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSessions(sessions: List<SessionEntity>)

    @Query("SELECT * FROM sessions WHERE groupId = :groupId")
    suspend fun getSessionsForGroup(groupId: String): List<SessionEntity>
}