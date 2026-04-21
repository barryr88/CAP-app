package com.crewq

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

class ShiftReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification = NotificationCompat.Builder(context, "crewq_notifications")
            .setSmallIcon(R.drawable.crewq_logo)
            .setContentTitle("Upcoming Shift!")
            .setContentText("Your shift starts in 15 minutes. Don't forget to clock in!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        manager.notify(1001, notification)
    }
}