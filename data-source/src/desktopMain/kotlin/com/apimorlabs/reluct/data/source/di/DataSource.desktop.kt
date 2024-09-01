package com.apimorlabs.reluct.data.source.di

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.apimorlabs.reluct.data.source.appUsageStats.manager.DesktopUsageDataManager
import com.apimorlabs.reluct.data.source.appUsageStats.manager.UsageDataManager
import com.apimorlabs.reluct.data.source.database.ReluctDatabase
import com.apimorlabs.reluct.data.source.database.adapters.GoalsTableAdapter
import com.apimorlabs.reluct.data.source.database.adapters.TasksTableAdapter
import com.apimorlabs.reluct.data.source.database.dao.DatabaseWrapper
import com.apimorlabs.reluct.data.source.util.Constants
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import java.io.File

internal actual fun platformModule(): Module = module {
    single<DatabaseWrapper> {
        val dbPath = File(System.getProperty("java.io.tmpdir"), Constants.RELUCT_DATABASE)
        val driver = JdbcSqliteDriver(url = "jdbc:sqlite:${dbPath.absolutePath}")
            .also { ReluctDatabase.Schema.create(it) }
        DatabaseWrapper(
            instance = ReluctDatabase(
                driver = driver,
                GoalsTableAdapter = GoalsTableAdapter,
                TasksTableAdapter = TasksTableAdapter
            )
        )
    }

    singleOf(::DesktopUsageDataManager).bind<UsageDataManager>()
}
