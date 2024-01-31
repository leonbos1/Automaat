package com.example.automaat.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import com.example.automaat.entities.RentalModel
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class NotificationScheduler {
    companion object {
        private const val RENTAL_STARTED_NOTIFICATION_ID = 100
        private const val RENTAL_ENDED_NOTIFICATION_ID = 101
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun scheduleNotification(
        context: Context,
        date: String,
        title: String,
        description: String,
        notificationId: Int
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(intent)
                return
            }
        }

        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("notificationId", notificationId)
            putExtra("title", title)
            putExtra("description", description)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notificationId,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val time = LocalTime.of(12, 0, 0)
        val dateTime = "$date $time"
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

        val dateTimeObject = LocalDateTime.parse(dateTime, formatter)

        val zonedDateTime = ZonedDateTime.of(dateTimeObject, ZoneId.systemDefault())
        val triggerTime = zonedDateTime.toInstant().toEpochMilli()

        try {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
        } catch (e: SecurityException) {
            println("Error while scheduling notification: $e")
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun scheduleRentalStartedNotification(context: Context, dateTime: String) {
        val title = "Rental Started"
        val description = "Your rental has started."
        scheduleNotification(context, dateTime, title, description, RENTAL_STARTED_NOTIFICATION_ID)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun scheduleRentalEndedNotification(context: Context, dateTime: String) {
        val title = "Rental Ended"
        val description = "Your rental has ended."
        scheduleNotification(context, dateTime, title, description, RENTAL_ENDED_NOTIFICATION_ID)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun scheduleRentalNotifications(context: Context, rental: RentalModel) {
        scheduleRentalStartedNotification(context, rental.fromDate)
        scheduleRentalEndedNotification(context, rental.toDate)
    }
}