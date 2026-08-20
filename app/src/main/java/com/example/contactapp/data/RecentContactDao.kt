package com.example.contactapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RecentContactDao {
    @Query("SELECT * FROM recent_contacts ORDER BY timestamp DESC LIMIT 10")
    suspend fun getRecentContacts(): List<RecentContactEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecentContact(contact: RecentContactEntity)
}
