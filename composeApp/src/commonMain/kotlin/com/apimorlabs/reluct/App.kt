package com.apimorlabs.reluct

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import com.apimorlabs.reluct.compose.ui.theme.ReluctAppTheme
import com.apimorlabs.reluct.compose.ui.theme.Theme
import com.apimorlabs.reluct.features.settings.GetSettings
import com.apimorlabs.reluct.navigation.destinations.SettingsCheck
import com.apimorlabs.reluct.navigation.navhost.AppNavHost
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext
import org.koin.compose.koinInject

@Composable
@Preview
fun App(settings: GetSettings? = null) {
    ReluctAppTheme {
        KoinContext {
            val localSettings = settings ?: koinInject<GetSettings>()
            ReluctMainCompose(localSettings)
        }
    }
}

@Composable
private fun ReluctMainCompose(settings: GetSettings) {
    // Theming Stuff
    val themeValue by settings.theme.collectAsState(
        Theme.FOLLOW_SYSTEM.themeValue,
        Dispatchers.Main.immediate
    )

    // Settings for determining start destinations
    val settingsCheck = produceState<SettingsCheck?>(initialValue = null) {
        value = getSettingsCheck(settings)
    }

    ReluctAppTheme(theme = themeValue) {
        AppNavHost(settingsCheck = settingsCheck)
    }
}

/**
 * Provide Settings check
 */
private suspend fun getSettingsCheck(settings: GetSettings): SettingsCheck =
    withContext(Dispatchers.IO) {
        val appVersionCode = 1 // ?: (BuildConfig.VERSION_CODE)
        val loginSkipped = settings.loginSkipped.firstOrNull()
        val onBoardingShown = settings.onBoardingShown.firstOrNull()
        val savedVersionCode = settings.savedVersionCode.firstOrNull() ?: appVersionCode
        settings.saveVersionCode(appVersionCode)
        SettingsCheck(
            isOnBoardingDone = onBoardingShown ?: false,
            showChangeLog = appVersionCode > savedVersionCode,
            loginSkipped = loginSkipped ?: false
        )
    }
