package app.trailblazercombi.haventide.game2.jetpack.gamescreen.tilemap

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.trailblazercombi.haventide.game.mechanisms.Mechanism
import app.trailblazercombi.haventide.game.mechanisms.PhoenixMechanism

@Composable
fun ComposableMechanism(mechanism: Mechanism, modifier: Modifier = Modifier) {
    if (mechanism is PhoenixMechanism) PhoenixOnBoard(mechanism)
}
