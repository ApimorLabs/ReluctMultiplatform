package com.apimorlabs.reluct

import android.app.Application
import com.apimorlabs.reluct.common.di.KoinMain
import org.koin.android.ext.koin.androidContext

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        KoinMain.init {
            androidContext(this@MyApplication)
        }
    }
}
