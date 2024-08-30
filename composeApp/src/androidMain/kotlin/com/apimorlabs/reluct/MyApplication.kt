package com.apimorlabs.reluct

import android.app.Application
import org.koin.android.ext.koin.androidContext
import com.apimorlabs.reluct.common.di.KoinMain

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        KoinMain.init {
            androidContext(this@MyApplication)
        }
    }
}
