package app.trailblazercombi.haventide

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import app.trailblazercombi.haventide.game.arena.*
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(launchParameters: GameLoop) {
    val gameLoop by remember { mutableStateOf(launchParameters) }

//    val diceStack = DiceStack()
//    diceStack.roll(14)
//    diceStack.viewModel.setDiePreference(DieType.MEDIC)
//    diceStack.viewModel.autoSelectDice(
//        diceStack.getDice()[0],
//        diceStack.getDice()[3],
//        diceStack.getDice()[5],
//        diceStack.getDice()[7],
//        diceStack.getDice()[8],
//    )

    MaterialTheme {
        ComposableGameScreen(gameLoop.viewModel)
//        ComposableDiceStack(diceStack.viewModel)
    }
}
