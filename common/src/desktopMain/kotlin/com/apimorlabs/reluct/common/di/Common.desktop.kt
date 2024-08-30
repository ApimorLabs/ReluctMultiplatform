package com.apimorlabs.reluct.common.di

import com.apimorlabs.reluct.common.sources.DbClient
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal actual fun platform(): Module = module {
    singleOf(::DbClient)
}
