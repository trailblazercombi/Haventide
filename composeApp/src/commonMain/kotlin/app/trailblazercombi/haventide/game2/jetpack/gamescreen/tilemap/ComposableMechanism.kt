package app.trailblazercombi.haventide.game2.jetpack.gamescreen.tilemap

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.Mechanism
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.PhoenixMechanism
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.effecters.aoe.AoEEffecter

@Composable
fun ComposableMechanism(mechanism: Mechanism, modifier: Modifier = Modifier) {
    if (mechanism is PhoenixMechanism) PhoenixOnBoard(mechanism)
    if (mechanism is AoEEffecter) AoEEffecterOnBoard(mechanism)
}
