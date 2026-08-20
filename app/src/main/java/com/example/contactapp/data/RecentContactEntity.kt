package com.example.contactapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recent_contacts")
data class RecentContactEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val phone: String,
    val photoUrl: String?,
    val timestamp: Long
)
