package com.apimorlabs.reluct.features.screenTime.states.events

import com.apimorlabs.reluct.common.models.domain.limits.AppTimeLimit

sealed class ScreenTimeStatsEvents {
    data object Nothing : ScreenTimeStatsEvents()
    data class ShowMessageDone(val isDone: Boolean, val msg: String) : ScreenTimeStatsEvents()
    class TimeLimitChange(val app: AppTimeLimit) : ScreenTimeStatsEvents()

    sealed class Navigation : ScreenTimeStatsEvents() {
        data class NavigateToAppInfo(
            val packageName: String
        ) : Navigation()

        data class OpenAppTimerSettings(
            val packageName: String
        ) : Navigation()

        data object GoBack : Navigation()
    }
}