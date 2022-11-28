package ua.rodev.buttontoactionapp.presentation.action.actions

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import ua.rodev.buttontoactionapp.R
import ua.rodev.buttontoactionapp.presentation.action.ActionUi
import ua.rodev.buttontoactionapp.presentation.main.MainActivity

class NotificationAction(
    private val context: Context,
    private val title: String = "Action is Notification!",
) : ActionUi {
    override fun perform() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL,
                NOTIFICATION_CHANNEL,
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.lockscreenVisibility = android.app.Notification.VISIBILITY_PUBLIC
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
        val resultIntent = Intent(context, MainActivity::class.java).apply {
            action = SHOW_CONTACTS_ACTION
        }

        val pendingIntentFlag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_IMMUTABLE
        } else {
            0
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            System.currentTimeMillis().toInt(),
            resultIntent,
            pendingIntentFlag
        )

        val builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(title)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setContentIntent(pendingIntent)
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
    }

    companion object {
        const val NOTIFICATION_CHANNEL = "NotificationChannel"
        const val SHOW_CONTACTS_ACTION = "ShowContactsAction"
    }
}
