package com.apimorlabs.reluct.features.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apimorlabs.reluct.data.source.settings.MultiplatformSettings
import com.apimorlabs.reluct.features.onboarding.states.OnBoardingPages
import com.apimorlabs.reluct.features.onboarding.states.OnBoardingState
import com.apimorlabs.reluct.features.onboarding.states.PermissionType
import com.apimorlabs.reluct.features.onboarding.states.PermissionsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OnBoardingViewModel(
    private val settings: MultiplatformSettings
) : ViewModel() {

    private val currentPage: MutableStateFlow<OnBoardingPages> =
        MutableStateFlow(OnBoardingPages.Welcome)
    private val permissionsState = MutableStateFlow(PermissionsState())

    val uiState: StateFlow<OnBoardingState> = combine(
        currentPage,
        permissionsState,
        settings.theme,
        settings.appBlockingEnabled
    ) { currentPage, permissionsState, currentThemeValue, appBlockingEnabled ->
        OnBoardingState(
            currentPage = currentPage,
            permissionsState = permissionsState,
            currentThemeValue = currentThemeValue,
            appBlockingEnabled = appBlockingEnabled
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = OnBoardingState()
    )

    fun updateCurrentPage(page: OnBoardingPages) {
        currentPage.update { page }
    }

    fun updatePermission(permissionType: PermissionType, isGranted: Boolean) {
        when (permissionType) {
            PermissionType.NOTIFICATION -> {
                permissionsState.update { it.copy(notificationGranted = isGranted) }
            }

            PermissionType.USAGE_ACCESS -> {
                permissionsState.update { it.copy(usageAccessGranted = isGranted) }
            }

            PermissionType.REMINDERS -> {
                permissionsState.update { it.copy(alarmsAndRemindersGranted = isGranted) }
            }

            PermissionType.OVERLAY -> {
                permissionsState.update { it.copy(overlayGranted = isGranted) }
            }
        }
    }

    fun toggleAppBlocking(isEnabled: Boolean) {
        viewModelScope.launch {
            settings.saveAppBlocking(isEnabled)
        }
    }

    fun saveTheme(themeValue: Int) {
        settings.saveThemeSettings(themeValue)
    }

    fun onBoardingComplete() {
        settings.saveOnBoardingShown(true)
    }
}
