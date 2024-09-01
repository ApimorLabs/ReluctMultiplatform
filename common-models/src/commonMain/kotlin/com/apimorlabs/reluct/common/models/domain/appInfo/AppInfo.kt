package com.apimorlabs.reluct.common.models.domain.appInfo

data class AppInfo(
    val packageName: String,
    val appName: String,
    val appIcon: com.apimorlabs.reluct.common.models.domain.appInfo.Icon,
) {
    override fun equals(other: Any?): Boolean {
        return other?.let { item ->
            item is com.apimorlabs.reluct.common.models.domain.appInfo.AppInfo && item.appName == this.appName && item.packageName == this.packageName
        } ?: false
    }

    /**
     * This item's hashcode will likely not be the same since the app icon is generated
     * in a way that invalidates the equality. Use something else to check if they are the same
     */
    override fun hashCode(): Int {
        return packageName.hashCode()
    }
}
