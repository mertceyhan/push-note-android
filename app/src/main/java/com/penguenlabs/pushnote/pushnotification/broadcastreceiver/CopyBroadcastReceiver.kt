package com.penguenlabs.pushnote.pushnotification.broadcastreceiver

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.penguenlabs.pushnote.R
import com.penguenlabs.pushnote.analytics.Event
import com.penguenlabs.pushnote.analytics.EventLogger
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CopyBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var eventLogger: EventLogger

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == ACTION_COPY) {
            // Log the copy event
            eventLogger.log(Event.Copy)

            val pushNotificationText = intent.extras?.getString(KEY_NOTIFICATION_TEXT)

            if (context != null && pushNotificationText != null) {
                val clipboard =
                    context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("", pushNotificationText)
                clipboard.setPrimaryClip(clip)

                Toast
                    .makeText(
                        context,
                        context.getString(R.string.copied),
                        Toast.LENGTH_LONG
                    )
                    .show()
            }
        }
    }

    companion object {
        private const val ACTION_COPY = "ACTION_COPY"
        private const val KEY_NOTIFICATION_TEXT = "KEY_NOTIFICATION_TEXT"

        @SuppressLint("UnspecifiedImmutableFlag")
        fun getPendingIntent(
            context: Context,
            notificationId: Int,
            pushNotificationText: String
        ): PendingIntent {
            val copyIntent = Intent(context, CopyBroadcastReceiver::class.java).apply {
                action = ACTION_COPY
                putExtra(KEY_NOTIFICATION_TEXT, pushNotificationText)
            }

            return PendingIntent.getBroadcast(
                context,
                notificationId,
                copyIntent,
                PendingIntent.FLAG_IMMUTABLE
            )
        }
    }
}