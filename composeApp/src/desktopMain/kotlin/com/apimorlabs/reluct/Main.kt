package com.apimorlabs.reluct

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.apimorlabs.reluct.common.di.KoinMain

fun main() {
    KoinMain.init()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "KMP Hello World",
        ) {
            App()
        }
    }
}
