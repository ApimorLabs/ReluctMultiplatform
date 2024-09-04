package com.apimorlabs.reluct.features.di

import com.apimorlabs.reluct.features.dashboard.DashboardOverviewViewModel
import com.apimorlabs.reluct.features.dashboard.DashboardStatisticsViewModel
import com.apimorlabs.reluct.features.onboarding.OnBoardingViewModel
import com.apimorlabs.reluct.features.settings.AppSettingsViewModel
import org.koin.core.KoinApplication
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

object Features {
    fun KoinApplication.install() = apply {
        modules(sharedModule(), tasksModule(), goalsModule())
    }

    private fun sharedModule(): Module = module {
        // On Boarding
        viewModelOf(::OnBoardingViewModel)

        // Dashboard
        viewModel {
            DashboardOverviewViewModel(
                getTasksUseCase = get(),
                modifyTasksUsesCase = get(),
                getUsageStats = get(),
                getGoals = get(),
                modifyGoals = get(),
                screenTimeServices = get()
            )
        }

        viewModel {
            DashboardStatisticsViewModel(
                screenTimeStatsViewModel = get(),
                tasksStatsViewModel = get()
            )
        }

        // Settings
        viewModel {
            AppSettingsViewModel(
                settings = get(),
                screenTimeServices = get()
            )
        }
    }
}
