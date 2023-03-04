package com.penguenlabs.pushnote.userdefault.pinnednotification

import android.content.SharedPreferences
import androidx.core.content.edit
import com.penguenlabs.pushnote.userdefault.UserDefault
import javax.inject.Inject

class PinnedNoteUserDefault @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : UserDefault {

    override fun setUserDefault(defaultValue: Boolean) {
        sharedPreferences.edit(true) {
            putBoolean(KEY, defaultValue)
        }
    }

    override fun getUserDefault(): Boolean = sharedPreferences.getBoolean(KEY, true)

    companion object {
        private const val KEY = "PINNED_NOTE_USER_DEFAULT"
    }
}