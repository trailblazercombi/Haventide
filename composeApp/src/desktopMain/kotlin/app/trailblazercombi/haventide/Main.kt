package app.trailblazercombi.haventide

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import app.trailblazercombi.haventide.netcode.TcpServer
import app.trailblazercombi.haventide.netcode.stopTcpClient
import app.trailblazercombi.haventide.netcode.stopTcpServer

fun main() = application {

    Window(
        onCloseRequest = {
            stopTcpClient()
            stopTcpServer()
            exitApplication()
        },
        title = "Haventide",
    ) {
        App()
    }
}
