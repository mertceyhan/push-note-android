package com.penguenlabs.pushnote.pushnotification.sender

import android.annotation.SuppressLint
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.penguenlabs.pushnote.R
import com.penguenlabs.pushnote.pushnotification.broadcastreceiver.CopyBroadcastReceiver
import com.penguenlabs.pushnote.pushnotification.broadcastreceiver.UnpinBroadcastReceiver
import com.penguenlabs.pushnote.pushnotification.channel.MainNotificationChannel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.random.Random

class NotificationSender @Inject constructor(
    @ApplicationContext private val context: Context
) {

    @SuppressLint("UnspecifiedImmutableFlag")
    fun sendNotification(pushNotificationText: String) {
        val notificationId = Random.nextInt()

        val copyIntent = Intent(context, CopyBroadcastReceiver::class.java).apply {
            action = CopyBroadcastReceiver.ACTION_COPY
            putExtra(CopyBroadcastReceiver.KEY_NOTIFICATION_TEXT, pushNotificationText)
        }

        val copyPendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getBroadcast(
                context,
                notificationId,
                copyIntent,
                PendingIntent.FLAG_IMMUTABLE
            )
        } else {
            PendingIntent.getBroadcast(
                context,
                notificationId,
                copyIntent,
                0
            )
        }

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
            }
            .build()

        with(NotificationManagerCompat.from(context)) {
            notify(Random.nextInt(), notification)
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    fun sendPinnedNotification(pushNotificationText: String) {
        val notificationId = Random.nextInt()

        val unpinIntent = Intent(context, UnpinBroadcastReceiver::class.java).apply {
            action = UnpinBroadcastReceiver.ACTION_UNPIN
            putExtra(UnpinBroadcastReceiver.KEY_NOTIFICATION_ID, notificationId)
        }

        val copyIntent = Intent(context, CopyBroadcastReceiver::class.java).apply {
            action = CopyBroadcastReceiver.ACTION_COPY
            putExtra(CopyBroadcastReceiver.KEY_NOTIFICATION_TEXT, pushNotificationText)
        }

        val unpinPendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getBroadcast(
                context,
                notificationId,
                unpinIntent,
                PendingIntent.FLAG_IMMUTABLE
            )
        } else {
            PendingIntent.getBroadcast(
                context,
                notificationId,
                unpinIntent,
                0
            )
        }

        val copyPendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getBroadcast(
                context,
                notificationId,
                copyIntent,
                PendingIntent.FLAG_IMMUTABLE
            )
        } else {
            PendingIntent.getBroadcast(
                context,
                notificationId,
                copyIntent,
                0
            )
        }

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
            }
            .build()

        notification.apply {
            flags = Notification.FLAG_NO_CLEAR or Notification.FLAG_ONGOING_EVENT
        }

        with(NotificationManagerCompat.from(context)) {
            notify(notificationId, notification)
        }
    }
}