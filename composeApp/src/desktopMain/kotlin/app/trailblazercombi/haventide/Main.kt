package app.trailblazercombi.haventide

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import app.trailblazercombi.haventide.netcode.TcpServer

fun main() = application {

    Window(
        onCloseRequest = {
//            Global.netScope.cancel()
            TcpServer.stop()
            exitApplication()
        },
        title = "Haventide",
    ) {
        App()
    }
}
