package app.trailblazercombi.haventide

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import app.trailblazercombi.haventide.game2.data.GameLoop
import app.trailblazercombi.haventide.game2.jetpack.gamescreen.ComposableGameScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(launchParameters: GameLoop) {
    val gameLoop by remember { mutableStateOf(launchParameters) }

    MaterialTheme {
        ComposableGameScreen(gameLoop)
    }
}
