package app.trailblazercombi.haventide

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import app.trailblazercombi.haventide.game2.data.GameLoop
import app.trailblazercombi.haventide.game2.jetpack.gamescreen.GameScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(launchParameters: GameLoop) {
    val gameLoop by remember { mutableStateOf(launchParameters) }
    val viewModel by remember { mutableStateOf(launchParameters.viewModel) }

    MaterialTheme {
        GameScreen(viewModel, gameLoop)
    }
}
