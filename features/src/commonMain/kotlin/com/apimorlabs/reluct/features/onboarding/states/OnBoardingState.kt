package com.apimorlabs.reluct.features.onboarding.states

import com.apimorlabs.reluct.data.source.settings.Keys

data class OnBoardingState(
    val currentPage: OnBoardingPages = OnBoardingPages.Welcome,
    val permissionsState: PermissionsState = PermissionsState(),
    val currentThemeValue: Int = Keys.Defaults.THEME,
    val appBlockingEnabled: Boolean = false
)

sealed class OnBoardingPages {
    data object Welcome : OnBoardingPages()
    data object Permissions : OnBoardingPages()
    data object Notifications : OnBoardingPages()
    data object Reminders : OnBoardingPages()
    data object UsageAccess : OnBoardingPages()
    data object Overlay : OnBoardingPages()
    data object Themes : OnBoardingPages()
    data object AllSet : OnBoardingPages()
}

data class PermissionsState(
    val notificationGranted: Boolean = false,
    val alarmsAndRemindersGranted: Boolean = false,
    val usageAccessGranted: Boolean = false,
    val overlayGranted: Boolean = false
)
