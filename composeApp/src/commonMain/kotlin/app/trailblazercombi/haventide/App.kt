package app.trailblazercombi.haventide

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import app.trailblazercombi.haventide.game.arena.ComposableTileMap
import app.trailblazercombi.haventide.game.arena.PlaceholderPlayers
import org.jetbrains.compose.ui.tooling.preview.Preview
import app.trailblazercombi.haventide.game.arena.TileMapData

@Composable
@Preview
fun App() {
    MaterialTheme {
        ComposableTileMap(
            TileMapData(
            PlaceholderPlayers.PLAYER_ONE.toProfile(),
            PlaceholderPlayers.PLAYER_TWO.toProfile()
        )
        )
    }
}
