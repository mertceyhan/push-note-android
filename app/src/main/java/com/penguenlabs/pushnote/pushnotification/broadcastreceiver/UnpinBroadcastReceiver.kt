package com.penguenlabs.pushnote.pushnotification.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat

class UnpinBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == ACTION_UNPIN) {
            val notificationId = intent.extras?.getInt(KEY_NOTIFICATION_ID)

            if (context != null && notificationId != null) {
                NotificationManagerCompat.from(context).cancel(notificationId)
            }
        }
    }

    companion object {
        const val ACTION_UNPIN = "ACTION_UNPIN"
        const val KEY_NOTIFICATION_ID = "KEY_NOTIFICATION_ID"
    }
}