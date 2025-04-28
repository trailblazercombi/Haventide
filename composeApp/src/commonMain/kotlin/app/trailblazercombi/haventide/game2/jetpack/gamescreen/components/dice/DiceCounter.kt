package app.trailblazercombi.haventide.game2.jetpack.gamescreen.components.dice

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.trailblazercombi.haventide.game2.data.turntable.countedDiceMatch
import app.trailblazercombi.haventide.game2.viewModel.GameLoopViewModel
import app.trailblazercombi.haventide.resources.*
import org.jetbrains.compose.resources.stringResource
import kotlin.math.min
import kotlin.math.max

@Suppress("DuplicatedCode")
@Composable
fun DiceCounter(viewModel: GameLoopViewModel, modifier: Modifier = Modifier) {
    val screenWidth by viewModel.screenWidth.collectAsState()
    val abilityPreview by viewModel.abilityPreview.collectAsState()

    val alignedSelected: Int by viewModel.alignedSelectedDiceCount.collectAsState()
    val scatteredSelected: Int by viewModel.scatteredSelectedDiceCount.collectAsState()

    val alignedNeeded: Int = abilityPreview?.alignedCost() ?: 0
    val scatteredNeeded: Int = abilityPreview?.scatteredCost() ?: 0

    val okDiceSelection = countedDiceMatch(
        actual = alignedSelected to scatteredSelected,
        required = alignedNeeded to scatteredNeeded
    )

    OutlinedButton(
        onClick = { viewModel.autoSelectDice() },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = DieStyle.CounterButtonSeverity.fillColor,
            contentColor = if (okDiceSelection) DieStyle.CounterButtonSeverity.contentColor else Palette.FillRed
        ),
        border = BorderStroke(
            DieStyle.OutlineThickness, DieStyle.CounterButtonSeverity.outlineColor
        ),
        contentPadding = PaddingValues(0.dp),
        modifier = modifier
            .padding(DieStyle.Separation)
            .height(DieStyle.DieSize * if (screenWidth > ScreenSizeThresholds.SpreadDiceStackOnSingleLine) 1 else 2)
            .width(DieStyle.DieSize * if (screenWidth > ScreenSizeThresholds.SpreadDiceStackOnSingleLine) 2 else 1)
    ) {
        if (screenWidth > ScreenSizeThresholds.SpreadDiceStackOnSingleLine) {
            Text(
                text = stringResource(
                    Res.string.game_button_dice_counter_wide,
                    min(alignedSelected, alignedNeeded), alignedNeeded,
                    max(alignedSelected - alignedNeeded, 0) + scatteredSelected, scatteredNeeded
                ),
                fontSize = DiceCounterStyle.HorizontalTextSize,
                textAlign = TextAlign.Center,
                lineHeight = DiceCounterStyle.HorizontalTextSize,
                maxLines = 1,
                modifier = modifier.fillMaxWidth()
            )
        } else {
            Column {
                Text(
                    text = stringResource(
                        Res.string.game_button_dice_counter_narrow1,
                        min(alignedSelected, alignedNeeded), alignedNeeded
                    ),
                    fontSize = DiceCounterStyle.VerticalTextSize,
                    textAlign = TextAlign.Center,
                    lineHeight = DiceCounterStyle.VerticalTextSize,
                    maxLines = 1,
                    modifier = modifier.fillMaxWidth()
                )
                Spacer(modifier.height(DiceCounterStyle.VerticalSeparation))
                Text(
                    text = stringResource(
                        Res.string.game_button_dice_counter_narrow2,
                        max(alignedSelected - alignedNeeded, 0) + scatteredSelected, scatteredNeeded
                    ),
                    fontSize = DiceCounterStyle.VerticalTextSize,
                    textAlign = TextAlign.Center,
                    lineHeight = DiceCounterStyle.VerticalTextSize,
                    maxLines = 1,
                    modifier = modifier.fillMaxWidth()
                )
            }
        }
    }
}
