package app.trailblazercombi.haventide.gameScreen

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

val tileSize = 128.dp

class TileMap {
    val rows = 16
    val columns = 16
}

class TileData {

}

@Composable
fun ScrollableTileMap(
    mapData: TileMap,
    modifier: Modifier = Modifier
) {
    // Keep states of ONE currently selected tile (I'll need two) and the scroll state
    var selectedTile1 by remember { mutableStateOf<TileData?>(null) }
    var selectedTile2 by remember { mutableStateOf<TileData?>(null) }
    val scrollStateX = rememberScrollState()
    val scrollStateY = rememberScrollState()

    // Nested Rows in a Column.
    Box(
        modifier = modifier.horizontalScroll(scrollStateX, true).verticalScroll(scrollStateY, true)
    ) {
        Column {
            for (y in 0 until mapData.rows) {
                Row {
                    for (x in 0 until mapData.columns) {
                        // Starting from this point, we have tile coordinates.
                        Tile(null)
                    }
                }
            }
        }
    }
}

@Composable
fun Tile(tileData: TileData? = null, modifier: Modifier = Modifier) {
    // if (tileData != null) {
    if (false) {
        // Filled Tile here
    } else {
        // Spacer(modifier.width(tileSize).height(tileSize))
        Box(
            modifier = modifier
                .width(tileSize)
                .height(tileSize)
                .clickable {
                    // on-click goes here and needs to be propagated UPWARDS
                },
            contentAlignment = Alignment.Center,
        ) {
            Text("Hello!")
        }
    }
}