package com.apimorlabs.reluct.domain.usecases.limits.impl

import com.apimorlabs.reluct.common.models.domain.limits.AppLimits
import com.apimorlabs.reluct.data.source.database.dao.screentime.LimitsDao
import com.apimorlabs.reluct.domain.mappers.asLimitsDbObject
import com.apimorlabs.reluct.domain.usecases.limits.ModifyAppLimits
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class ModifyAppLimitsImpl(
    private val limitsDao: LimitsDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ModifyAppLimits {

    override suspend fun insertApp(appLimits: AppLimits) = withContext(dispatcher) {
        limitsDao.insertApp(appLimits.asLimitsDbObject())
    }

    override suspend fun removeApp(packageName: String) = withContext(dispatcher) {
        limitsDao.removeApp(packageName)
    }

    override suspend fun removeAllApp(packageName: String) = withContext(dispatcher) {
        limitsDao.removeAllApps()
    }

    override suspend fun resumeAllApps() = withContext(dispatcher) {
        limitsDao.resumeAllApps()
    }

    override suspend fun setLimit(packageName: String, timeLimit: Long) = withContext(dispatcher) {
        limitsDao.setTimeLimit(packageName, timeLimit)
    }

    override suspend fun pauseApp(packageName: String, isPaused: Boolean) =
        withContext(dispatcher) {
            limitsDao.togglePausedApp(packageName, isPaused)
        }

    override suspend fun makeDistractingApp(packageName: String, isDistracting: Boolean) =
        withContext(dispatcher) {
            limitsDao.toggleDistractingApp(packageName, isDistracting)
        }

    override suspend fun overrideLimit(packageName: String, overridden: Boolean) =
        withContext(dispatcher) {
            limitsDao.toggleLimitOverride(packageName, overridden)
        }
}
