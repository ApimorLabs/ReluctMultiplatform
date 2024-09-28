package com.apimorlabs.reluct.features.screenTime.ui.overlay

import android.content.Context
import android.util.Log
import android.view.View
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.apimorlabs.reluct.compose.ui.theme.ReluctAppTheme
import com.apimorlabs.reluct.compose.ui.theme.Theme
import com.apimorlabs.reluct.data.source.settings.MultiplatformSettings
import com.apimorlabs.reluct.features.screenTime.statistics.AppScreenTimeStatsViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class AppLimitedOverlayView(
    private val context: Context,
    private val viewModel: AppScreenTimeStatsViewModel,
    private val exit: (View) -> Unit
) : KoinComponent {

    private val preferences: MultiplatformSettings by inject()

    fun getView() = ComposeView(context).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnDetachedFromWindow)
        setContent {
            Log.e("AppLimitedOverlayView", "Overlay UI not Implemented")
            val themeValue = Theme.MATERIAL_YOU.themeValue
            ReluctAppTheme(theme = themeValue) {
                // Put Your Stuff Here
                val composeHaptics = LocalHapticFeedback.current
                val focusModeOn by preferences.focusMode.collectAsState(initial = false)
                AppLimitedOverlayUI(
                    viewModel = viewModel,
                    focusModeOn = focusModeOn,
                    exit = {
                        composeHaptics.performHapticFeedback(HapticFeedbackType.LongPress)
                        exit(this)
                    }
                )
            }
        }
    }
}
