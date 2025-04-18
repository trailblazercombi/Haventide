package app.trailblazercombi.haventide.game2.jetpack.gamescreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import app.trailblazercombi.haventide.game.arena.GameLoopViewModel
import app.trailblazercombi.haventide.game2.jetpack.universal.ScreenSizeFinder
import app.trailblazercombi.haventide.resources.*
import app.trailblazercombi.haventide.game2.jetpack.gamescreen.tilemap.TileMap
import app.trailblazercombi.haventide.game2.jetpack.gamescreen.dialogs.GameOverDialog
import app.trailblazercombi.haventide.game2.jetpack.gamescreen.dialogs.EndRoundDialog
import app.trailblazercombi.haventide.game2.jetpack.gamescreen.dialogs.PauseMenuDialog
import app.trailblazercombi.haventide.game2.jetpack.gamescreen.dialogs.StartRoundDialog
import app.trailblazercombi.haventide.game2.jetpack.universal.YesNoDialog
import app.trailblazercombi.haventide.game2.jetpack.gamescreen.panels.DiceInfoPanel
import app.trailblazercombi.haventide.game2.jetpack.gamescreen.panels.GameStatusPanel

@Composable
fun ComposableGameScreen(viewModel: GameLoopViewModel, modifier: Modifier = Modifier) {
    val viewModelState = viewModel.gameLoopState.collectAsState()

    // Find the current screen size...
    ScreenSizeFinder(viewModel.screenWidth, viewModel.screenHeight)

    // The contents of the game screen
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        TileMap(viewModelState.value.tileMap)
        GameStatusPanel(viewModel)
        DiceInfoPanel(viewModel)
    }

    // The dialogs
    PauseMenuDialog(viewModel)
    EndRoundDialog(viewModel)
    StartRoundDialog(viewModel)
    YesNoDialog(
        viewModel = viewModel,
        openDialogState = viewModel.forfeitAreYouSureDialog,
        title = Res.string.yes_no_dialog_forfeit_title,
        acceptLabel = Res.string.yes_no_dialog_forfeit_yes,
        declineLabel = Res.string.yes_no_dialog_forfeit_no,
        onAccept = {
            viewModel.forfeitAreYouSureDialog.value = false
            viewModel.pauseMenuDialog.value = false
            viewModel.gameLoopState.value.forfeitMatch(viewModel.gameLoopState.value.localPlayer())
        },
        onDecline = { viewModel.forfeitAreYouSureDialog.value = false },
        acceptSeverity = ButtonSeverity.DESTRUCTIVE,
        declineSeverity = ButtonSeverity.NEUTRAL,
    )
    GameOverDialog(viewModel)

    // Once all is done, start the game - TODO Make it better
    viewModel.gameLoopState.value.localPlayer().startRound()
}
