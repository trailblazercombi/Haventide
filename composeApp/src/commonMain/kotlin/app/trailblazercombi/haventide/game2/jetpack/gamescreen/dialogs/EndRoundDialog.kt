package app.trailblazercombi.haventide.game2.jetpack.gamescreen.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import app.trailblazercombi.haventide.game2.jetpack.universal.YesNoDialog
import app.trailblazercombi.haventide.game2.viewModel.GameLoopViewModel
import app.trailblazercombi.haventide.resources.*

@Composable
fun EndRoundDialog(viewModel: GameLoopViewModel, modifier: Modifier = Modifier) {

    if (!viewModel.playerHasDiceLeft()) {
        viewModel.hideEndRoundDialog()
        viewModel.processEndRoundEvent()
        return
    }

    YesNoDialog(
        screenSizeProvider = viewModel,
        openDialogState = viewModel.endRoundDialog,
        title = Res.string.end_round_dialog_title,
        acceptLabel = Res.string.end_round_dialog_confirm_button,
        declineLabel = Res.string.end_round_dialog_cancel_button,
        onAccept = {
            viewModel.hideEndRoundDialog()
            viewModel.processEndRoundEvent()
                   },
        onDecline = { viewModel.hideEndRoundDialog() },
        acceptSeverity = ButtonSeverity.PREFERRED,
        declineSeverity = ButtonSeverity.NEUTRAL,
        modifier = modifier
    )
}
