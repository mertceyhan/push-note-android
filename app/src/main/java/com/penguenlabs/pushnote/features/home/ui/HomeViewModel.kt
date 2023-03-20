package com.penguenlabs.pushnote.features.home.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.penguenlabs.pushnote.analytics.Event
import com.penguenlabs.pushnote.analytics.EventLogger
import com.penguenlabs.pushnote.data.local.entity.HistoryEntity
import com.penguenlabs.pushnote.features.home.data.HomeRepository
import com.penguenlabs.pushnote.pushnotification.counter.NotificationCounter
import com.penguenlabs.pushnote.pushnotification.sender.NotificationSender
import com.penguenlabs.pushnote.userdefault.pinnednotification.PinnedNoteUserDefault
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val pinnedNoteUserDefault: PinnedNoteUserDefault,
    private val notificationSender: NotificationSender,
    private val homeRepository: HomeRepository,
    private val eventLogger: EventLogger,
    private val notificationCounter: NotificationCounter
) : ViewModel() {

    var homeScreeState by mutableStateOf(
        HomeScreenState(
            isPinnedNote = pinnedNoteUserDefault.getUserDefault()
        )
    )
        private set

    private val _inAppReviewLauncher = MutableSharedFlow<Unit>()
    val inAppReviewLauncher = _inAppReviewLauncher.asSharedFlow()

    private val requiredNotificationCountRange = 8..10

    fun onTextFieldValueChange(textFieldValue: String) {
        homeScreeState = homeScreeState.copy(
            textFieldValue = textFieldValue, isError = textFieldValue.isEmpty()
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

            logPush(pushNotificationText, isPinnedNote)

            launchInAppReview()

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

    private fun logPush(pushNotificationText: String, isPinnedNote: Boolean) {
        eventLogger.log(
            Event.Push, bundleOf(
                Pair(Event.Push.PARAM_KEY_PUSH_NOTIFICATION_TEXT, pushNotificationText),
                Pair(Event.Push.PARAM_KEY_IS_PINNED_NOTE, isPinnedNote)
            )
        )
    }

    private fun launchInAppReview() {
        if (requiredNotificationCountRange.contains(notificationCounter.getNotificationCount())) {
            viewModelScope.launch {
                _inAppReviewLauncher.emit(Unit)
            }
            eventLogger.log(Event.InAppReviewLaunched)
        }
    }
}