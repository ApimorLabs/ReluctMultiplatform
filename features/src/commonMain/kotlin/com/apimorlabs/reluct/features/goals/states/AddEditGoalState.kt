package com.apimorlabs.reluct.features.goals.states

import com.apimorlabs.reluct.common.models.domain.appInfo.AppInfo
import com.apimorlabs.reluct.common.models.domain.goals.Goal
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class AddEditGoalState(
    val modifyGoalState: ModifyGoalState = ModifyGoalState.Loading,
    val appsState: GoalAppsState = GoalAppsState.Nothing
)

sealed class ModifyGoalState {
    data class Data(
        val goal: Goal,
        val isEdit: Boolean
    ) : ModifyGoalState()

    data object Saved : ModifyGoalState()

    data object Loading : ModifyGoalState()

    data object NotFound : ModifyGoalState()
}

sealed class GoalAppsState(
    val selectedApps: ImmutableList<AppInfo>,
    val unselectedApps: ImmutableList<AppInfo>
) {
    data object Nothing : GoalAppsState(persistentListOf(), persistentListOf())

    data class Data(
        private val selected: ImmutableList<AppInfo>,
        private val unselected: ImmutableList<AppInfo>
    ) : GoalAppsState(selectedApps = selected, unselectedApps = unselected)

    data object Loading : GoalAppsState(persistentListOf(), persistentListOf())
}
