package com.apimorlabs.reluct.features.tasks.pending

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apimorlabs.reluct.common.models.domain.tasks.Task
import com.apimorlabs.reluct.common.models.util.filterPersistent
import com.apimorlabs.reluct.domain.usecases.tasks.GetTasksUseCase
import com.apimorlabs.reluct.domain.usecases.tasks.ModifyTaskUseCase
import com.apimorlabs.reluct.features.tasks.states.PendingTasksState
import com.apimorlabs.reluct.features.tasks.states.events.TasksEvents
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PendingTasksViewModel(
    private val getTasksUseCase: GetTasksUseCase,
    private val modifyTasksUsesCase: ModifyTaskUseCase
) : ViewModel() {

    private val _uiState: MutableStateFlow<PendingTasksState> =
        MutableStateFlow(PendingTasksState.Loading())
    private val _events: Channel<TasksEvents> = Channel()

    val uiState: StateFlow<PendingTasksState>
        get() = _uiState

    val events: Flow<TasksEvents>
        get() = _events.receiveAsFlow()

    private var limitFactor = 1L
    private var newDataPresent = true

    private lateinit var pendingTasksJob: Job

    init {
        getPendingTasks(limitFactor)
    }

    private fun getPendingTasks(limitFactor: Long) {
        pendingTasksJob = viewModelScope.launch {
            getTasksUseCase.getPendingTasks(factor = limitFactor).collectLatest { taskList ->
                val overdueList = taskList.filterPersistent { it.overdue }
                val grouped = taskList
                    .filterNot { it.overdue }
                    .groupBy { it.dueDate }
                    .mapValues { it.value.toImmutableList() }
                    .toImmutableMap()
                _uiState.update {
                    newDataPresent = it.tasksData != grouped
                    PendingTasksState.Data(
                        tasks = grouped,
                        overdueTasks = overdueList,
                        newDataPresent = newDataPresent
                    )
                }
            }
        }
    }

    fun fetchMoreData() {
        if (newDataPresent) {
            limitFactor++
            pendingTasksJob.cancel()
            _uiState.update {
                PendingTasksState.Loading(
                    tasks = it.tasksData,
                    overdueTasks = it.overdueTasksData,
                    newDataPresent = it.shouldUpdateData
                )
            }
            getPendingTasks(limitFactor)
        }
    }

    fun toggleDone(task: Task, isDone: Boolean) {
        viewModelScope.launch {
            modifyTasksUsesCase.toggleTaskDone(task, isDone)
            _events.send(TasksEvents.ShowMessageDone(isDone, task.title))
        }
    }

    fun navigateToTaskDetails(taskId: String) {
        _events.trySend(TasksEvents.Navigation.NavigateToTaskDetails(taskId))
    }
}
