package com.penguenlabs.pushnote.pushnotification.channel

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.penguenlabs.pushnote.R

object MainNotificationChannel {

    const val CHANNEL_ID = "1"

    @RequiresApi(Build.VERSION_CODES.O)
    fun getNotificationChannel(context: Context): NotificationChannel = NotificationChannel(
        CHANNEL_ID,
        context.getString(R.string.main_notifications),
        NotificationManager.IMPORTANCE_HIGH
    )
}