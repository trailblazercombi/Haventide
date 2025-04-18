package app.trailblazercombi.haventide.game2.data.tilemap

import androidx.compose.ui.graphics.Color
import app.trailblazercombi.haventide.game.arena.*
import app.trailblazercombi.haventide.game2.viewModel.TileMapViewModel

/**
 * This is the map data class in all its glory.
 */
class TileMapData(private val turnTable: TurnTable, val gameLoop: GameLoop) {

    // [LOAD FROM FILE] TODO Size does not need to be a property,
    //                   and also, read this from file, including the backdrop color.
    private val columns = 10; private val rows = 10
    val backdropColor = Color(0xFF381428)

    private val tiles: MutableMap<Position, TileData?> = mutableMapOf()

    val viewModel = TileMapViewModel(this)

    /* NOTE: Secondary always takes precedence.
         * Reason: when the primary is selected (clicked on a Phoenix),
         * selecting the secondary will always yield a move preview. If the move's target is an ally,
         * previewing a move targeting an ally is always more desirable than selecting another ally.
         * That is, of course, if the ally is targetable (within range and the Phoenix has an ability for that).
         */
    // [GAME LOOP] FIXME I think this is the case already, test it and see

    private fun currentPlayer(): PlayerInGame {
        return turnTable.currentPlayer()
    }

    fun localPlayer(): PlayerInGame {
        return gameLoop.localPlayer()
    }

    /**
     * Get [a tile][TileData] from the map definition.
     * @param x The x-coordinate of the [desired tile][TileData].
     * @param y The y-coordiante of the [desired tile][TileData].
     * @return The [desired tile][TileData] if it exists at that [position][Position],
     * or `null` if the requested [position][Position] yielded no [tile][TileData].
     */
    operator fun get(x: Int, y: Int): TileData? {
        return tiles[Position(x, y)]
    }

    /**
     * Get [a tile][TileData] from the map definition.
     * @param position The [position][Position] of the [desired tile][TileData].
     * @return The [desired tile][TileData] if it exists at that [position][Position],
     * or `null` if the requested [position][Position] yielded no [tile][TileData].
     */
    operator fun get(position: Position): TileData? {
        return tiles[position]
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // THE FORSAKEN INIT BLOCK
    // Moved here because NullPointerException when trying to call fields below it...
    // Ghhhhhhh...
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    init {
        // [LOAD FROM FILE] TODO Read from a file instead
        for (y in 0 until rows) {
            for (x in 0 until columns) {
                val position = Position(x, y)
//                if (x % 2 == 0 && y % 4 == 0) tiles[position] = null
                tiles[position] = TileData(this, position)
            }
        }
        var i = 0 // [MAPS] FIXME this is so janky
        turnTable.allPlayers().forEach {
            it.addRoster(get(3, 3 * i)!!, get(4, 3 * i)!!, get(5, 3 * i)!!)
            i++
        }
        viewModel.updateAvailableTiles()
        // [MAPS] FIXME Make sure there is at least one row / column of empty tiles on every edge...
    }
}
