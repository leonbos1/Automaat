package com.example.automaat.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi

class NotificationReceiver : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onReceive(context: Context?, intent: Intent?) {
        val notificationManager = AutomaatNotificationManager()

        notificationManager.sendNotification(context!!, "Automaat", "Your rental has started!")
    }
}