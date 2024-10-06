package com.apimorlabs.reluct.features.di

import com.apimorlabs.reluct.features.dashboard.DashboardOverviewViewModel
import com.apimorlabs.reluct.features.dashboard.DashboardStatisticsViewModel
import com.apimorlabs.reluct.features.onboarding.OnBoardingViewModel
import com.apimorlabs.reluct.features.screenTime.limits.ScreenTimeLimitsViewModel
import com.apimorlabs.reluct.features.screenTime.statistics.AppScreenTimeStatsViewModel
import com.apimorlabs.reluct.features.screenTime.statistics.ScreenTimeStatsViewModel
import com.apimorlabs.reluct.features.settings.AppSettingsViewModel
import com.apimorlabs.reluct.features.settings.GetSettings
import org.koin.core.KoinApplication
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

object Features {
    fun KoinApplication.install() = apply {
        modules(platformModule(), sharedModule(), tasksModule(), goalsModule())
    }

    private fun sharedModule(): Module = module {
        // On Boarding
        viewModelOf(::OnBoardingViewModel)

        // Dashboard
        viewModelOf(::DashboardOverviewViewModel)
        viewModelOf(::DashboardStatisticsViewModel)

        // Settings
        viewModelOf(::AppSettingsViewModel)
        singleOf(::GetSettings)

        // Screen Time
        viewModelOf(::ScreenTimeLimitsViewModel)

        viewModelOf(::ScreenTimeStatsViewModel)

        viewModel { (packageName: String) ->
            AppScreenTimeStatsViewModel(
                packageName = packageName,
                getAppUsageInfo = get(),
                manageAppTimeLimit = get(),
                manageDistractingApps = get(),
                managePausedApps = get(),
                getWeekRangeFromOffset = get()
            )
        }
    }
}

internal expect fun platformModule(): Module
