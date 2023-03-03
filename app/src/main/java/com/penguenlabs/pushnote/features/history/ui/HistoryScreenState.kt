package com.penguenlabs.pushnote.features.history.ui

import com.penguenlabs.pushnote.data.local.entity.HistoryEntity

data class HistoryScreenState(
    val historyItems: List<HistoryEntity> = emptyList(),
    val selectedHistoryEntities: List<HistoryEntity> = emptyList()
) {
    fun hasHistory(): Boolean = historyItems.isNotEmpty()
    fun isSelectable(): Boolean = selectedHistoryEntities.isNotEmpty()
    fun isSelected(historyEntity: HistoryEntity): Boolean =
        selectedHistoryEntities.contains(historyEntity)

    fun selectedHistoryEntityCountString(): String = selectedHistoryEntities.count().toString()
}