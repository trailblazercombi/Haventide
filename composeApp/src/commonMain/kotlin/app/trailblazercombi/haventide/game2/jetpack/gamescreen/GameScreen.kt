package app.trailblazercombi.haventide.game2.jetpack.gamescreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import app.trailblazercombi.haventide.game2.data.GameLoop
import app.trailblazercombi.haventide.game2.jetpack.gamescreen.dialogs.AcceptDrawDialog
import app.trailblazercombi.haventide.game2.jetpack.gamescreen.dialogs.EndRoundDialog
import app.trailblazercombi.haventide.game2.jetpack.gamescreen.dialogs.ForfeitConfirmationDialog
import app.trailblazercombi.haventide.game2.jetpack.gamescreen.dialogs.GameOverDialog
import app.trailblazercombi.haventide.game2.jetpack.gamescreen.dialogs.OfferDrawDialog
import app.trailblazercombi.haventide.game2.jetpack.gamescreen.dialogs.PauseMenuDialog
import app.trailblazercombi.haventide.game2.jetpack.gamescreen.panels.BottomPanel
import app.trailblazercombi.haventide.game2.jetpack.gamescreen.panels.CiPanel
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
fun GameScreen(
    viewModel: GameLoopViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    println("GAME SCREEN ONLINE!!!")

    // This just figures out the current screen size...
    BoxWithConstraints(Modifier.fillMaxSize()) {
        LaunchedEffect(maxWidth, maxHeight) {
            viewModel.screenWidth.value = maxWidth
            viewModel.screenHeight.value = maxHeight
            println("[GS WiM] $maxWidth x $maxHeight")
        }
    }

    // The contents of the game screen
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        TileMap(viewModel)
        GameStatusPanel(viewModel)
//        DiceInfoPanel(viewModel)
//        CiPanel(viewModel)
        BottomPanel(viewModel)
    }

    // The dialogs
    PauseMenuDialog(viewModel)
    EndRoundDialog(viewModel)
//    StartRoundDialog(viewModel)
    ForfeitConfirmationDialog(viewModel)
    OfferDrawDialog(viewModel)
    AcceptDrawDialog(viewModel)
    GameOverDialog(viewModel, navController)

    LaunchedEffect(Unit) {
        viewModel.updateTileHighlights()
    }
}
