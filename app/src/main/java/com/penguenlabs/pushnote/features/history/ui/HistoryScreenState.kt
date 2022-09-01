package com.penguenlabs.pushnote.features.history.ui

import com.penguenlabs.pushnote.data.local.entity.HistoryEntity

data class HistoryScreenState(
    val historyItems: List<HistoryEntity> = emptyList(),
    val selectedHistoryEntity: HistoryEntity? = null
) {

    fun hasHistory() = historyItems.isNotEmpty()
}