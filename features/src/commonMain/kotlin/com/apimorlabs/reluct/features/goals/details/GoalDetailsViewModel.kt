package com.apimorlabs.reluct.features.goals.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apimorlabs.reluct.common.models.domain.goals.Goal
import com.apimorlabs.reluct.domain.usecases.goals.GetGoals
import com.apimorlabs.reluct.domain.usecases.goals.ModifyGoals
import com.apimorlabs.reluct.features.goals.states.GoalDetailsState
import com.apimorlabs.reluct.features.goals.states.events.GoalsEvents
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.onSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GoalDetailsViewModel(
    private val getGoals: GetGoals,
    private val modifyGoals: ModifyGoals,
    private val goalId: String?
) : ViewModel() {

    private val state: MutableStateFlow<GoalDetailsState> =
        MutableStateFlow(GoalDetailsState.Loading)
    val uiState: StateFlow<GoalDetailsState> = state.asStateFlow()

    private val eventsChannel = Channel<GoalsEvents>(Channel.UNLIMITED)
    val events: Flow<GoalsEvents> = eventsChannel.receiveAsFlow()

    private var syncDataJob: Job? = null

    init {
        getGoal()
    }

    fun toggleGoalActiveState(goalId: String, isActive: Boolean) {
        viewModelScope.launch {
            modifyGoals.toggleGoalActiveState(isActive, goalId)
            eventsChannel.send(GoalsEvents.ChangedGoalState(isActive, ""))
        }
    }

    fun updateCurrentValue(goalId: String, value: Long) {
        viewModelScope.launch {
            modifyGoals.updateGoalCurrentValue(id = goalId, currentValue = value)
        }
    }

    fun syncData() {
        syncDataJob?.cancel()
        syncDataJob = viewModelScope.launch {
            val currentState = state.value
            if (currentState is GoalDetailsState.Data) {
                state.update { GoalDetailsState.Data(currentState.goal, true) }
                modifyGoals.syncGoals()
                state.update { GoalDetailsState.Data(currentState.goal, false) }
            }
        }
    }

    fun deleteGoal(goal: Goal) {
        viewModelScope.launch {
            modifyGoals.deleteGoal(goal.id)
            val result = eventsChannel.trySend(GoalsEvents.DeletedGoal(goal.id, goal.name))
            result.onSuccess { eventsChannel.send(GoalsEvents.Exit) }
        }
    }

    private fun getGoal() {
        viewModelScope.launch {
            when (goalId) {
                null -> state.update { GoalDetailsState.NotFound }
                else -> getGoals.getGoal(goalId).collectLatest { goal ->
                    when (goal) {
                        null -> state.update { GoalDetailsState.NotFound }
                        else -> state.update { GoalDetailsState.Data(goal, it.isSyncing) }
                    }
                }
            }
        }
    }
}
