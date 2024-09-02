package com.apimorlabs.reluct.features.goals.states

data class ActiveGoalsState(
    val isSyncing: Boolean = false,
    val goalsListState: GoalsListState = GoalsListState.Loading()
)
