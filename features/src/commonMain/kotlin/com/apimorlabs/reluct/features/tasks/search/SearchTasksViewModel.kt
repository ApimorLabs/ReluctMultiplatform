package com.apimorlabs.reluct.features.tasks.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apimorlabs.reluct.common.models.domain.tasks.Task
import com.apimorlabs.reluct.domain.usecases.tasks.GetTasksUseCase
import com.apimorlabs.reluct.domain.usecases.tasks.ModifyTaskUseCase
import com.apimorlabs.reluct.features.tasks.states.SearchData
import com.apimorlabs.reluct.features.tasks.states.SearchTasksState
import com.apimorlabs.reluct.features.tasks.states.events.TasksEvents
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

class SearchTasksViewModel(
    private val getTasksUseCase: GetTasksUseCase,
    private val modifyTasksUsesCase: ModifyTaskUseCase
) : ViewModel() {

    private val searchData: MutableStateFlow<SearchData> = MutableStateFlow(SearchData.Loading())
    private val searchQuery: MutableStateFlow<String> = MutableStateFlow("")
    private val shouldUpdateData: MutableStateFlow<Boolean> = MutableStateFlow(true)

    val uiState: StateFlow<SearchTasksState> = combine(
        searchData,
        searchQuery,
        shouldUpdateData
    ) { searchData, searchQuery, shouldUpdateData ->
        SearchTasksState(
            searchData = searchData,
            searchQuery = searchQuery,
            shouldUpdateData = shouldUpdateData
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SearchTasksState()
    )

    private val _events: Channel<TasksEvents> = Channel()
    val events: Flow<TasksEvents>
        get() = _events.receiveAsFlow()

    private var limitFactor = 1L

    private lateinit var searchTasksJob: Job

    init {
        getTasks()
    }

    private fun getTasks() {
        searchTasksJob = viewModelScope.launch {
            getTasksUseCase.getSearchedTasks(searchQuery.value, limitFactor)
                .collectLatest { tasks ->
                    if (tasks.isEmpty() || searchQuery.value.isBlank()) {
                        searchData.update { SearchData.Empty }
                    } else {
                        searchData.update {
                            shouldUpdateData.value = it.tasksData != tasks
                            SearchData.Data(tasks)
                        }
                    }
                }
        }
    }

    fun search(query: String) {
        searchTasksJob.cancel() // Cancel the current collection job
        searchData.update { SearchData.Loading(tasks = it.tasksData) }
        searchQuery.update { query }
        getTasks()
    }

    fun fetchMoreData() {
        if (shouldUpdateData.value) {
            limitFactor++
            searchTasksJob.cancel() // Cancel the current collection job
            searchData.update { SearchData.Loading(tasks = it.tasksData) }
            getTasks()
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

    fun goBack() {
        _events.trySend(TasksEvents.Navigation.GoBack)
    }
}
