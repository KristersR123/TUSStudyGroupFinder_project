package com.example.tusstudygroupfinder_project

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * TUS Study Group Finder Project
 * File: GroupEntity.kt
 * Description: Data model for the "groups" table in the Room database.
 *              Represents a study group with attributes like groupId, groupName, course, and visibility.
 * Author: Kristers Rakstins - K00273773
 */

// Entity for Groups
@Entity(tableName = "groups")
data class GroupEntity(
    @PrimaryKey val groupId: String,
    val groupName: String,
    val course: String,
    val isPublic: Boolean
)


