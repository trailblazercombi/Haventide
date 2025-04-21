package app.trailblazercombi.haventide

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import app.trailblazercombi.haventide.game2.data.GameLoop
import app.trailblazercombi.haventide.resources.PlaceholderPlayers

fun main() = application {

    Window(
        onCloseRequest = ::exitApplication,
        title = "Haventide",
    ) {
        App()
    }
}
