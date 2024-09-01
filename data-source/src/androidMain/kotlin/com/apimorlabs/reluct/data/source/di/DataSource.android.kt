package com.apimorlabs.reluct.data.source.di

import android.app.usage.UsageStatsManager
import android.content.Context
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.apimorlabs.reluct.data.source.appUsageStats.manager.AndroidUsageDataManager
import com.apimorlabs.reluct.data.source.appUsageStats.manager.UsageDataManager
import com.apimorlabs.reluct.data.source.database.ReluctDatabase
import com.apimorlabs.reluct.data.source.database.adapters.GoalsTableAdapter
import com.apimorlabs.reluct.data.source.database.adapters.TasksTableAdapter
import com.apimorlabs.reluct.data.source.database.dao.DatabaseWrapper
import com.apimorlabs.reluct.data.source.util.Constants
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual fun platformModule(): Module = module {
    single<DatabaseWrapper> {
        val driver = AndroidSqliteDriver(
            schema = ReluctDatabase.Schema,
            context = androidContext().applicationContext,
            name = Constants.RELUCT_DATABASE
        )
        DatabaseWrapper(
            instance = ReluctDatabase(
                driver = driver,
                GoalsTableAdapter = GoalsTableAdapter,
                TasksTableAdapter = TasksTableAdapter
            )
        )
    }

    single<UsageDataManager> {
        AndroidUsageDataManager(
            context = androidContext().applicationContext,
            usageStats = androidContext()
                .getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        )
    }
}
