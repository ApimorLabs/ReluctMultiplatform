package com.apimorlabs.reluct.domain.alarms

import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.apimorlabs.reluct.domain.usecases.tasks.ManageTasksAlarms
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class RestartAlarmsReceiver : BroadcastReceiver(), KoinComponent {

    private val scope = CoroutineScope(SupervisorJob())
    private val manageTasksAlarms: ManageTasksAlarms by inject()

    override fun onReceive(context: Context, intent: Intent) {
        if (
            intent.action == AlarmManager.ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED ||
            intent.action == Intent.ACTION_BOOT_COMPLETED ||
            intent.action == "android.intent.action.QUICKBOOT_POWERON" ||
            intent.action == "com.htc.intent.action.QUICKBOOT_POWERON"
        ) {
            val pendingResult = goAsync()
            Log.d(TAG, "Restarting Alarms")
            scope.launch(Dispatchers.Default) {
                try {
                    manageTasksAlarms.rescheduleAlarms()
                } finally {
                    Log.d(TAG, "Alarms Restarted!")
                    pendingResult.finish()
                }
            }
        }
    }

    companion object {
        const val TAG = "RestartAlarmsReceiver"
    }
}
