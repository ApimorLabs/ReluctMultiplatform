package com.apimorlabs.reluct.features.screenTime.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.apimorlabs.reluct.data.source.settings.MultiplatformSettings
import com.apimorlabs.reluct.features.screenTime.permissions.UsageAccessPermission
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

internal class RestartScreenTimeServiceReceiver : BroadcastReceiver(), KoinComponent {

    override fun onReceive(context: Context, intent: Intent) {
        if (
            intent.action == Intent.ACTION_BOOT_COMPLETED ||
            intent.action == "android.intent.action.QUICKBOOT_POWERON" ||
            intent.action == "com.htc.intent.action.QUICKBOOT_POWERON"
        ) {
            val scope = MainScope()
            scope.launch {
                val settings = get<MultiplatformSettings>()
                val notification = UsageAccessPermission.requestUsageAccessNotification(context)

                val onBoarding = settings.onBoardingShown.firstOrNull()
                val isAppBlocking = settings.appBlockingEnabled.firstOrNull()
                val canContinue = onBoarding == true && isAppBlocking == true

                if (UsageAccessPermission.isAllowed(context) && canContinue) {
                    notification.cancel()
                    val service = Intent(context, ScreenTimeLimitService::class.java)
                    context.startForegroundService(service)
                } else {
                    notification.show()
                }
            }.invokeOnCompletion { scope.cancel() }
        }
    }
}
