package com.penguenlabs.pushnote.pushnotification.broadcastreceiver

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import com.penguenlabs.pushnote.activenotification.ActiveNotificationManager
import com.penguenlabs.pushnote.analytics.Event
import com.penguenlabs.pushnote.analytics.EventLogger
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class UnpinBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var activeNotificationManager: ActiveNotificationManager

    @Inject
    lateinit var eventLogger: EventLogger

    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default)

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == ACTION_UNPIN) {
            // Log the unpin event
            eventLogger.log(Event.Unpin)

            val notificationId = intent.extras?.getInt(KEY_NOTIFICATION_ID)
            val notificationEntityId = intent.extras?.getLong(KEY_NOTIFICATION_ENTITY_ID)

            if (context != null && notificationId != null && notificationEntityId != null) {
                NotificationManagerCompat.from(context).cancel(notificationId)

                scope.launch {
                    // Mark the notification as canceled
                    activeNotificationManager.markAsCancelledById(notificationEntityId)
                }
            }
        }
    }

    companion object {
        private const val ACTION_UNPIN = "ACTION_UNPIN"
        private const val KEY_NOTIFICATION_ID = "KEY_NOTIFICATION_ID"
        private const val KEY_NOTIFICATION_ENTITY_ID = "KEY_NOTIFICATION_ENTITY_ID"

        @SuppressLint("UnspecifiedImmutableFlag")
        fun getPendingIntent(
            context: Context,
            notificationId: Int,
            notificationEntityId: Long,
        ): PendingIntent {
            val unpinIntent = Intent(context, UnpinBroadcastReceiver::class.java).apply {
                action = ACTION_UNPIN
                putExtra(KEY_NOTIFICATION_ID, notificationId)
                putExtra(KEY_NOTIFICATION_ENTITY_ID, notificationEntityId)
            }

            return PendingIntent.getBroadcast(
                context,
                notificationId,
                unpinIntent,
                PendingIntent.FLAG_IMMUTABLE
            )
        }
    }
}