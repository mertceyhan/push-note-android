package com.penguenlabs.pushnote.pushnotification.sender

import android.annotation.SuppressLint
import android.app.Notification
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.penguenlabs.pushnote.R
import com.penguenlabs.pushnote.activenotification.NotificationCancellationBroadcastReceiver
import com.penguenlabs.pushnote.pushnotification.broadcastreceiver.CopyBroadcastReceiver
import com.penguenlabs.pushnote.pushnotification.broadcastreceiver.UnpinBroadcastReceiver
import com.penguenlabs.pushnote.pushnotification.channel.MainNotificationChannel
import com.penguenlabs.pushnote.pushnotification.counter.NotificationCounter
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.random.Random

class NotificationSender @Inject constructor(
    @ApplicationContext private val context: Context,
    private val notificationCounter: NotificationCounter
) {

    @SuppressLint("UnspecifiedImmutableFlag", "MissingPermission")
    fun sendNotification(
        notificationEntityId: Long = Random.nextLong(),
        pushNotificationText: String
    ) {
        val notificationId = Random.nextInt()
        val copyPendingIntent = CopyBroadcastReceiver.getPendingIntent(
            context, notificationId, pushNotificationText
        )
        val cancelledNotificationPendingIntent =
            NotificationCancellationBroadcastReceiver.getPendingIntent(
                context,
                notificationId,
                notificationEntityId
            )

        val notification = NotificationCompat.Builder(context, MainNotificationChannel.CHANNEL_ID)
            .apply {
                setSmallIcon(R.drawable.ic_notification)
                setContentTitle(pushNotificationText)
                priority = NotificationCompat.PRIORITY_HIGH
                addAction(
                    R.drawable.ic_unpin_notification,
                    context.getString(R.string.copy),
                    copyPendingIntent
                )
                setDeleteIntent(cancelledNotificationPendingIntent)
            }
            .build()

        with(NotificationManagerCompat.from(context)) {
            notify(Random.nextInt(), notification)
        }

        notificationCounter.increaseNotificationCount()
    }

    @SuppressLint("UnspecifiedImmutableFlag", "MissingPermission")
    fun sendPinnedNotification(
        notificationEntityId: Long = Random.nextLong(),
        pushNotificationText: String
    ) {
        val notificationId = Random.nextInt()
        val unpinPendingIntent = UnpinBroadcastReceiver.getPendingIntent(
            context, notificationId, notificationEntityId
        )
        val copyPendingIntent = CopyBroadcastReceiver.getPendingIntent(
            context, notificationId, pushNotificationText
        )
        val cancelledNotificationPendingIntent =
            NotificationCancellationBroadcastReceiver.getPendingIntent(
                context,
                notificationId,
                notificationEntityId
            )

        val notification = NotificationCompat.Builder(context, MainNotificationChannel.CHANNEL_ID)
            .apply {
                setSmallIcon(R.drawable.ic_notification)
                setContentTitle(pushNotificationText)
                priority = NotificationCompat.PRIORITY_HIGH
                addAction(
                    R.drawable.ic_unpin_notification,
                    context.getString(R.string.copy),
                    copyPendingIntent
                )
                addAction(
                    R.drawable.ic_unpin_notification,
                    context.getString(R.string.unpin),
                    unpinPendingIntent
                )
                setDeleteIntent(cancelledNotificationPendingIntent)
            }
            .build()

        notification.apply {
            flags = Notification.FLAG_NO_CLEAR or Notification.FLAG_ONGOING_EVENT
        }

        with(NotificationManagerCompat.from(context)) {
            notify(notificationId, notification)
        }

        notificationCounter.increaseNotificationCount()
    }
}