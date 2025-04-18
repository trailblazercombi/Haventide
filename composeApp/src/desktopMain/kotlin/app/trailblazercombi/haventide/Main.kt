package app.trailblazercombi.haventide

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import app.trailblazercombi.haventide.game.arena.GameLoop
import app.trailblazercombi.haventide.game.arena.PlaceholderPlayers

fun main(args: Array<String>) = application {
    val gameLoop = GameLoop(
        PlaceholderPlayers.PLAYER_ONE.toProfile(),
        PlaceholderPlayers.PLAYER_TWO.toProfile()
    )

    Window(
        onCloseRequest = ::exitApplication,
        title = "Haventide",
    ) {
        App(gameLoop)
    }
}
