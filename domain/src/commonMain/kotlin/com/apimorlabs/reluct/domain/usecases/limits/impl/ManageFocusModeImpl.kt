package com.apimorlabs.reluct.domain.usecases.limits.impl

import com.apimorlabs.reluct.data.source.settings.MultiplatformSettings
import com.apimorlabs.reluct.domain.usecases.limits.ManageFocusMode
import com.apimorlabs.reluct.system.services.haptics.HapticFeedback
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

internal class ManageFocusModeImpl(
    private val settings: MultiplatformSettings,
    private val haptics: HapticFeedback,
    private val backgroundDispatcher: CoroutineDispatcher
) : ManageFocusMode {
    override val isFocusModeOn: Flow<Boolean> = settings.focusMode.flowOn(backgroundDispatcher)

    override val isDoNotDisturbOn: Flow<Boolean> = settings.doNoDisturb.flowOn(backgroundDispatcher)

    override val isAppBlockingEnabled: Flow<Boolean> = settings.appBlockingEnabled
        .flowOn(backgroundDispatcher)

    override suspend fun toggleFocusMode(isFocusMode: Boolean) {
        withContext(backgroundDispatcher) {
            settings.saveFocusMode(isFocusMode)
            haptics.tick()
        }
    }

    override suspend fun toggleDoNoDisturb(isDnd: Boolean) {
        withContext(backgroundDispatcher) {
            settings.saveDoNotDisturb(isDnd)
            haptics.tick()
        }
    }
}