package app.trailblazercombi.haventide.game2.jetpack.gamescreen.tilemap

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.Mechanism
import app.trailblazercombi.haventide.resources.TileStyle.TileSize

/**
 * The [tile][TileData]'s current [Mechanism Stack][TileData.mechanismStack].
 * @param mechanisms The set of Mechanisms to be rendered.
 */
@Composable
fun MechanismStack(mechanisms: Set<Mechanism>, modifier: Modifier = Modifier) {
    Box (
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(TileSize)
    ) {
        mechanisms.forEach {
            ComposableMechanism(it, Modifier.align(Alignment.Center))
        }
    }
    // [LATER...] TODO Smarter approach, because this will look so stupid
}
