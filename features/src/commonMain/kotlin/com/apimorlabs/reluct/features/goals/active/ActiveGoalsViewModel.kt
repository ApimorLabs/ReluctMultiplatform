package com.apimorlabs.reluct.features.goals.active

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apimorlabs.reluct.common.models.domain.goals.Goal
import com.apimorlabs.reluct.domain.usecases.goals.GetGoals
import com.apimorlabs.reluct.domain.usecases.goals.ModifyGoals
import com.apimorlabs.reluct.features.goals.states.ActiveGoalsState
import com.apimorlabs.reluct.features.goals.states.GoalsListState
import com.apimorlabs.reluct.features.goals.states.events.GoalsEvents
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ActiveGoalsViewModel(
    private val getGoals: GetGoals,
    private val modifyGoals: ModifyGoals
) : ViewModel() {

    private val goalsListState: MutableStateFlow<GoalsListState> =
        MutableStateFlow(GoalsListState.Loading())
    private val isSyncingData = MutableStateFlow(false)
    val uiState: StateFlow<ActiveGoalsState> = combine(
        goalsListState,
        isSyncingData
    ) { goalsListState, isSyncingData ->
        ActiveGoalsState(
            isSyncing = isSyncingData,
            goalsListState = goalsListState
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = ActiveGoalsState()
    )

    private val eventsChannel = Channel<GoalsEvents>(capacity = Channel.UNLIMITED)
    val event: Flow<GoalsEvents> = eventsChannel.receiveAsFlow()

    private var limitFactor = 1L
    private var newDataPresent = true

    private var activeGoalsJob: Job? = null
    private var syncDataJob: Job? = null

    init {
        getActiveGoals(limitFactor)
        syncData()
    }

    private fun getActiveGoals(limitFactor: Long) {
        activeGoalsJob?.cancel()
        activeGoalsJob = viewModelScope.launch {
            getGoals.getActiveGoals(factor = limitFactor).collectLatest { goals ->
                goalsListState.update { state ->
                    newDataPresent = (state.goals.firstOrNull()?.id != goals.firstOrNull()?.id) &&
                            (state.goals.lastOrNull()?.id != goals.lastOrNull()?.id)
                    GoalsListState.Data(goalsData = goals, newDataPresent = newDataPresent)
                }
            }
        }
    }

    fun syncData() {
        syncDataJob?.cancel()
        syncDataJob = viewModelScope.launch {
            isSyncingData.update { true }
            modifyGoals.syncGoals()
            isSyncingData.update { false }
        }
    }

    fun fetchMoreData() {
        if (newDataPresent && goalsListState.value !is GoalsListState.Loading) {
            limitFactor++
            activeGoalsJob?.cancel()
            goalsListState.update { GoalsListState.Loading(it.goals, newDataPresent) }
            getActiveGoals(limitFactor)
        }
    }

    fun toggleGoalActiveState(goal: Goal, isActive: Boolean) {
        viewModelScope.launch {
            modifyGoals.toggleGoalActiveState(isActive = isActive, id = goal.id)
            eventsChannel.send(GoalsEvents.ChangedGoalState(isActive = isActive, msg = ""))
        }
    }
}