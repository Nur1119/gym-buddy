package app.gymbuddy.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import app.gymbuddy.R
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationHelper @Inject constructor() {

    companion object {
        const val CHANNEL_MESSAGES = "gymbuddy_messages"
        const val CHANNEL_MATCHES = "gymbuddy_matches"
        const val CHANNEL_FRIENDS = "gymbuddy_friends"

        fun createChannels(context: Context) {
            val nm = context.getSystemService(NotificationManager::class.java) ?: return
            nm.createNotificationChannel(
                NotificationChannel(CHANNEL_MESSAGES, "Messages", NotificationManager.IMPORTANCE_HIGH).apply {
                    description = "New chat messages"
                }
            )
            nm.createNotificationChannel(
                NotificationChannel(CHANNEL_MATCHES, "Matches", NotificationManager.IMPORTANCE_HIGH).apply {
                    description = "New gym buddy matches"
                }
            )
            nm.createNotificationChannel(
                NotificationChannel(CHANNEL_FRIENDS, "Friends", NotificationManager.IMPORTANCE_DEFAULT).apply {
                    description = "Friend requests"
                }
            )
        }
    }

    fun showMessage(context: Context, senderName: String, text: String) {
        show(context, CHANNEL_MESSAGES, senderName, text, System.currentTimeMillis().toInt())
    }

    fun showMatch(context: Context, name: String) {
        show(context, CHANNEL_MATCHES, "New Match! 🏋️", "You matched with $name", 1001)
    }

    fun showFriendRequest(context: Context, name: String) {
        show(context, CHANNEL_FRIENDS, "Friend Request", "$name wants to be your gym buddy", 1002)
    }

    private fun show(context: Context, channel: String, title: String, body: String, id: Int) {
        val nm = NotificationManagerCompat.from(context)
        if (!nm.areNotificationsEnabled()) return
        val notif = NotificationCompat.Builder(context, channel)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()
        try { nm.notify(id, notif) } catch (_: SecurityException) {}
    }
}
