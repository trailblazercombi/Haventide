package app.trailblazercombi.haventide

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import app.trailblazercombi.haventide.game.arena.*
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    val gameLoop = GameLoop(
        PlaceholderPlayers.PLAYER_ONE.toProfile(),
        PlaceholderPlayers.PLAYER_TWO.toProfile()
    ).toViewModel()

    MaterialTheme {
        ComposableGameScreen(gameLoop)
    }
}
