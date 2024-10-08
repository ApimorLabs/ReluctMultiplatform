package com.apimorlabs.reluct.system.services.notifications

import android.Manifest
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.apimorlabs.reluct.system.services.util.IconUtils.toDrawable

class SimpleAndroidNotification(
    private val context: Context,
    private val notificationData: NotificationData,
    private val channelInfo: NotificationChannelInfo,
    private val onNotificationClick: () -> PendingIntent?,
    private val onRequestPermission: () -> Unit
) {
    fun show() {
        val notificationManager = NotificationManagerCompat.from(context)
        val notification = makeNotification(context, notificationManager)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            onRequestPermission()
            return
        }
        notificationManager.notify(
            notificationData.notificationTag,
            notificationData.notificationId,
            notification
        )
    }

    fun cancel() {
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.cancel(
            notificationData.notificationTag,
            notificationData.notificationId
        )
    }

    private fun makeNotification(
        context: Context,
        notificationManager: NotificationManagerCompat
    ): Notification {
        channelInfo.createNotificationChannel(notificationManager)
        val bitmap = notificationData.iconProvider?.let { getBitmap(it.toDrawable(context)) }
        val iconCompat = bitmap?.let { IconCompat.createWithBitmap(bitmap) }
        val builder = NotificationCompat.Builder(context, channelInfo.channelId)
            .apply {
                iconCompat?.let { setSmallIcon(it) }
                setContentTitle(notificationData.title)
                setTicker(notificationData.title)
                if (notificationData.content.isNotBlank()) {
                    setContentText(notificationData.content)
                    setStyle(
                        NotificationCompat.BigTextStyle()
                            .bigText(notificationData.content)
                    )
                }
                priority = NotificationCompat.PRIORITY_DEFAULT
                onNotificationClick()?.let { intent ->
                    setContentIntent(intent)
                }
                setAutoCancel(true)
                setOnlyAlertOnce(true)
                setCategory(notificationData.category)
            }
        return builder.build()
    }

    private fun getBitmap(drawable: Drawable): Bitmap? =
        try {
            val bitmap: Bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        } catch (e: OutOfMemoryError) {
            // Handle the error
            println("Icon error: ${e.message}")
            null
        }
}
