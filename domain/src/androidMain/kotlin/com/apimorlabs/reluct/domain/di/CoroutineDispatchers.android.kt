package com.apimorlabs.reluct.domain.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

internal actual object CoroutineDispatchers {
    actual val background: CoroutineDispatcher = Dispatchers.IO
}
