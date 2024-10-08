package com.apimorlabs.reluct.data.source.settings

import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

internal class MultiplatformSettingsImpl(
    private val settings: Settings
) : MultiplatformSettings {

    private val _themeFlow = MutableSharedFlow<Int>(replay = 1)
    private val _focusMode = MutableSharedFlow<Boolean>(replay = 1)
    private val _doNotDisturb = MutableSharedFlow<Boolean>(replay = 1)
    private val _savedVersionCode = MutableSharedFlow<Int>(replay = 1)
    private val _onBoardingShown = MutableSharedFlow<Boolean>(replay = 1)
    private val _appBlockingEnabled = MutableSharedFlow<Boolean>(replay = 1)
    private val _loginSkipped = MutableSharedFlow<Boolean>(replay = 1)

    override val theme: Flow<Int> = _themeFlow.asSharedFlow()
    override val focusMode: Flow<Boolean> = _focusMode.asSharedFlow()
    override val doNoDisturb: Flow<Boolean> = _doNotDisturb.asSharedFlow()
    override val savedVersionCode: Flow<Int> = _savedVersionCode.asSharedFlow()
    override val onBoardingShown: Flow<Boolean> = _onBoardingShown.asSharedFlow()
    override val appBlockingEnabled: Flow<Boolean> = _appBlockingEnabled.asSharedFlow()
    override val loginSkipped: Flow<Boolean> = _loginSkipped.asSharedFlow()

    init {
        initializeSettings()
    }

    private fun initializeSettings() {
        _themeFlow.tryEmit(readThemeSettings())
        _focusMode.tryEmit(readFocusMode())
        _doNotDisturb.tryEmit(readDoNotDisturb())
        _onBoardingShown.tryEmit(readOnBoardingShown())
        _savedVersionCode.tryEmit(readSavedVersionCode())
        _appBlockingEnabled.tryEmit(readAppBlocking())
        _loginSkipped.tryEmit(readLoginSkipped())
    }

    override fun saveThemeSettings(value: Int): Boolean {
        settings.putInt(key = Keys.THEME_OPTION, value = value)
        return _themeFlow.tryEmit(readThemeSettings())
    }

    override fun saveFocusMode(value: Boolean): Boolean {
        settings.putBoolean(key = Keys.FOCUS_MODE, value = value)
        return _focusMode.tryEmit(readFocusMode())
    }

    override fun saveDoNotDisturb(value: Boolean): Boolean {
        settings.putBoolean(key = Keys.DO_NOT_DISTURB, value = value)
        return _doNotDisturb.tryEmit(readDoNotDisturb())
    }

    override fun saveVersionCode(value: Int): Boolean {
        settings.putInt(key = Keys.SAVED_VERSION_CODE, value = value)
        return _savedVersionCode.tryEmit(readSavedVersionCode())
    }

    override fun saveOnBoardingShown(value: Boolean): Boolean {
        settings.putBoolean(key = Keys.ON_BOARDING_SHOWN, value = value)
        return _onBoardingShown.tryEmit(readOnBoardingShown())
    }

    override fun saveAppBlocking(value: Boolean): Boolean {
        settings.putBoolean(key = Keys.APP_BLOCKING, value = value)
        return _appBlockingEnabled.tryEmit(readAppBlocking())
    }

    override fun saveLoginSkipped(value: Boolean): Boolean {
        settings.putBoolean(key = Keys.LOGIN_SKIPPED, value = value)
        return _loginSkipped.tryEmit(readLoginSkipped())
    }

    // Private Read methods
    private fun readThemeSettings(): Int =
        settings.getInt(key = Keys.THEME_OPTION, defaultValue = Keys.Defaults.THEME)

    private fun readFocusMode(): Boolean =
        settings.getBoolean(key = Keys.FOCUS_MODE, defaultValue = false)

    private fun readDoNotDisturb(): Boolean =
        settings.getBoolean(key = Keys.DO_NOT_DISTURB, defaultValue = false)

    private fun readSavedVersionCode(): Int =
        settings.getInt(key = Keys.SAVED_VERSION_CODE, defaultValue = 0)

    private fun readOnBoardingShown(): Boolean =
        settings.getBoolean(key = Keys.ON_BOARDING_SHOWN, defaultValue = false)

    private fun readAppBlocking(): Boolean =
        settings.getBoolean(key = Keys.APP_BLOCKING, defaultValue = true)

    private fun readLoginSkipped(): Boolean =
        settings.getBoolean(key = Keys.LOGIN_SKIPPED, defaultValue = false)
}
