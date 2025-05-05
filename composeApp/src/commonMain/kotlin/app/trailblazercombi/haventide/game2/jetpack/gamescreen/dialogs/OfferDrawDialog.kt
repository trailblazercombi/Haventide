package app.trailblazercombi.haventide.game2.jetpack.gamescreen.dialogs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import app.trailblazercombi.haventide.game2.jetpack.gamescreen.components.misc.TapToDismissLabel
import app.trailblazercombi.haventide.game2.jetpack.universal.DialogGenerics
import app.trailblazercombi.haventide.game2.viewModel.GameLoopViewModel
import app.trailblazercombi.haventide.netcode.TcpClient
import app.trailblazercombi.haventide.resources.GameScreenDialogBoxStyle
import app.trailblazercombi.haventide.resources.Palette
import app.trailblazercombi.haventide.resources.Res
import app.trailblazercombi.haventide.resources.offer_draw_dialog_offered
import app.trailblazercombi.haventide.resources.offer_draw_dialog_refused
import app.trailblazercombi.haventide.resources.offer_draw_dialog_accepted
import org.jetbrains.compose.resources.stringResource

@Composable
fun OfferDrawDialog(viewModel: GameLoopViewModel, modifier: Modifier = Modifier) {

    val openDialog by viewModel.offerDrawDialog.collectAsState()
    val drawOffered by viewModel.drawOffered.collectAsState()
    val drawRefused by viewModel.drawRefused.collectAsState()

    DialogGenerics(
        openDialogState = viewModel.offerDrawDialog,
        onDismissRequest = {
            if (drawRefused) {
                viewModel.hideOfferDrawDialog()
                // Reset the variables...
                viewModel.drawOffered.value = false
                viewModel.drawRefused.value = false
            }
        },
    )

    LaunchedEffect(openDialog) {
        if (!openDialog) return@LaunchedEffect
        // Upon the dialog's opening, a Draw shall be offered to the opponent...
        viewModel.drawOffered.value = true
        TcpClient.sendToRemoteServer("GEEMU_DOROWU")
    }

    AnimatedVisibility(visible = openDialog, enter = scaleIn(), exit = scaleOut()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(
                    if (drawOffered && !drawRefused) Res.string.offer_draw_dialog_offered
                    else if (drawRefused) Res.string.offer_draw_dialog_refused
                    else Res.string.offer_draw_dialog_accepted
                ),
                color = Palette.FullWhite,
                fontSize = GameScreenDialogBoxStyle.LargeTextSize,
                textAlign = TextAlign.Center,
                lineHeight = GameScreenDialogBoxStyle.LargeTextSize,
            )
        }
    }

    if (drawRefused) {
        TapToDismissLabel(viewModel.offerDrawDialog)
    }
}
