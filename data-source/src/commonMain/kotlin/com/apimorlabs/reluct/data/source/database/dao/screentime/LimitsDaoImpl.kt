package com.apimorlabs.reluct.data.source.database.dao.screentime

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrDefault
import com.apimorlabs.reluct.data.source.database.dao.DatabaseWrapper
import com.apimorlabs.reluct.data.source.database.dao.screentime.LimitsHelpers.getAppFromDb
import com.apimorlabs.reluct.data.source.database.dao.screentime.LimitsHelpers.getDistractingAppsFromDb
import com.apimorlabs.reluct.data.source.database.dao.screentime.LimitsHelpers.getPausedAppsFromDb
import com.apimorlabs.reluct.data.source.database.dao.screentime.LimitsHelpers.insertAppToDb
import com.apimorlabs.reluct.data.source.database.models.LimitsDbObject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

internal class LimitsDaoImpl(
    private val dispatcher: CoroutineDispatcher,
    databaseWrapper: DatabaseWrapper
) : LimitsDao {

    private val limitsQueries = databaseWrapper.instance?.limitsTableQueries

    override suspend fun insertApp(appLimit: LimitsDbObject) {
        limitsQueries?.insertAppToDb(limit = appLimit)
    }

    override fun getApp(packageName: String): Flow<LimitsDbObject> =
        limitsQueries?.getAppFromDb(packageName = packageName)?.asFlow()
            ?.mapToOneOrDefault(
                LimitsDbObject(
                    packageName = packageName,
                    timeLimit = 0,
                    isADistractingAp = false,
                    isPaused = false,
                    overridden = false
                ),
                context = dispatcher
            )
            ?: flowOf(
                LimitsDbObject(
                    packageName = packageName,
                    timeLimit = 0,
                    isADistractingAp = false,
                    isPaused = false,
                    overridden = false
                )
            )

    override suspend fun getAppSync(packageName: String): LimitsDbObject =
        limitsQueries?.getAppFromDb(packageName = packageName)?.executeAsOneOrNull()
            ?: LimitsDbObject(
                packageName = packageName,
                timeLimit = 0,
                isADistractingAp = false,
                isPaused = false,
                overridden = false
            )

    override fun getDistractingApps(): Flow<List<LimitsDbObject>> =
        limitsQueries?.getDistractingAppsFromDb()
            ?.asFlow()
            ?.mapToList(context = dispatcher) ?: flowOf(emptyList())

    override fun getDistractingAppsSync(): List<LimitsDbObject> =
        limitsQueries?.getDistractingAppsFromDb()?.executeAsList() ?: emptyList()

    override suspend fun isDistractingApp(packageName: String): Boolean =
        limitsQueries?.isDistractingApp(packageName)?.executeAsOneOrNull() ?: false

    override suspend fun removeApp(packageName: String) {
        limitsQueries?.transaction {
            limitsQueries.removeApp(packageName = packageName)
        }
    }

    override suspend fun removeAllApps() {
        limitsQueries?.transaction {
            limitsQueries.removeAllApps()
        }
    }

    override suspend fun setTimeLimit(packageName: String, timeLimit: Long) {
        limitsQueries?.transaction {
            limitsQueries.setTimeLimit(
                timeLimit = timeLimit,
                packageName = packageName
            )
        }
    }

    override suspend fun togglePausedApp(packageName: String, isPaused: Boolean) {
        limitsQueries?.transaction {
            limitsQueries.togglePausedApp(
                isPaused = isPaused,
                packageName = packageName
            )
        }
    }

    override suspend fun toggleDistractingApp(packageName: String, isDistracting: Boolean) {
        limitsQueries?.transaction {
            limitsQueries.toggleDistractingApp(
                isDistracting = isDistracting,
                packageName = packageName
            )
        }
    }

    override suspend fun toggleLimitOverride(packageName: String, overridden: Boolean) {
        limitsQueries?.transaction {
            limitsQueries.toggleLimitOverride(
                overridden = overridden,
                packageName = packageName
            )
        }
    }

    override fun getPausedApps(): Flow<List<LimitsDbObject>> =
        limitsQueries?.getPausedAppsFromDb()
            ?.asFlow()
            ?.mapToList(dispatcher)
            ?: flowOf(emptyList())

    override suspend fun getPausedAppsSync(): List<LimitsDbObject> =
        limitsQueries?.getPausedAppsFromDb()?.executeAsList() ?: emptyList()

    override suspend fun isAppPaused(packageName: String): Boolean =
        limitsQueries?.isAppPaused(packageName = packageName)?.executeAsOneOrNull() ?: false

    override suspend fun resumeAllApps() {
        limitsQueries?.transaction {
            limitsQueries.resumeAllApps()
        }
    }
}
