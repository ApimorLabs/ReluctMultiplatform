package di

import dependencies.DbClient
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal actual val platformModule: Module = module {
    singleOf(::DbClient)
}