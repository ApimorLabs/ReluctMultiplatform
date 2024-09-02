package com.apimorlabs.reluct.domain.usecases.appInfo

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import com.apimorlabs.reluct.common.models.domain.appInfo.AppInfo
import com.apimorlabs.reluct.domain.util.hasMainActivity
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class AndroidGetInstalledApps(
    private val context: Context,
    private val getAppInfo: GetAppInfo,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : GetInstalledApps {
    private val packageManager = context.packageManager

    @SuppressLint("QueryPermissionsNeeded")
    override suspend fun getApps(): ImmutableList<AppInfo> = withContext(dispatcher) {
        val installedApps = packageManager.run {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                getInstalledApplications(
                    PackageManager.ApplicationInfoFlags.of(PackageManager.GET_META_DATA.toLong())
                )
            } else {
                //@Suppress("DEPRECATION")
                getInstalledApplications(PackageManager.GET_META_DATA)
            }
        }
        installedApps.filter {
            hasMainActivity(context = context, packageName = it.packageName)
        }.map {
            AppInfo(
                packageName = it.packageName,
                appName = getAppInfo.getAppName(it.packageName),
                appIcon = getAppInfo.getAppIcon(it.packageName)
            )
        }
            .sortedBy { it.appName }
            .toImmutableList()
    }
}