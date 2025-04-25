package app.trailblazercombi.haventide.game2.jetpack.gamescreen.tilemap

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import app.trailblazercombi.haventide.game2.data.tilemap.TileData
import app.trailblazercombi.haventide.resources.TileStyle.TileSize

/**
 * The [tile][TileData]'s current [Mechanism Stack][TileData.mechanismStack].
 * @param mechanisms The set of Mechanisms to be rendered.
 */
@Composable
fun MechanismStack(mechanismProvider: TileData, modifier: Modifier = Modifier) {

    val mechanisms by mechanismProvider.mechanismStackStack.collectAsState()

    Box (
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(TileSize)
    ) {
        mechanisms.forEach {
            ComposableMechanism(it, Modifier.align(Alignment.Center))
        }
    }
}
