package ua.rodev.buttontoactionapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.animation.Animation.RELATIVE_TO_SELF
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.Button
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import ua.rodev.buttontoactionapp.presentation.main.MainActivity
import javax.inject.Inject

// TODO delete and use only ActionUi
enum class ActionType(val value: String) {
    Animation("animation"),
    Toast("toast"),
    Call("toast"),
    Notification("toast"),
    None("");

    companion object {
        fun fromType(userStatus: String) = values().first { it.value == userStatus }
    }
}

sealed class ActionUi {

    abstract fun action()

    class Animation(private val button: Button, private val duration: Long = DURATION) :
        ActionUi() {
        override fun action() {
            val rotate = RotateAnimation(
                FROM_DEGREES,
                TO_DEGREES,
                RELATIVE_TO_SELF,
                PIVOT_X_VALUE,
                RELATIVE_TO_SELF,
                PIVOT_X_VALUE
            )
            rotate.duration = duration
            rotate.interpolator = LinearInterpolator()

            button.startAnimation(rotate)
        }

        companion object {
            private const val FROM_DEGREES = 0f
            private const val TO_DEGREES = 360f
            private const val PIVOT_X_VALUE = 0.5f
            private const val DURATION = 2000L
        }
    }

    class ToastMessage @Inject constructor(
        private val context: Context,
        private val message: String = "Animation action",
    ) : ActionUi() {
        override fun action() {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    class Call : ActionUi() {
        override fun action() {
            // TODO obtain action
        }
    }

    class Notification(
        private val context: Context,
        private val title: String = "Action is Notification!",
    ) : ActionUi() {
        override fun action() {
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
                putExtra(NOTIFICATION_TITLE, title)
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            }

            val pendingIntent = PendingIntent.getActivity(
                context,
                System.currentTimeMillis().toInt(),
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
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
            const val NOTIFICATION_TITLE = "NotificationTitle"
            const val SHOW_CONTACTS_ACTION = "ShowContactsAction"
        }
    }
}
