package com.apimorlabs.reluct.features.goals.states

import com.apimorlabs.reluct.common.models.domain.goals.Goal

sealed class GoalDetailsState(val isSyncing: Boolean) {
    data class Data(
        val goal: Goal,
        private val syncingData: Boolean
    ) : GoalDetailsState(syncingData)

    data object NotFound : GoalDetailsState(false)
    data object Loading : GoalDetailsState(false)
}
