import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import work.racka.template.common.di.KoinMain

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
