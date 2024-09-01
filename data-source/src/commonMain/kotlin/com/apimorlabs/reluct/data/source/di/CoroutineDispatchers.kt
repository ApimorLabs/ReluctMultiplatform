package com.apimorlabs.reluct.data.source.di

import kotlinx.coroutines.CoroutineDispatcher

internal expect object CoroutineDispatchers {
    val background: CoroutineDispatcher
}
