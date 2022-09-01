package com.penguenlabs.pushnote.features.history.data.local

import com.penguenlabs.pushnote.data.local.dao.HistoryDao
import com.penguenlabs.pushnote.data.local.entity.HistoryEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HistoryLocalDataSource @Inject constructor(
    private val historyDao: HistoryDao
) {

    fun getAllHistory(): Flow<List<HistoryEntity>> =
        historyDao.getAllHistory()

    suspend fun deleteHistory(historyEntity: HistoryEntity) =
        historyDao.deleteHistory(historyEntity)
}