package app.trailblazercombi.haventide.game2.jetpack.gamescreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import app.trailblazercombi.haventide.game2.data.GameLoop
import app.trailblazercombi.haventide.game2.jetpack.gamescreen.dialogs.AbilityPickerDialog
import app.trailblazercombi.haventide.game2.jetpack.gamescreen.dialogs.AcceptDrawDialog
import app.trailblazercombi.haventide.game2.jetpack.gamescreen.dialogs.DetailedCiInfoDialog
import app.trailblazercombi.haventide.game2.jetpack.gamescreen.dialogs.EndRoundDialog
import app.trailblazercombi.haventide.game2.jetpack.gamescreen.dialogs.ForfeitConfirmationDialog
import app.trailblazercombi.haventide.game2.jetpack.gamescreen.dialogs.GameOverDialog
import app.trailblazercombi.haventide.game2.jetpack.gamescreen.dialogs.OfferDrawDialog
import app.trailblazercombi.haventide.game2.jetpack.gamescreen.dialogs.PauseMenuDialog
import app.trailblazercombi.haventide.game2.jetpack.gamescreen.dialogs.StartRoundDialog
import app.trailblazercombi.haventide.game2.jetpack.gamescreen.panels.BottomPanel
import app.trailblazercombi.haventide.game2.jetpack.gamescreen.panels.CiPanel
import app.trailblazercombi.haventide.game2.jetpack.gamescreen.panels.DiceInfoPanel
import app.trailblazercombi.haventide.game2.jetpack.gamescreen.tilemap.TileMap
import app.trailblazercombi.haventide.game2.jetpack.gamescreen.panels.GameStatusPanel
import app.trailblazercombi.haventide.game2.jetpack.universal.YesNoDialog
import app.trailblazercombi.haventide.game2.viewModel.GameLoopViewModel
import app.trailblazercombi.haventide.netcode.TcpServer
import app.trailblazercombi.haventide.netcode.startTcpServer
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

    val serverRunning by TcpServer.serverRunning.collectAsState()
    val backStackEntry by navController.currentBackStackEntryAsState()

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
    StartRoundDialog(viewModel)
    ForfeitConfirmationDialog(viewModel)
    OfferDrawDialog(viewModel)
    AcceptDrawDialog(viewModel)
    GameOverDialog(viewModel, navController)
    AbilityPickerDialog(viewModel)

    DetailedCiInfoDialog(
        openDialogState = viewModel.leftModificatorsDialog,
        hideDialog = viewModel::hideLeftModificatorsDialog,
        ciInfo = viewModel.leftCiInfo
    )
    DetailedCiInfoDialog(
        openDialogState = viewModel.rightModificatorsDialog,
        hideDialog = viewModel::hideRightModificatorsDialog,
        ciInfo = viewModel.rightCiInfo
    )

    LaunchedEffect(Unit) {
        viewModel.updateTileHighlights()
    }

    LaunchedEffect(backStackEntry) {
        if (!serverRunning) startTcpServer()
    }
}
