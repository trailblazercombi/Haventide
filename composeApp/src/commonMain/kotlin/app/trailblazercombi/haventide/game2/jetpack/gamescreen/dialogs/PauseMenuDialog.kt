package app.trailblazercombi.haventide.game2.jetpack.gamescreen.dialogs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import app.trailblazercombi.haventide.game2.jetpack.universal.DialogGenerics
import app.trailblazercombi.haventide.game2.jetpack.universal.MenuButton
import app.trailblazercombi.haventide.game2.viewModel.GameLoopViewModel
import app.trailblazercombi.haventide.resources.*
import app.trailblazercombi.haventide.resources.GameScreenTopBubbleStyle.StandardButtonWidth

@Composable
fun PauseMenuDialog(viewModel: GameLoopViewModel, modifier: Modifier = Modifier) {
    val openDialog by viewModel.pauseMenuDialog.collectAsState()

    DialogGenerics(
        openDialogState = viewModel.pauseMenuDialog,
        onDismissRequest = { viewModel.pauseMenuDialog.value = false },
        modifier = modifier
    )

    AnimatedVisibility(openDialog, enter = slideInVertically(), exit = slideOutVertically()) {
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Surface(
                color = Palette.Glass00,
                shape = RoundedCornerShape(GameScreenDialogBoxStyle.OuterCornerRounding),
                contentColor = Palette.FullWhite,
//                border = BorderStroke(
//                    GameScreenDialogBoxStyle.OutlineThickness,
//                    Palette.Abyss10.compositeOver(viewModel.backdropColor.value),
//                ),
                elevation = GameScreenDialogBoxStyle.Elevation,
                modifier = modifier.padding(
                    top = GameScreenTopBubbleStyle.BubbleHeight + GameScreenTopBubbleStyle.OffsetFromEdge * 2
                )
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = modifier
                ) {
                    Column(
                        modifier = modifier
                            .width(StandardButtonWidth)
                            .padding(GameScreenDialogBoxStyle.InnerPadding),
                    ) {
                        /* RETURN   */ MenuButton({ viewModel.hidePauseMenuDialog() },
                        Res.string.pause_dialog_return_button, ButtonSeverity.NEUTRAL)
                        /* DRAW     */ MenuButton({ viewModel.showOfferDrawDialog() },
                        Res.string.pause_dialog_offer_draw_button, ButtonSeverity.NEUTRAL)
                        /* FORFEIT  */ MenuButton(onClick = { viewModel.showForfeitConfirmationDialog() },
                        Res.string.pause_dialog_forfeit_button, ButtonSeverity.DESTRUCTIVE_MINOR)
                    }
                }
            }
        }
    }
}
