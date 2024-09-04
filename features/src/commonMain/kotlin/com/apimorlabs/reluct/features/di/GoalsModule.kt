package com.apimorlabs.reluct.features.di

import com.apimorlabs.reluct.features.goals.active.ActiveGoalsViewModel
import com.apimorlabs.reluct.features.goals.addEdit.AddEditGoalViewModel
import com.apimorlabs.reluct.features.goals.details.GoalDetailsViewModel
import com.apimorlabs.reluct.features.goals.inactive.InActiveGoalsViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

internal fun goalsModule(): Module = module {
    viewModel {
        ActiveGoalsViewModel(
            getGoals = get(),
            modifyGoals = get()
        )
    }

    viewModel {
        InActiveGoalsViewModel(
            getGoals = get(),
            modifyGoals = get()
        )
    }

    viewModel { (goalId: String?) ->
        GoalDetailsViewModel(
            getGoals = get(),
            modifyGoals = get(),
            goalId = goalId
        )
    }

    viewModel { (goalId: String?, defaultGoalIndex: Int?) ->
        AddEditGoalViewModel(
            getGoals = get(),
            modifyGoals = get(),
            getInstalledApps = get(),
            goalId = goalId,
            defaultGoalIndex = defaultGoalIndex
        )
    }
}
