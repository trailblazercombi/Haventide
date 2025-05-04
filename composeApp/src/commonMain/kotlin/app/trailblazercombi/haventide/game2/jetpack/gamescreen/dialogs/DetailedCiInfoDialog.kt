package app.trailblazercombi.haventide.game2.jetpack.gamescreen.dialogs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import app.trailblazercombi.haventide.game2.data.tilemap.TileViewInfo
import app.trailblazercombi.haventide.game2.jetpack.gamescreen.components.cipanel.CiModificatorView
import app.trailblazercombi.haventide.game2.jetpack.gamescreen.components.misc.TapToDismissLabel
import app.trailblazercombi.haventide.game2.jetpack.gamescreen.panels.CiPanel
import app.trailblazercombi.haventide.game2.jetpack.universal.DialogGenerics
import app.trailblazercombi.haventide.resources.CiStyle
import app.trailblazercombi.haventide.resources.GameScreenDialogBoxStyle
import app.trailblazercombi.haventide.resources.Palette
import app.trailblazercombi.haventide.resources.Res
import app.trailblazercombi.haventide.resources.yay_no_modificators
import kotlinx.coroutines.flow.MutableStateFlow
import org.jetbrains.compose.resources.stringResource

@Composable
fun DetailedCiInfoDialog(
    openDialogState: MutableStateFlow<Boolean>,
    hideDialog: () -> Unit,
    ciInfo: MutableStateFlow<TileViewInfo?>,
    modifier: Modifier = Modifier
) {
    val openDialog by openDialogState.collectAsState()
    val ciInfo by ciInfo.collectAsState()

    val scrollState = rememberScrollState()

    DialogGenerics(
        openDialogState = openDialogState,
        onDismissRequest = hideDialog,
        modifier = modifier
    )

    AnimatedVisibility(
        visible = openDialog,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
    ) {
        if (ciInfo is TileViewInfo.Ally) {
            Box(contentAlignment = Alignment.Center, modifier = modifier.fillMaxSize()) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = modifier.verticalScroll(scrollState)
                ) {
                    if ((ciInfo as TileViewInfo.Ally).phoenix.modificators.isEmpty()) {
                        Text(
                            text = stringResource(Res.string.yay_no_modificators),
                            fontSize = CiStyle.TitleSize,
                            lineHeight = CiStyle.TitleSize,
                            textAlign = TextAlign.Center,
                            color = Palette.FullWhite,
                            modifier = modifier.padding(CiStyle.InnerPadding)
                        )
                    }
                    (ciInfo as TileViewInfo.Ally).phoenix.modificators.forEach {
                        CiModificatorView(it)
                    }
                }
            }
        }
    }

    TapToDismissLabel(openDialogState, modifier)
}
