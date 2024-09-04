package com.apimorlabs.reluct.features.settings.states

import com.apimorlabs.reluct.data.source.settings.Keys

data class SettingsState(
    val themeValue: Int = Keys.Defaults.THEME,
    val limitSettings: LimitSettings = LimitSettings(),
)

data class LimitSettings(
    val dndOn: Boolean = false,
    val focusModeOn: Boolean = false,
    val appBlockingEnabled: Boolean = false
)
