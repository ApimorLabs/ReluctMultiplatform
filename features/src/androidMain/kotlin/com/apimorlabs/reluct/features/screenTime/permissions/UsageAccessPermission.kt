package com.apimorlabs.reluct.features.screenTime.permissions

import android.app.AppOpsManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Process
import android.provider.Settings
import androidx.core.app.NotificationCompat
import androidx.core.content.res.ResourcesCompat
import com.apimorlabs.reluct.common.models.domain.appInfo.Icon
import com.apimorlabs.reluct.common.models.util.getBitmap
import com.apimorlabs.reluct.common.models.util.toByteArrayUncompressed
import com.apimorlabs.reluct.features.R
import com.apimorlabs.reluct.system.services.notifications.Channels.getAppAlertsChannel
import com.apimorlabs.reluct.system.services.notifications.NotificationData
import com.apimorlabs.reluct.system.services.notifications.SimpleAndroidNotification

internal object UsageAccessPermission {

    fun isAllowed(
        context: Context
    ): Boolean {
        val appOps: AppOpsManager = context.getSystemService(Context.APP_OPS_SERVICE)
            as AppOpsManager
        val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            appOps.unsafeCheckOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                Process.myUid(),
                context.packageName
            )
        } else {
            @Suppress("DEPRECATION")
            appOps.checkOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                Process.myUid(),
                context.packageName
            )
        }
        return mode == AppOpsManager.MODE_ALLOWED
    }

    fun requestUsageAccessNotification(context: Context): SimpleAndroidNotification {
        val drawable =
            ResourcesCompat.getDrawable(context.resources, R.drawable.ic_twotone_report_24, null)
        val icon =
            drawable?.let { Icon(it.getBitmap()?.toByteArrayUncompressed() ?: byteArrayOf()) }
        val notificationData = NotificationData(
            iconProvider = icon,
            title = context.getString(R.string.no_usage_access_notif_title),
            content = context.getString(R.string.no_usage_access_notif_content),
            notificationId = NOTIFICATION_ID,
            notificationTag = NOTIFICATION_TAG,
            category = NotificationCompat.CATEGORY_REMINDER
        )
        return SimpleAndroidNotification(
            context = context,
            notificationData = notificationData,
            channelInfo = getAppAlertsChannel(context),
            onNotificationClick = { context.openDeviceUsageSettings() },
            onRequestPermission = {
                // TODO1: Fix this
            }
        )
    }

    private fun requestIntent() = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)

    private fun Context.openDeviceUsageSettings(): PendingIntent = PendingIntent.getActivity(
        this,
        0,
        requestIntent(),
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    private const val NOTIFICATION_ID = 20
    private const val NOTIFICATION_TAG = "no_usage_access"
}
