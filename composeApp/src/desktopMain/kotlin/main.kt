import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import di.KoinMain

fun main() {
    KoinMain.initKoin()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "KMP Hello World",
        ) {
            App()
        }
    }
}