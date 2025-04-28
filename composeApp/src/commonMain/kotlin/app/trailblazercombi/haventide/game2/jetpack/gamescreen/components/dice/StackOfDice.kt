package app.trailblazercombi.haventide.game2.jetpack.gamescreen.components.dice

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import app.trailblazercombi.haventide.game2.data.turntable.Die
import app.trailblazercombi.haventide.game2.viewModel.GameLoopViewModel
import app.trailblazercombi.haventide.resources.DieStyle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.math.ceil

@Composable
fun StackOfDice(
    viewModel: GameLoopViewModel,
    diceStack: MutableStateFlow<List<Die>>,
    dicePerRow: Int = 4,
    onDieClicked: (Die) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val diceList by diceStack.collectAsState()
    val rows = ceil((diceList.size.toDouble() / dicePerRow)).toInt()
    val finalRow = diceList.size % dicePerRow

    Column {
        Row {
            repeat(finalRow) {
                val die = diceList[it]
                ComposableDie(
                    viewModel = viewModel,
                    die = die,
                    onClick = { onDieClicked(die) },
                    modifier = modifier.padding(DieStyle.Separation / 2)
                )
            }
        }
        repeat(if (finalRow == 0) rows else rows - 1) { row ->
            Row {
                repeat(dicePerRow) { column ->
                    val die = diceList[finalRow + (dicePerRow * row + column)]
                    ComposableDie(
                        viewModel = viewModel,
                        die = die,
                        onClick = { onDieClicked(die) },
                        modifier = modifier.padding(DieStyle.Separation / 2)
                    )
                }
            }
        }
    }
}
