package com.apimorlabs.reluct.data.source.di

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.apimorlabs.reluct.data.source.database.ReluctDatabase
import com.apimorlabs.reluct.data.source.database.adapters.GoalsTableAdapter
import com.apimorlabs.reluct.data.source.database.adapters.TasksTableAdapter
import com.apimorlabs.reluct.data.source.database.dao.DatabaseWrapper

internal actual fun getDatabaseWrapper(): DatabaseWrapper {
    val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        .also { ReluctDatabase.Schema.create(it) }
    return DatabaseWrapper(
        ReluctDatabase(
            driver,
            GoalsTableAdapter = GoalsTableAdapter,
            TasksTableAdapter = TasksTableAdapter
        )
    )
}