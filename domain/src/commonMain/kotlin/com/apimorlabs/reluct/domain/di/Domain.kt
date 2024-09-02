package com.apimorlabs.reluct.domain.di

import com.apimorlabs.reluct.domain.usecases.appUsage.GetAppUsageInfo
import com.apimorlabs.reluct.domain.usecases.appUsage.GetUsageStats
import com.apimorlabs.reluct.domain.usecases.appUsage.impl.GetAppUsageInfoImpl
import com.apimorlabs.reluct.domain.usecases.appUsage.impl.GetUsageStatsImpl
import com.apimorlabs.reluct.domain.usecases.goals.GetGoals
import com.apimorlabs.reluct.domain.usecases.goals.ModifyGoals
import com.apimorlabs.reluct.domain.usecases.goals.impl.GetGoalsImpl
import com.apimorlabs.reluct.domain.usecases.goals.impl.ModifyGoalsImpl
import com.apimorlabs.reluct.domain.usecases.limits.GetAppLimits
import com.apimorlabs.reluct.domain.usecases.limits.GetDistractingApps
import com.apimorlabs.reluct.domain.usecases.limits.GetPausedApps
import com.apimorlabs.reluct.domain.usecases.limits.ManageAppTimeLimit
import com.apimorlabs.reluct.domain.usecases.limits.ManageDistractingApps
import com.apimorlabs.reluct.domain.usecases.limits.ManageFocusMode
import com.apimorlabs.reluct.domain.usecases.limits.ManagePausedApps
import com.apimorlabs.reluct.domain.usecases.limits.ModifyAppLimits
import com.apimorlabs.reluct.domain.usecases.limits.impl.GetAppLimitsImpl
import com.apimorlabs.reluct.domain.usecases.limits.impl.GetDistractingAppsImpl
import com.apimorlabs.reluct.domain.usecases.limits.impl.GetPausedAppsImpl
import com.apimorlabs.reluct.domain.usecases.limits.impl.ManageAppTimeLimitImpl
import com.apimorlabs.reluct.domain.usecases.limits.impl.ManageDistractingAppsImpl
import com.apimorlabs.reluct.domain.usecases.limits.impl.ManageFocusModeImpl
import com.apimorlabs.reluct.domain.usecases.limits.impl.ManagePausedAppsImpl
import com.apimorlabs.reluct.domain.usecases.limits.impl.ModifyAppLimitsImpl
import com.apimorlabs.reluct.domain.usecases.tasks.GetGroupedTasksStats
import com.apimorlabs.reluct.domain.usecases.tasks.GetTasksUseCase
import com.apimorlabs.reluct.domain.usecases.tasks.ManageTaskLabels
import com.apimorlabs.reluct.domain.usecases.tasks.ModifyTaskUseCase
import com.apimorlabs.reluct.domain.usecases.tasks.impl.GetGroupedTasksStatsImpl
import com.apimorlabs.reluct.domain.usecases.tasks.impl.GetTasksUseCaseImpl
import com.apimorlabs.reluct.domain.usecases.tasks.impl.ManageTaskLabelsImpl
import com.apimorlabs.reluct.domain.usecases.tasks.impl.ModifyTaskUseCaseImpl
import com.apimorlabs.reluct.domain.usecases.time.GetWeekRangeFromOffset
import com.apimorlabs.reluct.domain.usecases.time.impl.GetWeekRangeFromOffsetImpl
import kotlinx.coroutines.Dispatchers
import org.koin.core.KoinApplication
import org.koin.core.module.Module
import org.koin.dsl.module

object Domain {
    fun KoinApplication.install() = apply {
        modules(sharedModule(), platformModule())
    }

    private fun sharedModule(): Module = module {
        /** UseCases **/
        // Tasks
        factory<GetTasksUseCase> {
            GetTasksUseCaseImpl(
                dao = get(),
                backgroundDispatcher = Dispatchers.IO
            )
        }

        factory<ModifyTaskUseCase> {
            ModifyTaskUseCaseImpl(
                dao = get(),
                haptics = get(),
                manageTasksAlarms = get(),
                backgroundDispatcher = Dispatchers.IO
            )
        }

        factory<GetGroupedTasksStats> {
            GetGroupedTasksStatsImpl(
                dao = get(),
                backgroundDispatcher = Dispatchers.IO
            )
        }

        factory<ManageTaskLabels> {
            ManageTaskLabelsImpl(
                dao = get(),
                haptics = get(),
                dispatcher = Dispatchers.IO
            )
        }

        // Time
        factory<GetWeekRangeFromOffset> {
            GetWeekRangeFromOffsetImpl(dispatcher = Dispatchers.IO)
        }

        // App Usage Stats
        factory<GetAppUsageInfo> {
            GetAppUsageInfoImpl(
                usageManager = get(),
                backgroundDispatcher = Dispatchers.IO,
                getAppInfo = get()
            )
        }

        factory<GetUsageStats> {
            GetUsageStatsImpl(
                usageManager = get(),
                getAppInfo = get(),
                backgroundDispatcher = Dispatchers.IO
            )
        }

        // Limits
        factory<GetAppLimits> {
            GetAppLimitsImpl(
                limitsDao = get(),
                getAppInfo = get(),
                dispatcher = Dispatchers.IO
            )
        }
        factory<GetPausedApps> {
            GetPausedAppsImpl(
                limitsDao = get(),
                getAppInfo = get(),
                backgroundDispatcher = Dispatchers.IO
            )
        }

        factory<ModifyAppLimits> {
            ModifyAppLimitsImpl(
                limitsDao = get(),
                dispatcher = Dispatchers.IO
            )
        }

        factory<GetDistractingApps> {
            GetDistractingAppsImpl(
                limitsDao = get(),
                getAppInfo = get(),
                backgroundDispatcher = Dispatchers.IO
            )
        }

        factory<ManageDistractingApps> {
            ManageDistractingAppsImpl(
                getDistractingApps = get(),
                getInstalledApps = get(),
                modifyAppLimits = get(),
                haptics = get(),
                backgroundDispatcher = Dispatchers.IO
            )
        }

        factory<ManagePausedApps> {
            ManagePausedAppsImpl(
                getPausedApps = get(),
                getInstalledApps = get(),
                modifyAppLimits = get(),
                haptics = get(),
                backgroundDispatcher = Dispatchers.IO
            )
        }

        factory<ManageFocusMode> {
            ManageFocusModeImpl(
                settings = get(),
                haptics = get(),
                backgroundDispatcher = Dispatchers.IO
            )
        }

        factory<ManageAppTimeLimit> {
            ManageAppTimeLimitImpl(
                limitsDao = get(),
                getAppInfo = get(),
                haptics = get(),
                dispatcher = Dispatchers.IO
            )
        }

        /** Goals **/

        factory<GetGoals> {
            GetGoalsImpl(
                goalsDao = get(),
                getAppInfo = get(),
                backgroundDispatcher = Dispatchers.IO
            )
        }

        factory<ModifyGoals> {
            ModifyGoalsImpl(
                goalsDao = get(),
                haptics = get(),
                usageDataManager = get(),
                getGroupedTasksStats = get(),
                dispatcher = Dispatchers.IO
            )
        }
    }
}

internal expect fun platformModule(): Module
