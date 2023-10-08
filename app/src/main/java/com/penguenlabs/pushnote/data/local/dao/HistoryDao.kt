package com.penguenlabs.pushnote.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.penguenlabs.pushnote.data.local.entity.HistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {

    @Insert
    suspend fun insertHistory(historyEntity: HistoryEntity): Long

    @Query("SELECT * FROM historyentity ORDER BY id DESC")
    fun getAllHistory(): Flow<List<HistoryEntity>>

    @Delete
    suspend fun deleteHistory(historyEntity: HistoryEntity)

    @Query("UPDATE HistoryEntity SET active = 0 WHERE id = :id")
    suspend fun markAsCancelled(id: Long)

    @Query("SELECT * FROM HistoryEntity WHERE active = 1")
    suspend fun getActiveHistoryEntities(): List<HistoryEntity>
}