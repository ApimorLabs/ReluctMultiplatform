package com.apimorlabs.reluct.features.di

import com.apimorlabs.reluct.features.tasks.addEdit.AddEditTaskViewModel
import com.apimorlabs.reluct.features.tasks.completed.CompletedTasksViewModel
import com.apimorlabs.reluct.features.tasks.pending.PendingTasksViewModel
import com.apimorlabs.reluct.features.tasks.search.SearchTasksViewModel
import com.apimorlabs.reluct.features.tasks.statistics.TasksStatisticsViewModel
import com.apimorlabs.reluct.features.tasks.details.TaskDetailsViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

internal fun tasksModule(): Module = module {
    viewModel {
        PendingTasksViewModel(
            getTasksUseCase = get(),
            modifyTasksUsesCase = get()
        )
    }

    viewModel {
        CompletedTasksViewModel(
            getTasksUseCase = get(),
            modifyTaskUseCase = get()
        )
    }

    viewModel { (taskId: String?) ->
        TaskDetailsViewModel(
            getTasksUseCase = get(),
            modifyTaskUseCase = get(),
            manageTaskLabels = get(),
            taskId = taskId
        )
    }

    viewModel { (taskId: String?) ->
        AddEditTaskViewModel(
            modifyTaskUseCase = get(),
            manageTaskLabels = get(),
            taskId = taskId
        )
    }

    viewModel {
        SearchTasksViewModel(
            getTasksUseCase = get(),
            modifyTasksUsesCase = get()
        )
    }

    viewModel {
        TasksStatisticsViewModel(
            modifyTasksUsesCase = get(),
            getGroupedTasksStats = get(),
            getWeekRangeFromOffset = get()
        )
    }
}