package app.trailblazercombi.haventide.game2.jetpack.gamescreen.dialogs

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import app.trailblazercombi.haventide.game2.jetpack.universal.YesNoDialog
import app.trailblazercombi.haventide.game2.viewModel.GameLoopViewModel
import app.trailblazercombi.haventide.netcode.TcpClient
import app.trailblazercombi.haventide.resources.ButtonSeverity
import app.trailblazercombi.haventide.resources.Res
import app.trailblazercombi.haventide.resources.accept_draw_dialog_no
import app.trailblazercombi.haventide.resources.accept_draw_dialog_title
import app.trailblazercombi.haventide.resources.accept_draw_dialog_yes

@Composable
fun AcceptDrawDialog(viewModel: GameLoopViewModel, modifier: Modifier = Modifier) {
    YesNoDialog(
        screenSizeProvider = viewModel,
        openDialogState = viewModel.acceptDrawDialog,
        title = Res.string.accept_draw_dialog_title,
        acceptLabel = Res.string.accept_draw_dialog_yes,
        declineLabel = Res.string.accept_draw_dialog_no,
        onAccept = {
            TcpClient.sendToRemoteServer("DOROWU_OKE")
            viewModel.hideAcceptDrawDialog()
            viewModel.declareDraw()
        },
        onDecline = {
            TcpClient.sendToRemoteServer("DOROWU_NAI")
            viewModel.hideAcceptDrawDialog()
        },
        onDismissRequest = {
            TcpClient.sendToRemoteServer("DOROWU_NAI")
            viewModel.hideAcceptDrawDialog()
        },
        acceptSeverity = ButtonSeverity.NEUTRAL,
        declineSeverity = ButtonSeverity.NEUTRAL,
        modifier = modifier
    )
}
