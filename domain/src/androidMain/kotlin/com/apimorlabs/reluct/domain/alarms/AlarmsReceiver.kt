package com.apimorlabs.reluct.domain.alarms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.res.ResourcesCompat
import com.apimorlabs.reluct.common.models.domain.appInfo.Icon
import com.apimorlabs.reluct.domain.R
import com.apimorlabs.reluct.domain.alarms.AlarmReminderNotifications.getTaskReminderNotificationInfo
import com.apimorlabs.reluct.domain.usecases.tasks.GetTasksUseCase
import com.apimorlabs.reluct.system.services.notifications.NotificationData
import com.apimorlabs.reluct.system.services.notifications.SimpleAndroidNotification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class AlarmsReceiver : BroadcastReceiver(), KoinComponent {

    private val scope = CoroutineScope(SupervisorJob())
    private val getTasks: GetTasksUseCase by inject()

    override fun onReceive(context: Context, intent: Intent) {
        /**
         * Check for actions before acting on the [Intent]
         * Each Alarm has a predefined [Intent.setAction] at creating and
         * the actions & keys are present in [AlarmsKeys]
         */
        // Tasks Reminders
        if (intent.action == AlarmsKeys.TASK_REMINDER.action) {
            val pendingResult = goAsync()
            val taskId = intent.getStringExtra(AlarmsKeys.TASK_REMINDER.key) ?: ""
            scope.launch(Dispatchers.Default) {
                try {
                    val task = getTasks.getTask(taskId).first()
                    task?.let { data ->
                        val drawable = ResourcesCompat.getDrawable(
                            context.resources,
                            R.drawable.ic_baseline_fact_check_24,
                            null
                        )
                        val icon = drawable?.let { Icon(it) }
                        val notificationData = NotificationData(
                            iconProvider = icon,
                            title = context.getString(R.string.task_reminder_title, data.title),
                            content = context.getString(
                                R.string.task_reminder_desc,
                                data.description
                            ),
                            notificationId = 1,
                            notificationTag = data.id,
                            category = NotificationCompat.CATEGORY_REMINDER
                        )
                        val channelInfo = context.getTaskReminderNotificationInfo()
                        // TODO1: Remember to fix this
                        val uriString = "TaskDetailsDestination.taskDetailsDeepLink(data.id)"
                        val pendingIntent = AlarmReminderNotifications
                            .openNotificationPendingIntent(context, uriString)
                        val notification = SimpleAndroidNotification(
                            context = context,
                            notificationData = notificationData,
                            channelInfo = channelInfo,
                            onNotificationClick = { pendingIntent },
                            onRequestPermission = {
                                // TODO1: Request Permission
                            }
                        )
                        notification.show()
                    }
                } finally {
                    pendingResult.finish()
                }
            }
        }
    }
}
