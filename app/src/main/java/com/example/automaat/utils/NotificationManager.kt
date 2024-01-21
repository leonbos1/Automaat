package com.example.automaat.utils

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.automaat.MainActivity

class NotificationManager(private val application: Application) {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun sendNotification(channelId: String, title: String, message: String, icon: Int, notificationId: Int) {
        val descriptionText = "$channelId Notifications"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, channelId, importance).apply {
            description = descriptionText
        }
        val isPermissionGranted = PermissionUtils.checkNotificationPermission(application)
        val notificationManager: NotificationManager =
            application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
        println("LogNotification IsPremissionGranted: $isPermissionGranted")
        if (isPermissionGranted) {
            val builder = NotificationCompat.Builder(application, channelId)
                .setSmallIcon(icon)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            NotificationManagerCompat.from(application).notify(notificationId, builder.build())
        } else {
            MainActivity().requestNotificationPermission()
        }
    }
}
