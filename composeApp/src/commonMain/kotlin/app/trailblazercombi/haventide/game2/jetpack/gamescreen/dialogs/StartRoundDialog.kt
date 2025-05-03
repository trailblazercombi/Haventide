package app.trailblazercombi.haventide.game2.jetpack.gamescreen.dialogs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key.Companion.R
import androidx.compose.ui.text.style.TextAlign
import app.trailblazercombi.haventide.game2.data.turntable.DiceStack
import app.trailblazercombi.haventide.game2.jetpack.gamescreen.components.dice.StackOfDice
import app.trailblazercombi.haventide.game2.jetpack.gamescreen.components.misc.TapToDismissLabel
import app.trailblazercombi.haventide.game2.jetpack.universal.DialogGenerics
import app.trailblazercombi.haventide.game2.viewModel.GameLoopViewModel
import app.trailblazercombi.haventide.resources.CiStyle
import app.trailblazercombi.haventide.resources.GameScreenDialogBoxStyle
import app.trailblazercombi.haventide.resources.Palette
import app.trailblazercombi.haventide.resources.Res
import app.trailblazercombi.haventide.resources.game_turn_state_round_start
import app.trailblazercombi.haventide.resources.game_turn_state_round_start_dice
import org.jetbrains.compose.resources.stringResource
import javax.swing.JToolBar

@Composable
fun StartRoundDialog(viewModel: GameLoopViewModel, modifier: Modifier = Modifier) {
    val openDialog by viewModel.startRoundDialog.collectAsState()
    val roundNumber by viewModel.roundNumber.collectAsState()

    DialogGenerics(
        openDialogState = viewModel.startRoundDialog,
        onDismissRequest = viewModel::hideStartRoundDialog,
        modifier = modifier
    )

    AnimatedVisibility(visible = openDialog, enter = scaleIn(), exit = scaleOut()) {
        Box(contentAlignment = Alignment.Center, modifier = modifier.fillMaxSize()) {
            Column(verticalArrangement = Arrangement.Center) {
                Text(
                    text = stringResource(Res.string.game_turn_state_round_start, roundNumber),
                    textAlign = TextAlign.Center,
                    color = Palette.FullWhite,
                    fontSize = GameScreenDialogBoxStyle.LargeTextSize,
                    lineHeight = GameScreenDialogBoxStyle.LargeTextSize,
                    modifier = modifier.fillMaxWidth()
                )
                Spacer(modifier = modifier.height(GameScreenDialogBoxStyle.InnerPadding))
                Text(
                    text = stringResource(Res.string.game_turn_state_round_start_dice, roundNumber),
                    textAlign = TextAlign.Center,
                    color = Palette.FullWhite,
                    fontSize = GameScreenDialogBoxStyle.ButtonTextSize,
                    lineHeight = GameScreenDialogBoxStyle.ButtonTextSize,
                    modifier = modifier.fillMaxWidth()
                )
                Spacer(modifier = modifier.height(GameScreenDialogBoxStyle.InnerPadding))
                Box (contentAlignment = Alignment.Center, modifier = modifier.fillMaxWidth()) {
                    StackOfDice(
                        viewModel = viewModel,
                        diceStack = viewModel.localDiceList,
                        dicePerRow = 4,
                        onDieClicked = {},
                    )
                }
            }
        }
    }

    TapToDismissLabel(viewModel.startRoundDialog)
}
