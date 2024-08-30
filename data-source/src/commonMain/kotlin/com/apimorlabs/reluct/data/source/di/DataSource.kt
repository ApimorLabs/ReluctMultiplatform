package com.apimorlabs.reluct.data.source.di

import com.apimorlabs.reluct.data.source.database.dao.goals.GoalsDao
import com.apimorlabs.reluct.data.source.database.dao.goals.GoalsDaoImpl
import com.apimorlabs.reluct.data.source.database.dao.screentime.LimitsDao
import com.apimorlabs.reluct.data.source.database.dao.screentime.LimitsDaoImpl
import com.apimorlabs.reluct.data.source.database.dao.tasks.TasksDao
import com.apimorlabs.reluct.data.source.database.dao.tasks.TasksDaoImpl
import com.apimorlabs.reluct.data.source.settings.MultiplatformSettings
import com.apimorlabs.reluct.data.source.settings.MultiplatformSettingsImpl
import com.russhwolf.settings.Settings
import org.koin.core.KoinApplication
import org.koin.core.module.Module
import org.koin.dsl.module

object DataSource {
    fun KoinApplication.install() = apply {
        modules(sharedModule(), platformModule())
    }

    private fun sharedModule(): Module = module {
        /** Database **/
        single<TasksDao> {
            TasksDaoImpl(
                dispatcher = CoroutineDispatchers.background,
                databaseWrapper = get()
            )
        }
        single<GoalsDao> {
            GoalsDaoImpl(
                dispatcher = CoroutineDispatchers.background,
                databaseWrapper = get()
            )
        }

        single<LimitsDao> {
            LimitsDaoImpl(
                dispatcher = CoroutineDispatchers.background,
                databaseWrapper = get()
            )
        }

        /** Settings **/
        single<MultiplatformSettings> {
            val settings = Settings()
            MultiplatformSettingsImpl(settings = settings)
        }
    }
}

internal expect fun platformModule(): Module
