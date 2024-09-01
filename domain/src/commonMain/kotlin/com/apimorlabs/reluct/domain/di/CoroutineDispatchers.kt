package com.apimorlabs.reluct.domain.di

import kotlinx.coroutines.CoroutineDispatcher

internal expect object CoroutineDispatchers {
    val background: CoroutineDispatcher
}
