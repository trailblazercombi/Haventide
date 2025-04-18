package app.trailblazercombi.haventide.game2.jetpack.gamescreen.panels

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.trailblazercombi.haventide.game.abilities.StackOfDiceInGame
import app.trailblazercombi.haventide.game.arena.GameLoopViewModel
import app.trailblazercombi.haventide.game2.jetpack.gamescreen.panels.components.DiceCounter
import app.trailblazercombi.haventide.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun DiceInfoPanel(viewModel: GameLoopViewModel, modifier: Modifier = Modifier) {
    val backdropColor by viewModel.backdropColor.collectAsState()
    val screenWidth by viewModel.screenWidth.collectAsState()

    Box (
        contentAlignment = Alignment.BottomCenter,
        modifier = modifier.fillMaxSize()
    ) {
        Surface(
            color = GameScreenTopBubbleStyle.FillColorModifier.compositeOver(backdropColor),
            border = BorderStroke(
                GameScreenTopBubbleStyle.OutlineThickness,
                GameScreenTopBubbleStyle.OutlineColorModifier.compositeOver(backdropColor)
            ),
            modifier = modifier,
        ) {
            Box (
                contentAlignment = Alignment.BottomCenter,
                modifier = modifier
                    .padding(DieStyle.PanelPadding)
                    .fillMaxWidth()
                    .height(
                        DieStyle.PanelPadding * 2
                            + if (screenWidth > ScreenSizeThresholds.SpreadDiceStackOnSingleLine)
                        DieStyle.DieSize else DieStyle.DieSize * 2
                    )
            ) {
                Row (modifier = modifier.matchParentSize(), verticalAlignment = Alignment.CenterVertically) {
                    StackOfDiceInGame(viewModel, viewModel.localPlayerDice.value.viewModel)
                    DiceCounter(viewModel, viewModel.localPlayerDice.value.viewModel)
                }
                Box (modifier = modifier.matchParentSize(), contentAlignment = Alignment.CenterEnd) {
                    Button(
                        onClick = { viewModel.endRoundDialog.value = true },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = DieStyle.EndRoundButtonSeverity.fillColor,
                            contentColor = DieStyle.EndRoundButtonSeverity.contentColor,
                        ),
                        border = BorderStroke(
                            DieStyle.OutlineThickness,
                            DieStyle.EndRoundButtonSeverity.outlineColor
                        ),
                        modifier = modifier
                            .padding(DieStyle.Separation)
                            .height(
                                DieStyle.DieSize *
                                    if (screenWidth > ScreenSizeThresholds.SpreadDiceStackOnSingleLine) 1 else 2)
                            .width(
                                DieStyle.DieSize *
                                    if (screenWidth > ScreenSizeThresholds.SpreadDiceStackOnSingleLine) 4 else 3)
                    ) {
                        Text(
                            text = stringResource(Res.string.game_button_end_round),
                            textAlign = TextAlign.Center,
                            fontSize = GameScreenDialogBoxStyle.ButtonTextSize,
                            modifier = modifier.fillMaxWidth().padding(0.dp)
                        )
                    }
                }
            }
        }
    }
}
