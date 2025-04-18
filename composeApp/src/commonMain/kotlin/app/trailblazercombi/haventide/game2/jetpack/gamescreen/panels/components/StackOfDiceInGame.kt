package app.trailblazercombi.haventide.game2.jetpack.gamescreen.panels.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.trailblazercombi.haventide.game.abilities.DiceStackViewModel
import app.trailblazercombi.haventide.game.abilities.Die
import app.trailblazercombi.haventide.game2.viewModel.GameLoopViewModel
import app.trailblazercombi.haventide.resources.ScreenSizeThresholds

@Composable
fun StackOfDiceInGame(
    loopViewModel: GameLoopViewModel,
    stackViewModel: DiceStackViewModel,
    modifier: Modifier = Modifier
) {
    val screenWidth by loopViewModel.screenWidth.collectAsState()
    val dicePerRow = if (screenWidth > ScreenSizeThresholds.SpreadDiceStackOnSingleLine) 8 else 4

    StackOfDice(
        diceStack = stackViewModel.diceStackAsState,
        dicePerRow = dicePerRow,
        onDieClicked = { die: Die -> stackViewModel.processClick(die) },
        modifier = modifier
    )
}
