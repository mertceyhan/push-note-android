package com.penguenlabs.pushnote.activenotification

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.penguenlabs.pushnote.analytics.Event
import com.penguenlabs.pushnote.analytics.EventLogger
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * BroadcastReceiver responsible for handling canceled notification events.
 *
 * This BroadcastReceiver captures notification cancellation events triggered when users swipe to dismiss notifications
 * or tap the "Clear All" button. Please note that notifications canceled programmatically using
 * "NotificationManagerCompat.from(context).cancel(notificationId)" will not be intercepted by this broadcast receiver.
 *
 * @property activeNotificationManager The manager responsible for handling active notifications.
 */
@AndroidEntryPoint
class NotificationCancellationBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var activeNotificationManager: ActiveNotificationManager

    @Inject
    lateinit var eventLogger: EventLogger

    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default)

    /**
     * Called when a broadcast is received.
     *
     * This method handles notification cancellation events and marks notifications as canceled.
     *
     * @param context The context in which the receiver is running.
     * @param intent The intent received by the receiver.
     */
    override fun onReceive(context: Context, intent: Intent) {
        val notificationEntityId =
            intent.extras?.getLong(KEY_NOTIFICATION_ENTITY_ID)

        if (intent.action == ACTION_CANCELLED && notificationEntityId != null) {

            // Log the notification cancelled event
            eventLogger.log(Event.NotificationCancelled)

            scope.launch {
                // Mark the notification as canceled
                activeNotificationManager.markAsCancelledById(notificationEntityId)
            }
        }
    }

    companion object {

        private const val ACTION_CANCELLED = "ACTION_CANCELLED"
        private const val KEY_NOTIFICATION_ENTITY_ID = "KEY_NOTIFICATION_ENTITY_ID"

        /**
         * Get a pending intent for NotificationCancellationBroadcastReceiver.
         *
         * @param context The application context.
         * @param notificationId The notification ID.
         * @param notificationEntityId The notification entity ID.
         * @return A PendingIntent for the canceled notification event.
         */
        fun getPendingIntent(
            context: Context,
            notificationId: Int,
            notificationEntityId: Long
        ): PendingIntent {
            val cancelledNotificationIntent =
                Intent(context, NotificationCancellationBroadcastReceiver::class.java).apply {
                    action = ACTION_CANCELLED
                    putExtra(KEY_NOTIFICATION_ENTITY_ID, notificationEntityId)
                }

            return PendingIntent.getBroadcast(
                context,
                notificationId,
                cancelledNotificationIntent,
                PendingIntent.FLAG_IMMUTABLE
            )
        }
    }
}