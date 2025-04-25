package app.trailblazercombi.haventide.game2.jetpack.gamescreen.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.trailblazercombi.haventide.resources.GameScreenDialogBoxStyle
import app.trailblazercombi.haventide.resources.Palette
import app.trailblazercombi.haventide.resources.Res
import app.trailblazercombi.haventide.resources.game_over_dialog_confirm_button
import kotlinx.coroutines.flow.StateFlow
import org.jetbrains.compose.resources.stringResource

@Composable
fun TapToDismissLabel(openDialog: StateFlow<Boolean>, modifier: Modifier = Modifier) {

    val openDialog by openDialog.collectAsState()

    AnimatedVisibility(openDialog, enter = fadeIn(), exit = fadeOut()) {
        Box (modifier.fillMaxSize()) {
            Text(
                text = stringResource(resource = Res.string.game_over_dialog_confirm_button),
                textAlign = TextAlign.Center,
                color = Palette.FullGrey,
                modifier = modifier
                    .align(Alignment.BottomCenter)
                    .padding(0.dp, GameScreenDialogBoxStyle.TapAnywhereLabelOffset)
            )
        }
    }
}
