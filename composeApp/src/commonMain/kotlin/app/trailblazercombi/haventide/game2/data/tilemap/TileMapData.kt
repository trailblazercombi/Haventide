package app.trailblazercombi.haventide.game2.data.tilemap

import androidx.compose.ui.graphics.Color
import app.trailblazercombi.haventide.game2.data.GameLoop
import app.trailblazercombi.haventide.game2.data.turntable.PlayerInGame
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.Mechanism
import app.trailblazercombi.haventide.game2.data.turntable.Team
import app.trailblazercombi.haventide.game2.data.turntable.NeutralFaction
import app.trailblazercombi.haventide.resources.MechanismTemplate

/**
 * This is the map data class in all its glory.
 *
 * It deals with the data of individual Tiles... and that's pretty much it.
 * It __does not__ deal with [Mechanism] or the actions or abilities
 * -> [Mechanism] and [TileData] talk that out between themselves.
 */
class TileMapData(val gameLoop: GameLoop) {
    /**
     * Check if the TileMap was already initialized.
     * @return `true` if [initialize] was called on `this` previously.
     */
    @Suppress("MemberVisibilityCanBePrivate")
    var isInitialized = false
        private set

    // [LOAD FROM FILE] TODO Size does not need to be a property,
    //                   and also, read this from file, including the backdrop color.
    val columns = 10; val rows = 10

    @Suppress("JoinDeclarationAndAssignment")
    val backdropColor: Color
    private val tiles: Map<Position, TileData?>

    init {
        // [LOAD FROM FILE] TODO Read from a file instead
        //                   and make sure there is at least one tile from the edge left blank...
        backdropColor = Color(0xFF381428)

        val tempTile = mutableMapOf<Position, TileData?>()
        for (y in 0 until rows) {
            for (x in 0 until columns) {
                val position = Position(x, y)
                tempTile[position] = TileData(this, position)
            }
        }
        tiles = tempTile.toMap()
    }

    /**
     * Initialize the map data by initializing the individual [PlayerInGame]s' [Team]s.
     * @param players The [PlayerInGame]s whose [Team]s are be placed upon the [TileMapData].
     */
    fun initialize(vararg players: PlayerInGame) {
        if (isInitialized) throw IllegalStateException("TileMapData is already initialized")

        var i = 0 // [MAPS] FIXME this is so janky
        players.forEach {
            it.initialize(get(3, 3 * i)!!, get(4, 3 * i)!!, get(5, 3 * i)!!)
            i++
        }

        isInitialized = true
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

    /**
     * Summons [Mechanism]s in a certain pattern.
     * @param mechanismTemplate The [Mechanism]s to be summoned.
     * @param summonPattern The pattern for [Mechanism]s to be summoned in.
     * @param teamAffiliation The desired [Team] for the [Mechanism]s to belong to, or `null` for [NeutralFaction].
     */
    fun summonMechanismsInPattern(
        mechanismTemplate: MechanismTemplate,
        summonPattern: Set<Position>,
        teamAffiliation: Team?
    ) {
        summonPattern.forEach { position ->
            get(position)?.let { tile -> mechanismTemplate.build(tile, teamAffiliation) }
            // FIXME Check if this fails if shit goes south (and the Mechie cannot be added to Tile)
        }
    }

    /**
     * @return All tiles as a copy of the map.
     */
    fun getAll() = tiles.toMap()
}
