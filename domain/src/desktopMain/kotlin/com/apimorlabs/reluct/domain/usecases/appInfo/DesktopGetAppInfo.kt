package com.apimorlabs.reluct.domain.usecases.appInfo

import com.apimorlabs.reluct.common.models.domain.appInfo.Icon

internal class DesktopGetAppInfo : GetAppInfo {
    override suspend fun getAppIcon(packageName: String): Icon {
        return Icon(byteArrayOf(0))
    }

    override suspend fun getAppName(packageName: String): String {
        return packageName
    }
}