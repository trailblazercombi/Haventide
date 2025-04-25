package app.trailblazercombi.haventide

import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import app.trailblazercombi.haventide.game2.data.GameLoop
import app.trailblazercombi.haventide.netcode.NetPairing
import app.trailblazercombi.haventide.netcode.TcpClient
import app.trailblazercombi.haventide.netcode.TcpServer
import app.trailblazercombi.haventide.resources.PlaceholderPlayers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

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
