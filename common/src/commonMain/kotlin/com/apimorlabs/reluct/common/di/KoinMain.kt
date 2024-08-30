package com.apimorlabs.reluct.common.di

import com.apimorlabs.reluct.data.source.di.DataSource
import com.apimorlabs.reluct.system.services.di.SystemServices
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

object KoinMain {
    fun init(
        appDeclaration: KoinAppDeclaration = {}
    ) {
        startKoin {
            appDeclaration()

            // Initialize modules
            Common.run { install() }
            DataSource.run { install() }
            SystemServices.run { install() }
        }
    }
}
