package app.trailblazercombi.haventide.game2.jetpack.gamescreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import app.trailblazercombi.haventide.game2.data.GameLoop
import app.trailblazercombi.haventide.game2.jetpack.gamescreen.dialogs.EndRoundDialog
import app.trailblazercombi.haventide.game2.jetpack.gamescreen.dialogs.ForfeitConfirmationDialog
import app.trailblazercombi.haventide.game2.jetpack.gamescreen.dialogs.GameOverDialog
import app.trailblazercombi.haventide.game2.jetpack.gamescreen.dialogs.PauseMenuDialog
import app.trailblazercombi.haventide.game2.jetpack.gamescreen.panels.DiceInfoPanel
import app.trailblazercombi.haventide.game2.jetpack.gamescreen.tilemap.TileMap
import app.trailblazercombi.haventide.game2.jetpack.gamescreen.panels.GameStatusPanel
import app.trailblazercombi.haventide.game2.jetpack.universal.YesNoDialog
import app.trailblazercombi.haventide.game2.viewModel.GameLoopViewModel
import app.trailblazercombi.haventide.resources.Res
import app.trailblazercombi.haventide.resources.yes_no_dialog_forfeit_no
import app.trailblazercombi.haventide.resources.yes_no_dialog_forfeit_title
import app.trailblazercombi.haventide.resources.yes_no_dialog_forfeit_yes
import app.trailblazercombi.haventide.resources.ButtonSeverity

@Composable
fun GameScreen(viewModel: GameLoopViewModel, gameLoop: GameLoop, modifier: Modifier = Modifier) {
//    val viewModel by remember { mutableStateOf(GameLoopViewModel(gameLoop)) }

    BoxWithConstraints(Modifier.fillMaxSize()) {
        LaunchedEffect(maxWidth, maxHeight) {
            viewModel.screenWidth.value = maxWidth
            viewModel.screenHeight.value = maxHeight
            println("WiM $maxWidth x $maxHeight")
        }
    }

    // The contents of the game screen
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        TileMap(viewModel)
        GameStatusPanel(viewModel)
        DiceInfoPanel(viewModel)
    }

    // The dialogs
    PauseMenuDialog(viewModel)
    EndRoundDialog(viewModel)
//    StartRoundDialog(viewModel)
    ForfeitConfirmationDialog(viewModel)
    GameOverDialog(viewModel)

    // Once all is done, start the game - TODO Make it better
    viewModel.updateTileHighlights()
}
