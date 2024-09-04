
package com.apimorlabs.reluct.features.screenTime.states.events

sealed class ScreenTimeLimitsEvents {
    data object Nothing : ScreenTimeLimitsEvents()
    data class ShowMessageDone(val isDone: Boolean, val msg: String) : ScreenTimeLimitsEvents()
    data class ShowMessage(val msg: String) : ScreenTimeLimitsEvents()
    data class DisplayErrorMsg(
        val msg: String,
    ) : ScreenTimeLimitsEvents()

    sealed class Navigation : ScreenTimeLimitsEvents() {
        data class NavigateToAppInfo(
            val packageName: String
        ) : Navigation()

        data class OpenAppTimerSettings(
            val packageName: String
        ) : Navigation()

        data object GoBack : Navigation()
    }
}
