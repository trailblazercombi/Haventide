package app.trailblazercombi.haventide.game2.jetpack.gamescreen.dialogs

import androidx.compose.runtime.Composable
import app.trailblazercombi.haventide.game2.jetpack.universal.YesNoDialog
import app.trailblazercombi.haventide.game2.viewModel.GameLoopViewModel
import app.trailblazercombi.haventide.resources.*

@Composable
fun ForfeitConfirmationDialog(viewModel: GameLoopViewModel) {
    YesNoDialog(
        screenSizeProvider = viewModel,
        openDialogState = viewModel.forfeitConfirmationDialog,
        title = Res.string.yes_no_dialog_forfeit_title,
        acceptLabel = Res.string.yes_no_dialog_forfeit_yes,
        declineLabel = Res.string.yes_no_dialog_forfeit_no,
        onAccept = {
            viewModel.hideForfeitConfirmationDialog()
            viewModel.hidePauseMenuDialog()
            viewModel.processForfeitMatchEvent()
        },
        onDecline = { viewModel.hideForfeitConfirmationDialog() },
        acceptSeverity = ButtonSeverity.DESTRUCTIVE,
        declineSeverity = ButtonSeverity.NEUTRAL,
    )
}
