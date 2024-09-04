package com.apimorlabs.reluct.features.tasks.completed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apimorlabs.reluct.common.models.domain.tasks.Task
import com.apimorlabs.reluct.domain.usecases.tasks.GetTasksUseCase
import com.apimorlabs.reluct.domain.usecases.tasks.ModifyTaskUseCase
import com.apimorlabs.reluct.features.tasks.states.CompletedTasksState
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

class CompletedTasksViewModel(
    private val getTasksUseCase: GetTasksUseCase,
    private val modifyTaskUseCase: ModifyTaskUseCase
) : ViewModel() {

    private val _uiState: MutableStateFlow<CompletedTasksState> =
        MutableStateFlow(CompletedTasksState.Loading())
    private val _events: Channel<TasksEvents> = Channel()

    val uiState: StateFlow<CompletedTasksState>
        get() = _uiState
    val events: Flow<TasksEvents>
        get() = _events.receiveAsFlow()

    private var limitFactor = 1L
    private var newDataPresent = true

    private lateinit var completedTasksJob: Job

    init {
        getCompletedTasks(limitFactor = limitFactor)
    }

    private fun getCompletedTasks(limitFactor: Long) {
        completedTasksJob = viewModelScope.launch {
            getTasksUseCase.getCompletedTasks(factor = limitFactor).collectLatest { taskList ->
                val grouped = taskList.groupBy { it.dueDate }
                    .mapValues { it.value.toImmutableList() }
                    .toImmutableMap()
                _uiState.update {
                    newDataPresent = it.tasksData != grouped
                    CompletedTasksState.Data(
                        tasks = grouped,
                        newDataPresent = newDataPresent
                    )
                }
            }
        }
    }

    fun fetchMoreData() {
        if (newDataPresent) {
            limitFactor++
            completedTasksJob.cancel()
            _uiState.update {
                CompletedTasksState.Loading(
                    tasks = it.tasksData,
                    newDataPresent = newDataPresent
                )
            }
            getCompletedTasks(limitFactor)
        }
    }

    fun toggleDone(task: Task, isDone: Boolean) {
        viewModelScope.launch {
            modifyTaskUseCase.toggleTaskDone(task, isDone)
            _events.send(TasksEvents.ShowMessageDone(isDone, task.title))
        }
    }

    fun navigateToTaskDetails(taskId: String) {
        _events.trySend(TasksEvents.Navigation.NavigateToTaskDetails(taskId))
    }
}
