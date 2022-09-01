package com.penguenlabs.pushnote.features.home.data.local

import com.penguenlabs.pushnote.data.local.dao.HistoryDao
import com.penguenlabs.pushnote.data.local.entity.HistoryEntity
import javax.inject.Inject

class HomeLocalDataSource @Inject constructor(
    private val historyDao: HistoryDao
) {

    suspend fun insertHistory(historyEntity: HistoryEntity) =
        historyDao.insertHistory(historyEntity)
}