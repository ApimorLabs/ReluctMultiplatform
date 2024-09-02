package com.apimorlabs.reluct.domain.usecases.limits.impl

import com.apimorlabs.reluct.common.models.domain.limits.AppLimits
import com.apimorlabs.reluct.data.source.database.dao.screentime.LimitsDao
import com.apimorlabs.reluct.domain.mappers.asAppLimits
import com.apimorlabs.reluct.domain.usecases.appInfo.GetAppInfo
import com.apimorlabs.reluct.domain.usecases.limits.GetAppLimits
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.withContext

internal class GetAppLimitsImpl(
    private val limitsDao: LimitsDao,
    private val getAppInfo: GetAppInfo,
    private val dispatcher: CoroutineDispatcher
) : GetAppLimits {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getApp(packageName: String): Flow<AppLimits> =
        limitsDao.getApp(packageName).mapLatest { it.asAppLimits(getAppInfo) }
            .flowOn(dispatcher)

    override suspend fun getAppSync(packageName: String): AppLimits = withContext(dispatcher) {
        limitsDao.getAppSync(packageName).asAppLimits(getAppInfo)
    }
}