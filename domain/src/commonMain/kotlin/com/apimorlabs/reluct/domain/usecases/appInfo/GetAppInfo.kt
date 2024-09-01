package com.apimorlabs.reluct.domain.usecases.appInfo

import com.apimorlabs.reluct.common.models.domain.appInfo.Icon

interface GetAppInfo {
    suspend fun getAppIcon(packageName: String): Icon
    suspend fun getAppName(packageName: String): String
}
