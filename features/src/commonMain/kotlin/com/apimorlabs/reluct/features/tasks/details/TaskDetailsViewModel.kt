package com.apimorlabs.reluct.features.tasks.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apimorlabs.reluct.common.models.domain.tasks.Task
import com.apimorlabs.reluct.common.models.domain.tasks.TaskLabel
import com.apimorlabs.reluct.domain.usecases.tasks.GetTasksUseCase
import com.apimorlabs.reluct.domain.usecases.tasks.ManageTaskLabels
import com.apimorlabs.reluct.domain.usecases.tasks.ModifyTaskUseCase
import com.apimorlabs.reluct.features.tasks.states.TaskDetailsState
import com.apimorlabs.reluct.features.tasks.states.TaskState
import com.apimorlabs.reluct.features.tasks.states.events.TasksEvents
import com.apimorlabs.reluct.features.util.Constants
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.onSuccess
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

class TaskDetailsViewModel(
    private val getTasksUseCase: GetTasksUseCase,
    private val modifyTaskUseCase: ModifyTaskUseCase,
    private val manageTaskLabels: ManageTaskLabels,
    private val taskId: String?,
) : ViewModel() {

    private val taskState: MutableStateFlow<TaskState> = MutableStateFlow(TaskState.Loading)
    private val availableTaskLabels: MutableStateFlow<ImmutableList<TaskLabel>> =
        MutableStateFlow(persistentListOf())

    private val eventsChannel: Channel<TasksEvents> = Channel()

    val uiState: StateFlow<TaskDetailsState> = combine(
        taskState,
        availableTaskLabels
    ) { taskState, availableTaskLabels ->
        TaskDetailsState(
            taskState = taskState,
            availableTaskLabels = availableTaskLabels
        )
    }.stateIn(
        scope = viewModelScope,
        initialValue = TaskDetailsState(),
        started = SharingStarted.WhileSubscribed(5_000)
    )

    val events: Flow<TasksEvents> = eventsChannel.receiveAsFlow()

    private var getTaskJob: Job? = null

    init {
        getTask()
        getTaskLabels()
    }

    private fun getTaskLabels() {
        viewModelScope.launch {
            manageTaskLabels.allLabels().collectLatest { labels ->
                availableTaskLabels.update { labels }
            }
        }
    }

    private fun getTask() {
        getTaskJob?.cancel()
        getTaskJob = viewModelScope.launch {
            when (taskId) {
                null -> {
                    taskState.update { TaskState.NotFound }
                }

                else -> getTasksUseCase.getTask(taskId).collectLatest { task ->
                    when (task) {
                        null -> taskState.update { TaskState.NotFound }
                        else -> taskState.update { TaskState.Data(task) }
                    }
                }
            }
        }
    }

    fun toggleDone(task: Task, isDone: Boolean) {
        viewModelScope.launch {
            modifyTaskUseCase.toggleTaskDone(task, isDone)
            eventsChannel.send(TasksEvents.ShowMessageDone(isDone, task.title))
        }
    }

    fun editTask(taskId: String) {
        eventsChannel.trySend(TasksEvents.Navigation.NavigateToEdit(taskId))
    }

    fun deleteTask(taskId: String) {
        viewModelScope.launch {
            modifyTaskUseCase.deleteTask(taskId)
            getTaskJob?.cancel()
            taskState.update { TaskState.Deleted }
            val result = eventsChannel.trySend(
                TasksEvents.ShowMessage(Constants.DELETED_SUCCESSFULLY)
            )
            result.onSuccess { eventsChannel.send(TasksEvents.Navigation.GoBack) }
        }
    }

    fun saveLabel(label: TaskLabel) {
        viewModelScope.launch {
            manageTaskLabels.addLabel(label)
        }
    }

    fun deleteLabel(label: TaskLabel) {
        viewModelScope.launch {
            manageTaskLabels.deleteLabel(label.id)
        }
    }

    fun goBack() {
        eventsChannel.trySend(TasksEvents.Navigation.GoBack)
    }
}
