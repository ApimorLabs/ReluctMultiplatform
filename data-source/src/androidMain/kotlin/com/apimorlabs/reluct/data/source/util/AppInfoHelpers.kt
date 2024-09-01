package com.apimorlabs.reluct.data.source.util

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import androidx.core.content.res.ResourcesCompat
import com.apimorlabs.reluct.data.model.domain.appInfo.Icon
import com.apimorlabs.reluct.data.source.R

const val APP_INFO_LOG = "APP_INFO_LOG"

internal fun getAppIcon(context: Context, packageName: String): Icon {

    // Quite dangerous, but should only happen if the drawable is not found
    var appIcon: Drawable =
        ResourcesCompat.getDrawable(context.resources, R.drawable.default_app_icon, null)!!
    try {
        Log.d(APP_INFO_LOG, "PackageName: $packageName")
        val newContext =
            context.createPackageContext(packageName, Context.CONTEXT_IGNORE_SECURITY)
        appIcon = newContext.packageManager.getApplicationIcon(packageName)
    } catch (e: PackageManager.NameNotFoundException) {
        Log.d(APP_INFO_LOG, "Package Name not found: ${e.message}")
    } catch (e: Exception) {
        Log.d(APP_INFO_LOG, "Error: ${e.message}")
    }
    return Icon(appIcon)
}

// If application name can't be found the package name will be returned
internal fun getAppName(context: Context, packageName: String): String {
    var appName = packageName
    try {
        //Timber.d("PackageName: $packageName")
        val newContext =
            context.createPackageContext(packageName, Context.CONTEXT_IGNORE_SECURITY)
        val packageManager = newContext.packageManager
        val appInfo = packageManager.run {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                getApplicationInfo(
                    packageName,
                    PackageManager.ApplicationInfoFlags.of(PackageManager.GET_META_DATA.toLong())
                )
            } else {
                //@Suppress("DEPRECATION")
                packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
            }
        }
        appName = packageManager.getApplicationLabel(appInfo).toString()
    } catch (e: PackageManager.NameNotFoundException) {
        Log.d(APP_INFO_LOG, "Package Name not found: ${e.message}")
    } catch (e: Exception) {
        Log.d(APP_INFO_LOG, "Error: ${e.message}")
    }
    return appName
}

// Check if it is not a system app
internal fun isSystemApp(context: Context, packageName: String): Boolean {
    val newContext =
        context.createPackageContext(packageName, Context.CONTEXT_IGNORE_SECURITY)
    val packageManager = newContext.packageManager
    val appInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        packageManager.getApplicationInfo(
            packageName,
            PackageManager.ApplicationInfoFlags.of(PackageManager.GET_META_DATA.toLong())
        )
    } else {
        //@Suppress("DEPRECATION")
        packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
    }
    return appInfo.flags == ApplicationInfo.FLAG_SYSTEM
}

/*
internal fun getDominantAppIconColor(appIcon: Drawable?): Int {
    val defaultColor = Color.GRAY
    val palette = appIcon?.let {
        Palette
            .from(it.toBitmap())
            .generate()
    }
    return palette?.getVibrantColor(defaultColor) ?: defaultColor
}*/
