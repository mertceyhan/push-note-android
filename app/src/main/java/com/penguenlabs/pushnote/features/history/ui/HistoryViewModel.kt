package com.penguenlabs.pushnote.features.history.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.penguenlabs.pushnote.analytics.Event
import com.penguenlabs.pushnote.analytics.EventLogger
import com.penguenlabs.pushnote.data.local.entity.HistoryEntity
import com.penguenlabs.pushnote.features.history.data.HistoryRepository
import com.penguenlabs.pushnote.pushnotification.sender.NotificationSender
import dagger.hilt.android.lifecycle.HiltViewModel
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

    fun getAllHistory() {
        viewModelScope.launch {
            historyRepository.getAllHistory().collect { historyItems ->
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

        getAllHistory()
    }

    fun onHistoryEntitySelect(historyEntity: HistoryEntity) {
        historyScreenState = historyScreenState.copy(selectedHistoryEntity = historyEntity)
    }

    fun onCopyClick() {
        eventLogger.log(Event.Copy)
    }

    fun onShareClick() {
        eventLogger.log(Event.Share)
    }

    fun onDeleteClick(historyEntity: HistoryEntity) {
        viewModelScope.launch {
            historyRepository.deleteHistory(historyEntity)
        }

        eventLogger.log(Event.Delete)
    }
}