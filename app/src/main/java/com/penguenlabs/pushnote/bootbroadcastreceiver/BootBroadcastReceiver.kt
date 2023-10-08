package com.penguenlabs.pushnote.bootbroadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.penguenlabs.pushnote.activenotification.ActiveNotificationManager
import com.penguenlabs.pushnote.analytics.Event
import com.penguenlabs.pushnote.analytics.EventLogger
import com.penguenlabs.pushnote.pushnotification.sender.NotificationSender
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * BroadcastReceiver responsible for resending active notifications after a device boot.
 * This ensures that users can still see their push notifications even after a device restart.
 *
 * @property notificationManager The manager responsible for handling active notifications.
 * @property notificationSender The sender responsible for sending notifications.
 * @property eventLogger The logger for logging events.
 */
@AndroidEntryPoint
class BootBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationManager: ActiveNotificationManager

    @Inject
    lateinit var notificationSender: NotificationSender

    @Inject
    lateinit var eventLogger: EventLogger

    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default)

    /**
     * Called when a broadcast is received.
     *
     * This method retrieves and resends active notifications using the provided manager.
     *
     * @param context The context in which the receiver is running.
     * @param intent The intent received by the receiver.
     */
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {

            // Log the boot received event
            eventLogger.log(Event.BootReceived)

            scope.launch {
                // Retrieve active notifications
                val activeNotifications = notificationManager.getActiveNotifications()

                // Log the notifications saved after boot event if there are any
                if (activeNotifications.isNotEmpty()) {
                    eventLogger.log(Event.NotificationsSavedAfterBoot)
                }

                // Resend active notifications based on their type
                activeNotifications.forEach { notification ->
                    if (notification.isPinnedNote) {
                        notificationSender.sendPinnedNotification(
                            notificationEntityId = notification.id,
                            pushNotificationText = notification.note
                        )
                    } else {
                        notificationSender.sendNotification(
                            notificationEntityId = notification.id,
                            pushNotificationText = notification.note
                        )
                    }
                }
            }
        }
    }
}
