package app.trailblazercombi.haventide.game2.jetpack.gamescreen.tilemap

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import app.trailblazercombi.haventide.game.arena.TileMapData

/**
 * This is the UI layer of [TileMapData].
 * @param mapData The [TileMapData] to be rendered.
 */
@Composable
fun TileMap(mapData: TileMapData, modifier: Modifier = Modifier) {
    // Keep the states here
    val scrollStateX = rememberScrollState()
    val scrollStateY = rememberScrollState()

    // The TileMap's background.
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .background(mapData.backdropColor)
            .scrolling(scrollStateX, scrollStateY)
    ) {
        Column {
            for (y in 0 until mapData.rows) {
                Row {
                    for (x in 0 until mapData.columns) {
                        // Starting from this point, we have tile coordinates.
                        Tile(mapData[x, y])
                    }
                }
            }
        }
    }
}
