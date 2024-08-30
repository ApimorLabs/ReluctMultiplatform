package com.apimorlabs.reluct.data.source.di

import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module
import work.racka.reluct.data.source.database.ReluctDatabase
import work.racka.reluct.data.source.database.adapters.GoalsTableAdapter
import work.racka.reluct.data.source.database.adapters.TasksTableAdapter
import work.racka.reluct.data.source.database.dao.DatabaseWrapper
import work.racka.reluct.data.source.util.Constants

internal actual fun platformModule(): Module = module {
    single<DatabaseWrapper> {
        val driver = AndroidSqliteDriver(
            schema = ReluctDatabase.Schema,
            context = androidContext(),
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
}
