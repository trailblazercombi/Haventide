package app.trailblazercombi.haventide.game2.data.tilemap

import androidx.compose.ui.graphics.Color
import app.trailblazercombi.haventide.game2.data.GameLoop
import app.trailblazercombi.haventide.game2.data.turntable.PlayerInGame
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.Mechanism
import app.trailblazercombi.haventide.game2.data.turntable.Team
import app.trailblazercombi.haventide.game2.data.turntable.NeutralFaction
import app.trailblazercombi.haventide.game2.jetpack.gamescreen.tilemap.Tile
import app.trailblazercombi.haventide.resources.MechanismTemplate
import app.trailblazercombi.haventide.resources.Palette
import java.util.Scanner

/**
 * This is the map data class in all its glory.
 *
 * It deals with the data of individual Tiles... and that's pretty much it.
 * It __does not__ deal with [Mechanism] or the actions or abilities
 * -> [Mechanism] and [TileData] talk that out between themselves.
 */
class TileMapData(private val gameLoop: GameLoop, mapData: String) {
    /**
     * Check if the TileMap was already initialized.
     * @return `true` if [initialize] was called on `this` previously.
     */
    @Suppress("MemberVisibilityCanBePrivate")
    var isInitialized = false
        private set

    val backdropColor: Color
    private val tiles: Map<Position, TileData?>

    init {
        val scanner = Scanner(mapData)
        val tempTile = mutableMapOf<Position, TileData?>()
        var tempBGC: Color = Palette.FillDarkPrimary

        var readMap = false
        var mapY = 0
        var mapX = 0

        while (scanner.hasNextLine()) {
            val line = scanner.nextLine().split(" ")
            if (readMap) {
                line.forEach {
                    val posXY = Position(mapX, mapY)
                    var addTile: TileData? = null
                    when (it) {
                        "###" -> addTile = null
                        "..." -> addTile = TileData(this, posXY)
                        "1:1" -> {
                            addTile = TileData(this, posXY)
                            addTile.addMechanism(gameLoop.getPhoenix(1, 1)
                                .build(addTile, gameLoop.player1.team))
                        }
                        "1:2" -> {
                            addTile = TileData(this, posXY)
                            addTile.addMechanism(gameLoop.getPhoenix(1, 2)
                                .build(addTile, gameLoop.player1.team))
                        }
                        "1:3" -> {
                            addTile = TileData(this, posXY)
                            addTile.addMechanism(gameLoop.getPhoenix(1, 3)
                                .build(addTile, gameLoop.player1.team))
                        }
                        "2:1" -> {
                            addTile = TileData(this, posXY)
                            addTile.addMechanism(gameLoop.getPhoenix(2, 1)
                                .build(addTile, gameLoop.player2.team))
                        }
                        "2:2" -> {
                            addTile = TileData(this, posXY)
                            addTile.addMechanism(gameLoop.getPhoenix(2, 2)
                                .build(addTile, gameLoop.player2.team))
                        }
                        "2:3" -> {
                            addTile = TileData(this, posXY)
                            addTile.addMechanism(gameLoop.getPhoenix(2, 3)
                                .build(addTile, gameLoop.player2.team))
                        }
                    }
                    tempTile[posXY] = addTile
                    mapX++
                }
                mapX = 0; mapY++
            } else {
                when (line.first()) {
                    "backdrop" -> tempBGC = Color(line[1].removePrefix("0x").toLong(16))
                    "tiles" -> readMap = true
                }
            }
        }

        scanner.close()
        tiles = tempTile.toMap()
        backdropColor = tempBGC
    }

    /**
     * Initialize the map data by initializing the individual [PlayerInGame]s' [Team]s.
     * @param players The [PlayerInGame]s whose [Team]s are be placed upon the [TileMapData].
     */
    fun initialize(vararg players: PlayerInGame) {
        if (isInitialized) throw IllegalStateException("TileMapData is already initialized")

//        var i = 0 // [MAPS] FIXME this is so janky
//        players.forEach {
//            it.initialize(get(3, 3 * i)!!, get(4, 3 * i)!!, get(5, 3 * i)!!)
//            i++
//        }

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

    /**
     * @return The number of rows this map has
     */
    fun rows() = 13 // FIXME Needs more robust approach

    /**
     * @return THe number of columns this map has
     */
    fun columns() = 13 // FIXME Needs more robust approach

    override fun toString(): String {
        val sb = StringBuilder()
        tiles.values.forEach {
            sb.append(it.toString())
        }
        return sb.toString()
    }
}
