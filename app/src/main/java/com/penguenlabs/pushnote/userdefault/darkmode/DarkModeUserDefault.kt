package com.penguenlabs.pushnote.userdefault.darkmode

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.core.content.edit
import com.penguenlabs.pushnote.userdefault.UserDefault
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DarkModeUserDefault @Inject constructor(
    @ApplicationContext private val context: Context,
    private val sharedPreferences: SharedPreferences
) : UserDefault {

    override fun setUserDefault(defaultValue: Boolean) {
        sharedPreferences.edit(true) {
            putBoolean(KEY, defaultValue)
        }
    }

    override fun getUserDefault(): Boolean =
        sharedPreferences.getBoolean(KEY, isSystemInDarkTheme())

    private fun isSystemInDarkTheme(): Boolean {
        return context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == UI_MODE_NIGHT_YES
    }

    companion object {
        private const val KEY = "DARK_MODE_USER_DEFAULT"
    }
}