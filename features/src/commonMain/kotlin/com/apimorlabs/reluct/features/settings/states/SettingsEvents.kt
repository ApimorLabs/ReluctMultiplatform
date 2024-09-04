package com.apimorlabs.reluct.features.settings.states

sealed class SettingsEvents {
    data object Nothing : SettingsEvents()
    class ThemeChanged(val themeValue: Int) : SettingsEvents()
    class FocusModeChanged(val isEnabled: Boolean) : SettingsEvents()
    class DndChanged(val isEnabled: Boolean) : SettingsEvents()
    class AppBlockingChanged(val isEnabled: Boolean) : SettingsEvents()
}
