package com.apimorlabs.reluct.features.tasks.addEdit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apimorlabs.reluct.common.models.domain.tasks.EditTask
import com.apimorlabs.reluct.common.models.domain.tasks.TaskLabel
import com.apimorlabs.reluct.domain.usecases.tasks.ManageTaskLabels
import com.apimorlabs.reluct.domain.usecases.tasks.ModifyTaskUseCase
import com.apimorlabs.reluct.features.tasks.states.AddEditTaskState
import com.apimorlabs.reluct.features.tasks.states.ModifyTaskState
import com.apimorlabs.reluct.features.tasks.states.events.TasksEvents
import com.apimorlabs.reluct.features.util.Constants
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.onSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddEditTaskViewModel(
    private val modifyTaskUseCase: ModifyTaskUseCase,
    private val manageTaskLabels: ManageTaskLabels,
    private val taskId: String?
) : ViewModel() {

    private val modifyTaskState: MutableStateFlow<ModifyTaskState> =
        MutableStateFlow(ModifyTaskState.Loading)
    private val availableTaskLabels: MutableStateFlow<ImmutableList<TaskLabel>> =
        MutableStateFlow(persistentListOf())

    val uiState: StateFlow<AddEditTaskState> = combine(
        modifyTaskState,
        availableTaskLabels
    ) { modifyTaskState, availableTaskLabels ->
        AddEditTaskState(
            modifyTaskState = modifyTaskState,
            availableTaskLabels = availableTaskLabels
        )
    }.stateIn(
        scope = viewModelScope,
        initialValue = AddEditTaskState(),
        started = SharingStarted.WhileSubscribed(5_000L)
    )

    private val eventsChannel: Channel<TasksEvents> = Channel()
    val events: Flow<TasksEvents>
        get() = eventsChannel.receiveAsFlow()

    init {
        getTask(taskId)
        getTaskLabels()
    }

    fun saveCurrentTask() {
        viewModelScope.launch {
            val taskState = modifyTaskState.value
            if (taskState is ModifyTaskState.Data) {
                modifyTaskUseCase.saveTask(taskState.task)
                val result = eventsChannel.trySend(TasksEvents.ShowMessage(Constants.TASK_SAVED))
                result.onSuccess {
                    /**
                     * Go back after saving if you are just editing a Task and the [taskId]
                     * is not null else just show the TaskSaved State for adding more tasks
                     */
                    /*if (taskId != null) {
                        eventsChannel.send(TasksEvents.Navigation.GoBack)
                    } else {
                        modifyTaskState.update { ModifyTaskState.Saved }
                    }*/
                    // Just exit the page
                    eventsChannel.send(TasksEvents.Navigation.GoBack)
                }
            }
        }
    }

    fun newTask() {
        modifyTaskState.update {
            ModifyTaskState.Data(
                isEdit = false,
                task = DefaultTasks.emptyEditTask()
            )
        }
    }

    fun updateCurrentTask(task: EditTask) {
        modifyTaskState.update { ModifyTaskState.Data(isEdit = taskId != null, task = task) }
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

    private fun getTaskLabels() {
        viewModelScope.launch {
            manageTaskLabels.allLabels().collectLatest { labels ->
                availableTaskLabels.update { labels }
            }
        }
    }

    private fun getTask(taskId: String?) {
        viewModelScope.launch {
            when (taskId) {
                null -> modifyTaskState.update {
                    ModifyTaskState.Data(isEdit = false, task = DefaultTasks.emptyEditTask())
                }

                else -> {
                    when (val task = modifyTaskUseCase.getTaskToEdit(taskId).firstOrNull()) {
                        null -> modifyTaskState.update { ModifyTaskState.NotFound }
                        else -> modifyTaskState.update {
                            ModifyTaskState.Data(
                                isEdit = true,
                                task = task
                            )
                        }
                    }
                }
            }
        }
    }
}
