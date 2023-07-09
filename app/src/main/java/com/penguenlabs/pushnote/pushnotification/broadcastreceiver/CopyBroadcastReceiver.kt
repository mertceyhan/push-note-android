package com.penguenlabs.pushnote.pushnotification.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.penguenlabs.pushnote.R

class CopyBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == ACTION_COPY) {
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
        const val ACTION_COPY = "ACTION_COPY"
        const val KEY_NOTIFICATION_TEXT = "KEY_NOTIFICATION_TEXT"
    }
}