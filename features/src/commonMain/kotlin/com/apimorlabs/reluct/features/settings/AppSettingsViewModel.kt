package com.apimorlabs.reluct.features.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apimorlabs.reluct.data.source.settings.MultiplatformSettings
import com.apimorlabs.reluct.features.settings.states.LimitSettings
import com.apimorlabs.reluct.features.settings.states.SettingsEvents
import com.apimorlabs.reluct.features.settings.states.SettingsState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.apimorlabs.reluct.features.screenTime.services.ScreenTimeServices

class AppSettingsViewModel(
    private val settings: MultiplatformSettings,
    private val screenTimeServices: ScreenTimeServices
) : ViewModel() {

    private val limitSettings = combine(
        settings.doNoDisturb,
        settings.focusMode,
        settings.appBlockingEnabled
    ) { dnd, focusMode, appBlocking ->
        LimitSettings(
            dndOn = dnd,
            focusModeOn = focusMode,
            appBlockingEnabled = appBlocking
        )
    }

    val uiState: StateFlow<SettingsState> = combine(
        settings.theme,
        limitSettings,
    ) { themeSelected, limitSettings ->
        SettingsState(
            themeValue = themeSelected,
            limitSettings = limitSettings
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SettingsState()
    )

    private val eventsChannel = Channel<SettingsEvents>()
    val events: Flow<SettingsEvents> = eventsChannel.receiveAsFlow()

    fun saveThemeSettings(themeValue: Int) {
        val saved = settings.saveThemeSettings(themeValue)
        if (saved) {
            eventsChannel.trySend(SettingsEvents.ThemeChanged(themeValue))
        }
    }

    fun toggleDnd(value: Boolean) {
        viewModelScope.launch {
            settings.saveDoNotDisturb(value)
            eventsChannel.send(SettingsEvents.DndChanged(value))
        }
    }

    fun toggleFocusMode(value: Boolean) {
        viewModelScope.launch {
            settings.saveFocusMode(value)
            eventsChannel.send(SettingsEvents.FocusModeChanged(value))
        }
    }

    fun toggleAppBlocking(value: Boolean) {
        viewModelScope.launch {
            if (settings.saveAppBlocking(value)) {
                eventsChannel.send(SettingsEvents.AppBlockingChanged(value))
                if (value) {
                    screenTimeServices.startLimitsService()
                } else {
                    screenTimeServices.stopLimitsService()
                }
            }
        }
    }
}
