package com.penguenlabs.pushnote

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import com.penguenlabs.pushnote.pushnotification.channel.MainNotificationChannel
import dagger.hilt.android.HiltAndroidApp

@Suppress("unused")
@HiltAndroidApp
class PushNoteApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannels()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannels() {
        with(NotificationManagerCompat.from(this)) {
            createNotificationChannel(
                MainNotificationChannel.getNotificationChannel(context = this@PushNoteApplication)
            )
        }
    }
}