package com.penguenlabs.pushnote.features.history.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.penguenlabs.pushnote.analytics.Event
import com.penguenlabs.pushnote.analytics.EventLogger
import com.penguenlabs.pushnote.data.local.entity.HistoryEntity
import com.penguenlabs.pushnote.features.history.data.HistoryRepository
import com.penguenlabs.pushnote.pushnotification.sender.NotificationSender
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val historyRepository: HistoryRepository,
    private val notificationSender: NotificationSender,
    private val eventLogger: EventLogger
) : ViewModel() {

    var historyScreenState by mutableStateOf(HistoryScreenState())
        private set

    @OptIn(FlowPreview::class)
    fun getAllHistory() {
        var isFirstCollect = true

        viewModelScope.launch {
            historyRepository.getAllHistory().debounce {
                if (isFirstCollect) 0 else 500
            }.collectLatest { historyItems ->
                isFirstCollect = false
                historyScreenState = historyScreenState.copy(
                    historyItems = historyItems
                )
            }
        }
    }

    fun sendNotification(pushNotificationText: String, isPinnedNote: Boolean) {
        if (isPinnedNote) {
            notificationSender.sendPinnedNotification(pushNotificationText)
        } else {
            notificationSender.sendNotification(pushNotificationText)
        }

        eventLogger.log(Event.Push)
    }

    fun onHistoryEntitySelect(isSelected: Boolean, historyEntity: HistoryEntity) {
        historyScreenState =
            historyScreenState.copy(selectedHistoryEntities = historyScreenState.selectedHistoryEntities.toMutableList()
                .apply {
                    if (isSelected) {
                        add(historyEntity)
                    } else {
                        remove(historyEntity)
                    }
                })
        eventLogger.log(Event.HistoryOnLongClicked)
    }

    fun onDeleteAllClick(selectedHistoryEntities: List<HistoryEntity>) {
        viewModelScope.launch {
            selectedHistoryEntities.forEach { historyEntry ->
                historyRepository.deleteHistory(historyEntry)
            }

            historyScreenState = historyScreenState.copy(selectedHistoryEntities = emptyList())
        }
        eventLogger.log(
            Event.HistoryDelete, bundleOf(
                Pair(
                    Event.HistoryDelete.PARAM_KEY_DELETED_ITEM_COUNT, selectedHistoryEntities.size
                )
            )
        )
    }

    fun onSelectAllClick() {
        historyScreenState =
            historyScreenState.copy(selectedHistoryEntities = historyScreenState.historyItems.toMutableList())
        eventLogger.log(Event.HistorySelectAll)
    }

    fun onDeselectAllClick() {
        historyScreenState = historyScreenState.copy(selectedHistoryEntities = emptyList())
        eventLogger.log(Event.HistoryDeselectAll)
    }
}