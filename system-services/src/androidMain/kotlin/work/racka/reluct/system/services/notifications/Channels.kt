package work.racka.reluct.system.services.notifications

import android.content.Context
import androidx.core.app.NotificationManagerCompat
import work.racka.reluct.system.services.R

object Channels {
    fun getAppAlertsChannel(context: Context) = NotificationChannelInfo(
        name = context.getString(R.string.app_alerts_notif_channel_title),
        description = context.getString(R.string.app_alerts_notif_channel_desc),
        channelId = context.getString(R.string.app_alerts_notif_channel_id),
        importance = NotificationManagerCompat.IMPORTANCE_HIGH
    )
}
