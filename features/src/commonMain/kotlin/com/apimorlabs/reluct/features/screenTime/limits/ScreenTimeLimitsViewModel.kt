package com.apimorlabs.reluct.features.screenTime.limits

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apimorlabs.reluct.domain.usecases.limits.ManageDistractingApps
import com.apimorlabs.reluct.domain.usecases.limits.ManageFocusMode
import com.apimorlabs.reluct.domain.usecases.limits.ManagePausedApps
import com.apimorlabs.reluct.features.screenTime.states.DistractingAppsState
import com.apimorlabs.reluct.features.screenTime.states.FocusModeState
import com.apimorlabs.reluct.features.screenTime.states.PausedAppsState
import com.apimorlabs.reluct.features.screenTime.states.ScreenTimeLimitState
import com.apimorlabs.reluct.features.screenTime.states.events.ScreenTimeLimitsEvents
import com.apimorlabs.reluct.features.util.Constants
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ScreenTimeLimitsViewModel(
    private val manageFocusMode: ManageFocusMode,
    private val managePausedApps: ManagePausedApps,
    private val manageDistractingApps: ManageDistractingApps
) : ViewModel() {

    private val focusModeState: MutableStateFlow<FocusModeState> =
        MutableStateFlow(FocusModeState())
    private val pausedAppsState: MutableStateFlow<PausedAppsState> =
        MutableStateFlow(PausedAppsState.Nothing)
    private val distractingAppsState: MutableStateFlow<DistractingAppsState> =
        MutableStateFlow(DistractingAppsState.Nothing)

    val uiState: StateFlow<ScreenTimeLimitState> = combine(
        focusModeState,
        pausedAppsState,
        distractingAppsState
    ) { focusModeState, pausedAppsState, distractingAppsState ->
        ScreenTimeLimitState(
            focusModeState = focusModeState,
            pausedAppsState = pausedAppsState,
            distractingAppsState = distractingAppsState
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ScreenTimeLimitState()
    )

    private val _events = Channel<ScreenTimeLimitsEvents>(Channel.UNLIMITED)
    val events: Flow<ScreenTimeLimitsEvents> = _events.receiveAsFlow()

    private var focusModeJob: Job? = null
    private var distractingAppsJob: Job? = null
    private var pausedAppsJob: Job? = null

    init {
        initialize()
    }

    private fun initialize() {
        getFocusMode()
        getPausedApps()
    }

    fun getDistractingApps() {
        if (distractingAppsJob == null) {
            distractingAppsState.update { DistractingAppsState.Loading() }
            distractingAppsJob = viewModelScope.launch {
                manageDistractingApps.getApps().collectLatest { apps ->
                    // First is distracting apps, Second is Non Distracting apps
                    distractingAppsState.update {
                        DistractingAppsState.Data(
                            distractingApps = apps.first,
                            otherApps = apps.second
                        )
                    }
                }
            }
        }
    }

    fun toggleFocusMode(value: Boolean) {
        viewModelScope.launch {
            manageFocusMode.toggleFocusMode(value)
            if (value) {
                _events.send(
                    ScreenTimeLimitsEvents.ShowMessageDone(
                        true,
                        Constants.FOCUS_MODE_ON
                    )
                )
            } else {
                _events.send(
                    ScreenTimeLimitsEvents.ShowMessageDone(
                        false,
                        Constants.FOCUS_MODE_OFF
                    )
                )
            }
        }
    }

    fun toggleDnd(value: Boolean) {
        viewModelScope.launch {
            manageFocusMode.toggleDoNoDisturb(value)
            if (value) {
                _events.send(
                    ScreenTimeLimitsEvents.ShowMessageDone(
                        true,
                        Constants.DND_ON
                    )
                )
            } else {
                _events.send(
                    ScreenTimeLimitsEvents.ShowMessageDone(
                        false,
                        Constants.DND_OFF
                    )
                )
            }
        }
    }

    fun pauseApp(packageName: String) {
        viewModelScope.launch {
            managePausedApps.pauseApp(packageName)
            _events.send(
                ScreenTimeLimitsEvents.ShowMessageDone(
                    true,
                    Constants.MARK_PAUSED
                )
            )
        }
    }

    fun unPauseApp(packageName: String) {
        viewModelScope.launch {
            managePausedApps.unPauseApp(packageName)
            _events.send(
                ScreenTimeLimitsEvents.ShowMessageDone(
                    false,
                    Constants.UN_MARK_PAUSED
                )
            )
        }
    }

    fun markAsDistracting(packageName: String) {
        viewModelScope.launch {
            manageDistractingApps.markAsDistracting(packageName)
            _events.send(
                ScreenTimeLimitsEvents.ShowMessageDone(
                    true,
                    Constants.MARK_DISTRACTING
                )
            )
        }
    }

    fun markAsNonDistracting(packageName: String) {
        viewModelScope.launch {
            manageDistractingApps.markAsNotDistracting(packageName)
            _events.send(
                ScreenTimeLimitsEvents.ShowMessageDone(
                    false,
                    Constants.UN_MARK_DISTRACTING
                )
            )
        }
    }

    private fun getFocusMode() {
        focusModeJob?.cancel()
        focusModeJob = viewModelScope.launch {
            val focus = combine(
                manageFocusMode.isFocusModeOn,
                manageFocusMode.isDoNotDisturbOn
            ) { focusMode, dnd ->
                FocusModeState(
                    focusModeOn = focusMode,
                    doNotDisturbOn = dnd
                )
            }
            focus.collect { state ->
                focusModeState.update { state }
            }
        }
    }

    private fun getPausedApps() {
        pausedAppsJob?.cancel()
        pausedAppsState.update { PausedAppsState.Loading() }
        pausedAppsJob = viewModelScope.launch {
            managePausedApps.getApps().collectLatest { apps ->
                // First is paused apps, Second is un paused apps
                pausedAppsState.update {
                    PausedAppsState.Data(
                        pausedApps = apps.first,
                        unPausedApps = apps.second
                    )
                }
            }
        }
    }

    private fun cancelAllJobs() {
        focusModeJob?.cancel()
        pausedAppsJob?.cancel()
        distractingAppsJob?.cancel()
    }

    override fun onCleared() {
        cancelAllJobs()
        super.onCleared()
    }
}
