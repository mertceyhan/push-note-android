package com.penguenlabs.pushnote.features

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.penguenlabs.pushnote.userdefault.darkmode.DarkModeUserDefault
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    darkModeUserDefault: DarkModeUserDefault
) : ViewModel() {

    var darkModeState by mutableStateOf(darkModeUserDefault.getUserDefault())
        private set

    fun onDarkModeChanged(value: Boolean) {
        darkModeState = value
    }
}