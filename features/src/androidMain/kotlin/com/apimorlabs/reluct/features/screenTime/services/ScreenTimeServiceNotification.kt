package com.apimorlabs.reluct.features.screenTime.services

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.res.ResourcesCompat
import com.apimorlabs.reluct.common.models.domain.appInfo.Icon
import com.apimorlabs.reluct.common.models.util.getBitmap
import com.apimorlabs.reluct.common.models.util.toByteArrayUncompressed
import com.apimorlabs.reluct.features.R
import com.apimorlabs.reluct.system.services.notifications.Channels.getAppAlertsChannel
import com.apimorlabs.reluct.system.services.notifications.NotificationChannelInfo
import com.apimorlabs.reluct.system.services.notifications.NotificationData
import com.apimorlabs.reluct.system.services.notifications.SimpleAndroidNotification
import com.apimorlabs.reluct.system.services.notifications.createNotificationChannel

object ScreenTimeServiceNotification {
    fun createNotification(
        context: Context,
        title: String,
        content: String,
        onNotificationClick: () -> PendingIntent?
    ): Notification {
        val notificationManager = NotificationManagerCompat.from(context)
        val channelInfo = context.screenTimeChannelInfo
        channelInfo.createNotificationChannel(notificationManager, showBadge = false)
        val builder = NotificationCompat.Builder(context, channelInfo.channelId).apply {
            setSmallIcon(R.drawable.ic_twotone_aod_24)
            setContentTitle(title)
            setContentText(content)
            priority = NotificationCompat.PRIORITY_LOW
            onNotificationClick()?.let { intent ->
                setContentIntent(intent)
            }
            setAutoCancel(false)
            setCategory(NotificationCompat.CATEGORY_STATUS)
            setTicker(title)
            setShowWhen(false)
        }
        return builder.build()
    }

    fun overlayPermissionNotification(context: Context): SimpleAndroidNotification {
        val drawable =
            ResourcesCompat.getDrawable(context.resources, R.drawable.ic_twotone_report_24, null)
        val icon = drawable?.let { Icon(it.getBitmap()?.toByteArrayUncompressed() ?: ByteArray(0)) }
        val notificationData = NotificationData(
            iconProvider = icon,
            title = context.getString(R.string.overlay_perm_notif_title),
            content = context.getString(R.string.overlay_perm_notif_content),
            notificationId = NOTIFICATION_ID * 2,
            notificationTag = "overlay_permission",
            category = NotificationCompat.CATEGORY_REMINDER
        )
        return SimpleAndroidNotification(
            context = context,
            notificationData = notificationData,
            channelInfo = getAppAlertsChannel(context),
            onNotificationClick = { context.openOverlaySettings() },
            onRequestPermission = {}
        )
    }

    private fun Context.openOverlaySettings(): PendingIntent = PendingIntent.getActivity(
        this,
        0,
        Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package: ${this.packageName}")
        ),
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    private val Context.screenTimeChannelInfo
        get() = NotificationChannelInfo(
            name = getString(R.string.screen_time_service_channel_name),
            description = getString(R.string.screen_time_service_channel_desc),
            channelId = "screen_time_limit_service",
            importance = NotificationManagerCompat.IMPORTANCE_LOW
        )

    const val NOTIFICATION_ID = 2143
}
