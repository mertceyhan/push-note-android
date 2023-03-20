package com.penguenlabs.pushnote.pushnotification.counter

import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject
import javax.inject.Singleton

interface NotificationCounter {
    fun increaseNotificationCount()
    fun getNotificationCount(): Int
}

@Singleton
class NotificationCounterImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : NotificationCounter {

    override fun increaseNotificationCount() {
        sharedPreferences.edit(true) {
            putInt(KEY, getNotificationCount().plus(1))
        }
    }

    override fun getNotificationCount(): Int = sharedPreferences.getInt(KEY, 0)

    companion object {
        private const val KEY = "NOTIFICATION_COUNT"
    }
}