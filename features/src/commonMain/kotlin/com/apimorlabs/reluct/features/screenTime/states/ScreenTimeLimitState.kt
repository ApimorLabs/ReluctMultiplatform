package com.apimorlabs.reluct.features.screenTime.states

import com.apimorlabs.reluct.common.models.domain.appInfo.AppInfo
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class ScreenTimeLimitState(
    val focusModeState: FocusModeState = FocusModeState(),
    val pausedAppsState: PausedAppsState = PausedAppsState.Nothing,
    val distractingAppsState: DistractingAppsState = DistractingAppsState.Nothing
)

data class FocusModeState(
    val focusModeOn: Boolean = false,
    val doNotDisturbOn: Boolean = false
)

sealed class PausedAppsState(
    val pausedApps: ImmutableList<AppInfo>,
    val unPausedApps: ImmutableList<AppInfo>
) {
    class Data(pausedApps: ImmutableList<AppInfo>, unPausedApps: ImmutableList<AppInfo>) :
        PausedAppsState(
            pausedApps = pausedApps,
            unPausedApps = unPausedApps
        )

    class Loading(
        pausedApps: ImmutableList<AppInfo> = persistentListOf(),
        unPausedApps: ImmutableList<AppInfo> = persistentListOf()
    ) : PausedAppsState(
        pausedApps = pausedApps,
        unPausedApps = unPausedApps
    )

    data object Nothing : PausedAppsState(persistentListOf(), persistentListOf())
}

sealed class DistractingAppsState(
    val distractingApps: ImmutableList<AppInfo>,
    val otherApps: ImmutableList<AppInfo>
) {
    class Data(distractingApps: ImmutableList<AppInfo>, otherApps: ImmutableList<AppInfo>) :
        DistractingAppsState(distractingApps = distractingApps, otherApps = otherApps)

    class Loading(
        distractingApps: ImmutableList<AppInfo> = persistentListOf(),
        otherApps: ImmutableList<AppInfo> = persistentListOf()
    ) : DistractingAppsState(distractingApps = distractingApps, otherApps = otherApps)

    data object Nothing : DistractingAppsState(persistentListOf(), persistentListOf())
}
