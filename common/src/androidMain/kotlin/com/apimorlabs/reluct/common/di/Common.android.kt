package com.apimorlabs.reluct.common.di

import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import com.apimorlabs.reluct.common.sources.DbClient

internal actual fun platform(): Module = module {
    singleOf(::DbClient)
}
