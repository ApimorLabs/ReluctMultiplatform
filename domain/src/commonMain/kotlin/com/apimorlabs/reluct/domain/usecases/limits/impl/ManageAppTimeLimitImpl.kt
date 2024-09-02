package com.apimorlabs.reluct.domain.usecases.limits.impl

import com.apimorlabs.reluct.common.models.domain.limits.AppTimeLimit
import com.apimorlabs.reluct.data.source.database.dao.screentime.LimitsDao
import com.apimorlabs.reluct.domain.mappers.asTimeLimit
import com.apimorlabs.reluct.domain.mappers.convertTimeToMillis
import com.apimorlabs.reluct.domain.usecases.appInfo.GetAppInfo
import com.apimorlabs.reluct.domain.usecases.limits.ManageAppTimeLimit
import com.apimorlabs.reluct.system.services.haptics.HapticFeedback
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.withContext

internal class ManageAppTimeLimitImpl(
    private val limitsDao: LimitsDao,
    private val getAppInfo: GetAppInfo,
    private val haptics: HapticFeedback,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ManageAppTimeLimit {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getLimit(packageName: String): Flow<AppTimeLimit> =
        limitsDao.getApp(packageName).mapLatest { it.asTimeLimit(getAppInfo) }
            .flowOn(dispatcher)

    override suspend fun getSync(packageName: String): AppTimeLimit = withContext(dispatcher) {
        limitsDao.getAppSync(packageName).asTimeLimit(getAppInfo)
    }

    override suspend fun setTimeLimit(appTimeLimit: AppTimeLimit) = withContext(dispatcher) {
        haptics.tick()
        val timeInMillis =
            convertTimeToMillis(hours = appTimeLimit.hours, minutes = appTimeLimit.minutes)
        limitsDao.setTimeLimit(packageName = appTimeLimit.appInfo.packageName, timeInMillis)
    }
}
