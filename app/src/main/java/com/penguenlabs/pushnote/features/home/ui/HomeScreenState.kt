package com.penguenlabs.pushnote.features.home.ui

data class HomeScreenState(
    val textFieldValue: String = "",
    val isPinnedNote: Boolean = false,
    val isError: Boolean = false
)