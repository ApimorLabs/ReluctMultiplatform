package com.apimorlabs.reluct.features.settings

import com.apimorlabs.reluct.data.source.settings.MultiplatformSettings
import kotlinx.coroutines.flow.Flow

class GetSettings(private val settings: MultiplatformSettings) {
    val theme: Flow<Int> = settings.theme
    val savedVersionCode: Flow<Int> = settings.savedVersionCode
    val loginSkipped: Flow<Boolean> = settings.loginSkipped
    val onBoardingShown: Flow<Boolean> = settings.onBoardingShown
    fun saveVersionCode(versionCode: Int) = settings.saveVersionCode(versionCode)
    fun getTheme(): Int = settings.getTheme()
    fun getOnBoardingShow(): Boolean = settings.getOnBoardingShown()
}
