package app.trailblazercombi.haventide

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import app.trailblazercombi.haventide.game.ComposableTileMap
import app.trailblazercombi.haventide.game.Position
import org.jetbrains.compose.ui.tooling.preview.Preview
import app.trailblazercombi.haventide.game.TileMapData

@Composable
@Preview
fun App() {
    MaterialTheme {
        ComposableTileMap(TileMapData())
        // ComposablePreviewArrow(Position(1,4), Position(4, 1))
    }
}