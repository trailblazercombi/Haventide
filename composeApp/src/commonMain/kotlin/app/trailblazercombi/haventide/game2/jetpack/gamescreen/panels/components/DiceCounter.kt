package app.trailblazercombi.haventide.game2.jetpack.gamescreen.panels.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.trailblazercombi.haventide.game.abilities.DiceStackViewModel
import app.trailblazercombi.haventide.resources.*
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun DiceCounter(loopViewModel: GameLoopViewModel, stackViewModel: DiceStackViewModel, modifier: Modifier = Modifier) {
    val aligned by stackViewModel.alignedNeeded.collectAsState()
    val scattered by stackViewModel.scatteredNeeded.collectAsState()
    val type by stackViewModel.typeNeeded.collectAsState()

    if (type != null) {
        val screenWidth by loopViewModel.screenWidth.collectAsState()

        val counterWidth = if (screenWidth > ScreenSizeThresholds.SpreadDiceStackOnSingleLine)
            DieStyle.Separation + DieStyle.DieSize * 2 else DieStyle.DieSize - DieStyle.Separation
        val counterHeight = if (screenWidth > ScreenSizeThresholds.SpreadDiceStackOnSingleLine)
            DieStyle.DieSize - DieStyle.Separation else DieStyle.Separation + DieStyle.DieSize * 2

        val selected: Boolean = countedDiceMatch(
            actual = type?.let { stackViewModel.countSelectedDice(it) } ?: (0 to 0),
            required = (aligned to scattered)
        )

        Box {
            Surface(
                shape = RoundedCornerShape(DiceCounterStyle.CornerRounding),
                color = ButtonSeverity.NEUTRAL_FILLED.fillColor,
                border = BorderStroke(DieStyle.OutlineThickness, ButtonSeverity.NEUTRAL_FILLED.outlineColor),
                contentColor = if (selected) ButtonSeverity.NEUTRAL_FILLED.contentColor else Palette.FillRed,
                modifier = modifier
                    .padding(DieStyle.Separation)
                    .height(counterHeight)
                    .width(counterWidth)
                    .clickable(
                        onClick = { stackViewModel.autoSelectDice() }
                    )
            ) {
                Box (
                    contentAlignment = Alignment.Center
                ) {
                    if (screenWidth > ScreenSizeThresholds.SpreadDiceStackOnSingleLine) {
                        Text(
                            text = stringResource(
                                Res.string.game_button_dice_counter_wide,
                                aligned, scattered
                            ),
                            fontSize = DiceCounterStyle.HorizontalTextSize,
                            textAlign = TextAlign.Center,
                            lineHeight = DiceCounterStyle.HorizontalTextSize,
                            maxLines = 1,
                            modifier = modifier.padding(0.dp).fillMaxWidth()
                        )
                    } else {
                        Column {
                            Text(
                                text = stringResource(
                                    Res.string.game_button_dice_counter_narrow1,
                                    aligned
                                ),
                                fontSize = DiceCounterStyle.VerticalTextSize,
                                textAlign = TextAlign.Center,
                                lineHeight = DiceCounterStyle.VerticalTextSize,
                                maxLines = 1,
                                modifier = modifier.padding(0.dp).fillMaxWidth()
                            )
                            Spacer(modifier.height(DiceCounterStyle.VerticalSeparation))
                            Text(
                                text = stringResource(
                                    Res.string.game_button_dice_counter_narrow2,
                                    scattered
                                ),
                                fontSize = DiceCounterStyle.VerticalTextSize,
                                textAlign = TextAlign.Center,
                                lineHeight = DiceCounterStyle.VerticalTextSize,
                                maxLines = 1,
                                modifier = modifier.padding(0.dp).fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}
