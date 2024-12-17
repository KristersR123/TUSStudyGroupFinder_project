package com.example.tusstudygroupfinder_project

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * TUS Study Group Finder Project
 * File: GroupDao.kt
 * Description: DAO for managing database operations related to groups using Room.
 *              Supports insertion and querying of groups.
 * Author: Kristers Rakstins - K00273773
 */

// Group Operations
@Dao
interface GroupDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroups(groups: List<GroupEntity>)

    @Query("SELECT * FROM groups")
    suspend fun getGroups(): List<GroupEntity>
}
