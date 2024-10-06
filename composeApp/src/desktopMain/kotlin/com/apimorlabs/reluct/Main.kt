package com.apimorlabs.reluct

import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.apimorlabs.reluct.common.di.KoinMain
import com.apimorlabs.reluct.compose.ui.Res
import com.apimorlabs.reluct.compose.ui.app_name
import org.jetbrains.compose.resources.stringResource
import java.awt.Dimension

fun main() {
    KoinMain.init()
    application {
        val windowState = rememberWindowState(
            size = DpSize(width = 1100.dp, height = 600.dp),
            placement = WindowPlacement.Floating,
            position = WindowPosition.Aligned(Alignment.Center)
        )

        Window(
            onCloseRequest = ::exitApplication,
            state = windowState,
            title = stringResource(Res.string.app_name),
        ) {
            setWindowMinSize(800, 500)

            App()
        }
    }
}

fun FrameWindowScope.setWindowMinSize(width: Int, height: Int) {
    window.minimumSize = Dimension(width, height)
}
