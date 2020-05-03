package com.delbel.fluent.location.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import com.delbel.fluent.location.R
import javax.inject.Inject

internal class NotificationCreator @Inject constructor(private val destination: Intent) {

    private lateinit var notificationManager: NotificationManager

    fun createAndDisplay(context: Context): Notification {
        notificationManager = getSystemService(context, NotificationManager::class.java)!!

        createNotificationChannelIfNeeded(context)

        val (icon, name, intent) = action(context)

        return NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(context.getString(R.string.notification_title))
            .setContentText(context.getString(R.string.notification_content))
            .setSmallIcon(R.drawable.ic_notifications)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setOngoing(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .addAction(icon, name, intent)
            .build()
    }

    private fun createNotificationChannelIfNeeded(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                context.getString(R.string.notification_title),
                IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun action(context: Context): Triple<Int, String, PendingIntent> {
        val actionName = context.getString(R.string.action_open_app)
        val destinationPendingIntent = PendingIntent.getActivity(context, 0, destination, 0)

        return Triple(R.drawable.ic_launch, actionName, destinationPendingIntent)
    }

    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "location_on_foreground_notification"
    }
}