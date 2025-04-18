package app.trailblazercombi.haventide.game2.jetpack.gamescreen.panels.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import app.trailblazercombi.haventide.game.abilities.ComposableDie
import app.trailblazercombi.haventide.game.abilities.Die
import app.trailblazercombi.haventide.resources.DieStyle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.math.ceil

@Composable
fun StackOfDice(
    diceStack: MutableStateFlow<List<Die>>,
    dicePerRow: Int = 4,
    onDieClicked: (Die) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val diceList by diceStack.collectAsState()
    val rows = ceil((diceList.size.toDouble() / dicePerRow)).toInt()
    val finalRow = diceList.size % dicePerRow

    Box (
        contentAlignment = Alignment.BottomStart,
    ) {
        Column {
            Row {
                repeat(finalRow) {
                    val die = diceList[it]
                    ComposableDie(
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
                            die = die,
                            onClick = { onDieClicked(die) },
                            modifier = modifier.padding(DieStyle.Separation / 2)
                        )
                    }
                }
            }
        }
    }
}
