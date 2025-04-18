package app.trailblazercombi.haventide.game2.jetpack.gamescreen.dialogs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import app.trailblazercombi.haventide.game.abilities.StackOfDice
import app.trailblazercombi.haventide.game.arena.GameLoopViewModel
import app.trailblazercombi.haventide.game2.jetpack.universal.DialogGenerics

@Composable
fun StartRoundDialog(viewModel: GameLoopViewModel, modifier: Modifier = Modifier) {
    val dialogOpen by viewModel.startRoundDialog.collectAsState()
    val renderDice by viewModel.localPlayerDice.collectAsState()

    DialogGenerics(viewModel.startRoundDialog, { viewModel.startRoundDialog.value = false })

    Box (modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        AnimatedVisibility(dialogOpen, enter = scaleIn(), exit = scaleOut()) {
            Column {
                StackOfDice(renderDice.freshListState())
            }
        }
    }
}
