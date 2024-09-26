package com.apimorlabs.reluct.domain.usecases.appInfo

import android.content.Context
import com.apimorlabs.reluct.common.models.domain.appInfo.Icon
import com.apimorlabs.reluct.data.source.util.AppInfoHelpers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class AndroidGetAppInfo(
    private val context: Context,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : GetAppInfo {

    override suspend fun getAppIcon(packageName: String): Icon = withContext(dispatcher) {
        AppInfoHelpers.getAppIcon(context, packageName)
    }

    override suspend fun getAppName(packageName: String): String = withContext(dispatcher) {
        AppInfoHelpers.getAppName(context, packageName)
    }
}
