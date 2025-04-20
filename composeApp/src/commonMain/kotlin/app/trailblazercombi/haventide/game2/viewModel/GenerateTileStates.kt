package app.trailblazercombi.haventide.game2.viewModel

import app.trailblazercombi.haventide.game2.data.GameLoop
import app.trailblazercombi.haventide.game2.data.tilemap.Position
import app.trailblazercombi.haventide.game2.data.tilemap.TileData

/**
 * A supporting function that generates tileStates from the GameLoop for the viewModel.
 * It expects the TileMap to be fully initialized.
 */
fun generateTileStates(gameLoop: GameLoop): TileStateMap {
    val tiles: Map<Position, TileData?> = gameLoop.getAllTiles()
    val result = MutableTileStateMap()
    tiles.values.forEach { result.put(it) }
    return result.toTileStateMap()
}
