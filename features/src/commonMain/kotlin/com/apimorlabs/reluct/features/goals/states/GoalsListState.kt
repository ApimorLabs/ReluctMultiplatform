package com.apimorlabs.reluct.features.goals.states

import com.apimorlabs.reluct.common.models.domain.goals.Goal
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

sealed class GoalsListState(
    val goals: ImmutableList<Goal>,
    val shouldUpdateData: Boolean
) {
    data class Data(
        private val goalsData: ImmutableList<Goal> = persistentListOf(),
        private val newDataPresent: Boolean = true
    ) : GoalsListState(goals = goalsData, shouldUpdateData = newDataPresent)

    data class Loading(
        private val goalsData: ImmutableList<Goal> = persistentListOf(),
        private val newDataPresent: Boolean = true
    ) : GoalsListState(goals = goalsData, shouldUpdateData = newDataPresent)
}
