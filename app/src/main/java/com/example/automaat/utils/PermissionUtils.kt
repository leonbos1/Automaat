package com.example.automaat.utils

import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat

object PermissionUtils {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun checkNotificationPermission(context: Context): Boolean {
        return ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PERMISSION_GRANTED
    }
}