package com.example.tusstudygroupfinder_project

import androidx.room.Entity
import androidx.room.PrimaryKey

// Entity for Groups
@Entity(tableName = "groups")
data class GroupEntity(
    @PrimaryKey val groupId: String,
    val groupName: String,
    val course: String,
    val isPublic: Boolean
)


