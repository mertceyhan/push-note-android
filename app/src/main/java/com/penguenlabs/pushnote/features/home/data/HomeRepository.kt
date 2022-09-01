package com.penguenlabs.pushnote.features.home.data

import com.penguenlabs.pushnote.data.local.entity.HistoryEntity
import com.penguenlabs.pushnote.features.home.data.local.HomeLocalDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val homeLocalDataSource: HomeLocalDataSource
) {

    suspend fun insertHistory(historyEntity: HistoryEntity) = withContext(Dispatchers.IO) {
        homeLocalDataSource.insertHistory(historyEntity)
    }
}