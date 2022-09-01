package com.penguenlabs.pushnote.features.history.data

import com.penguenlabs.pushnote.data.local.entity.HistoryEntity
import com.penguenlabs.pushnote.features.history.data.local.HistoryLocalDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HistoryRepository @Inject constructor(
    private val historyLocalDataSource: HistoryLocalDataSource
) {

    fun getAllHistory(): Flow<List<HistoryEntity>> =
        historyLocalDataSource.getAllHistory()

    suspend fun deleteHistory(historyEntity: HistoryEntity) = withContext(Dispatchers.IO) {
        historyLocalDataSource.deleteHistory(historyEntity)
    }
}