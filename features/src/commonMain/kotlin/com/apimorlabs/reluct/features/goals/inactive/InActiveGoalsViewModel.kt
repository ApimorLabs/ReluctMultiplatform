package com.apimorlabs.reluct.features.goals.inactive

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apimorlabs.reluct.common.models.domain.goals.Goal
import com.apimorlabs.reluct.domain.usecases.goals.GetGoals
import com.apimorlabs.reluct.domain.usecases.goals.ModifyGoals
import com.apimorlabs.reluct.features.goals.states.GoalsListState
import com.apimorlabs.reluct.features.goals.states.events.GoalsEvents
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class InActiveGoalsViewModel(
    private val getGoals: GetGoals,
    private val modifyGoals: ModifyGoals
) : ViewModel() {

    private val goalUiState: MutableStateFlow<GoalsListState> =
        MutableStateFlow(GoalsListState.Loading())
    val uiState: StateFlow<GoalsListState> = goalUiState.asStateFlow()

    private val eventsChannel = Channel<GoalsEvents>(capacity = Channel.UNLIMITED)
    val event: Flow<GoalsEvents> = eventsChannel.receiveAsFlow()

    private var limitFactor = 1L
    private var newDataPresent = true

    private var inActiveGoalsJob: Job? = null

    init {
        getInActiveGoals(limitFactor)
    }

    private fun getInActiveGoals(limitFactor: Long) {
        inActiveGoalsJob?.cancel()
        inActiveGoalsJob = viewModelScope.launch {
            getGoals.getInActiveGoals(factor = limitFactor).collectLatest { goals ->
                goalUiState.update {
                    newDataPresent = it.goals != goals
                    GoalsListState.Data(goalsData = goals, newDataPresent = newDataPresent)
                }
            }
        }
    }

    fun fetchMoreData() {
        if (newDataPresent && goalUiState.value !is GoalsListState.Loading) {
            limitFactor++
            inActiveGoalsJob?.cancel()
            goalUiState.update { GoalsListState.Loading(it.goals, newDataPresent) }
            getInActiveGoals(limitFactor)
        }
    }

    fun toggleGoalActiveState(goal: Goal, isActive: Boolean) {
        viewModelScope.launch {
            modifyGoals.toggleGoalActiveState(isActive = isActive, id = goal.id)
            eventsChannel.send(GoalsEvents.ChangedGoalState(isActive = isActive, msg = ""))
        }
    }
}
