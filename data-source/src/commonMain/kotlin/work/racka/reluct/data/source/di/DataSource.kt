package work.racka.reluct.data.source.di

import org.koin.core.KoinApplication
import org.koin.core.module.Module
import org.koin.dsl.module
import work.racka.reluct.data.source.database.dao.goals.GoalsDao
import work.racka.reluct.data.source.database.dao.goals.GoalsDaoImpl
import work.racka.reluct.data.source.database.dao.screentime.LimitsDao
import work.racka.reluct.data.source.database.dao.screentime.LimitsDaoImpl
import work.racka.reluct.data.source.database.dao.tasks.TasksDao
import work.racka.reluct.data.source.database.dao.tasks.TasksDaoImpl

object DataSource {
    fun KoinApplication.install() = apply {
        modules(sharedModule(), platformModule())
    }

    private fun sharedModule(): Module = module {
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
    }
}

internal expect fun platformModule(): Module
