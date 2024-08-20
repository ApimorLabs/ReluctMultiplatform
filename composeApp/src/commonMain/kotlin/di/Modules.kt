package di

import dependencies.MyRepository
import dependencies.MyRepositoryImpl
import dependencies.MyViewModel
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal expect val platformModule: Module

internal val sharedModule = module {
    //    single<MyRepository> {
    //        MyRepositoryImpl(dbClient = get())
    //    }
    // Can also be written as;
    singleOf(::MyRepositoryImpl).bind<MyRepository>()
    viewModelOf(::MyViewModel)
}