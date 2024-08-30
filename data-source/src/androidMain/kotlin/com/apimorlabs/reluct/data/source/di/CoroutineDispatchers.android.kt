package com.apimorlabs.reluct.data.source.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

actual object CoroutineDispatchers {
    actual val background: CoroutineDispatcher = Dispatchers.IO
}
