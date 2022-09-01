package com.penguenlabs.pushnote.features.home.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.penguenlabs.pushnote.analytics.Event
import com.penguenlabs.pushnote.analytics.EventLogger
import com.penguenlabs.pushnote.data.local.entity.HistoryEntity
import com.penguenlabs.pushnote.features.home.data.HomeRepository
import com.penguenlabs.pushnote.pushnotification.sender.NotificationSender
import com.penguenlabs.pushnote.userdefault.pinnednotification.PinnedNoteUserDefault
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val pinnedNoteUserDefault: PinnedNoteUserDefault,
    private val notificationSender: NotificationSender,
    private val homeRepository: HomeRepository,
    private val eventLogger: EventLogger,
) : ViewModel() {

    var homeScreeState by mutableStateOf(
        HomeScreenState(
            isPinnedNote = pinnedNoteUserDefault.getUserDefault()
        )
    )
        private set

    fun onTextFieldValueChange(textFieldValue: String) {
        homeScreeState = homeScreeState.copy(
            textFieldValue = textFieldValue,
            isError = textFieldValue.isEmpty()
        )
    }

    fun onCheckedChange(isChecked: Boolean) {
        homeScreeState = homeScreeState.copy(isPinnedNote = isChecked)
    }

    fun sendNotification(pushNotificationText: String, isPinnedNote: Boolean) {
        homeScreeState = if (pushNotificationText.isEmpty()) {
            homeScreeState.copy(isError = true)
        } else {
            if (isPinnedNote) {
                notificationSender.sendPinnedNotification(pushNotificationText)
            } else {
                notificationSender.sendNotification(pushNotificationText)
            }

            insertHistory(pushNotificationText, isPinnedNote)

            logPush()

            homeScreeState.copy(textFieldValue = "", isError = false)
        }
    }

    fun updateScreenState() {
        homeScreeState = homeScreeState.copy(isPinnedNote = pinnedNoteUserDefault.getUserDefault())
    }

    private fun insertHistory(pushNotificationText: String, isPinnedNote: Boolean) {
        viewModelScope.launch {
            homeRepository.insertHistory(
                HistoryEntity(
                    note = pushNotificationText,
                    time = System.currentTimeMillis(),
                    isPinnedNote = isPinnedNote
                )
            )
        }
    }

    private fun logPush() {
        eventLogger.log(Event.Push)
    }
}