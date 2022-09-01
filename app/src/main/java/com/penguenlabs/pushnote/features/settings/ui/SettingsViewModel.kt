package com.penguenlabs.pushnote.features.settings.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.penguenlabs.pushnote.analytics.Event
import com.penguenlabs.pushnote.analytics.EventLogger
import com.penguenlabs.pushnote.userdefault.darkmode.DarkModeUserDefault
import com.penguenlabs.pushnote.userdefault.pinnednotification.PinnedNoteUserDefault
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val darkModeUserDefault: DarkModeUserDefault,
    private val pinnedNoteUserDefault: PinnedNoteUserDefault,
    private val eventLogger: EventLogger
) : ViewModel() {

    var settingsScreenState by mutableStateOf(
        SettingsScreenState(
            darkModeEnabled = darkModeUserDefault.getUserDefault(),
            defaultPinnedNoteEnabled = pinnedNoteUserDefault.getUserDefault()
        )
    )
        private set

    fun setDarkModeUserDefault(value: Boolean) {
        darkModeUserDefault.setUserDefault(value)
        settingsScreenState = settingsScreenState.copy(darkModeEnabled = value)
    }

    fun setPinnedNoteUserDefault(value: Boolean) {
        pinnedNoteUserDefault.setUserDefault(value)
        settingsScreenState = settingsScreenState.copy(defaultPinnedNoteEnabled = value)
    }

    fun onShareApplicationClick() {
        eventLogger.log(Event.ShareApplication)
    }

    fun onReportProblemClick() {
        eventLogger.log(Event.ReportProblem)
    }

    fun onRateUsClick() {
        eventLogger.log(Event.RateUs)
    }
}