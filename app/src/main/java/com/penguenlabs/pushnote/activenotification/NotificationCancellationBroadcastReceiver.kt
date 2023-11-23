package com.penguenlabs.pushnote.activenotification

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.penguenlabs.pushnote.analytics.Event
import com.penguenlabs.pushnote.analytics.EventLogger
import com.penguenlabs.pushnote.pushnotification.sender.NotificationSender
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

    @Inject
    lateinit var notificationSender: NotificationSender

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
        val notificationEntityId = intent.extras?.getLong(KEY_NOTIFICATION_ENTITY_ID)
        val isPinnedNote = intent.extras?.getBoolean(KEY_IS_PINNED_NOTE)
        val pushNotificationText = intent.extras?.getString(KEY_PUSH_NOTIFICATION_TEXT).orEmpty()

        if (intent.action == ACTION_CANCELLED && notificationEntityId != null) {

            // Log the notification cancelled event
            eventLogger.log(Event.NotificationCancelled)

            scope.launch {
                if (isPinnedNote == true) {
                    resendNotificationIfIsAndroid14(notificationEntityId, pushNotificationText)
                } else {
                    // Mark the notification as canceled
                    activeNotificationManager.markAsCancelledById(notificationEntityId)
                }
            }
        }
    }

    /**
     * Resends a pinned notification in case the user is on Android 14.
     * Notifications on Android 14 can be dismissed by swipe even if they are pinned.
     * The "Clear All" button doesn't dismiss the notification.
     * This function works around the issue by resending the notification
     * until the user presses the "Unpin" button.
     *
     * @param notificationEntityId The ID of the notification entity.
     * @param pushNotificationText The text to be displayed in the notification.
     */
    private fun resendNotificationIfIsAndroid14(
        notificationEntityId: Long, pushNotificationText: String
    ) {
        // Check if the Android version is 14
        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.TIRAMISU) {
            // Resend the pinned notification
            notificationSender.sendPinnedNotification(notificationEntityId, pushNotificationText)
        }
    }

    companion object {

        private const val ACTION_CANCELLED = "ACTION_CANCELLED"
        private const val KEY_NOTIFICATION_ENTITY_ID = "KEY_NOTIFICATION_ENTITY_ID"
        private const val KEY_IS_PINNED_NOTE = "KEY_IS_PINNED_NOTE"
        private const val KEY_PUSH_NOTIFICATION_TEXT = "KEY_PUSH_NOTIFICATION_TEXT"

        /**
         * Get a pending intent for NotificationCancellationBroadcastReceiver.
         *
         * @param context The application context.
         * @param notificationId The notification ID.
         * @param notificationEntityId The notification entity ID.
         * @param isPinnedNote `true` if the notification is pinned.
         * @param pushNotificationText The notification text.
         * @return A PendingIntent for the canceled notification event.
         */
        fun getPendingIntent(
            context: Context,
            notificationId: Int,
            notificationEntityId: Long,
            isPinnedNote: Boolean,
            pushNotificationText: String
        ): PendingIntent {
            val cancelledNotificationIntent =
                Intent(context, NotificationCancellationBroadcastReceiver::class.java).apply {
                    action = ACTION_CANCELLED
                    putExtra(KEY_NOTIFICATION_ENTITY_ID, notificationEntityId)
                    putExtra(KEY_IS_PINNED_NOTE, isPinnedNote)
                    putExtra(KEY_PUSH_NOTIFICATION_TEXT, pushNotificationText)
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