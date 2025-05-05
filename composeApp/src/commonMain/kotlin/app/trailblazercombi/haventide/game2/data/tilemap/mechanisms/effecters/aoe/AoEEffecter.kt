package app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.effecters.aoe

import app.trailblazercombi.haventide.game2.data.tilemap.TileData
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.Mechanism
import app.trailblazercombi.haventide.game2.data.turntable.Team
import org.jetbrains.compose.resources.DrawableResource

/**
 * This class represents an Effecter ([Mechanism]) that's placed on a [tile][TileData],
 * sits there and does something to its known scope (the [tile][TileData]).
 */
abstract class AoEEffecter(parentTile: TileData, val drawable: DrawableResource, teamAffiliation: Team? = null) : Mechanism(parentTile, teamAffiliation) {
    override fun vetoTilemateAddition(tilemate: Mechanism): Boolean {
        return false
    }

    /**
     * Called when the Effecter is placed.
     */
    open fun onPlacement() = Unit

    /**
     * Called when another [Mechanism] steps onto the same tile as this Effecter.
     * @param mechanism The [Mechanism] that just stepped onto the tile.
     */
    open fun onStepOnto(mechanism: Mechanism) = Unit

    /**
     * Called when another [Mechanism] leaves the same tile as this Effecter.
     * @param mechanism The [Mechanism] that just left.
     */
    open fun onLeave(mechanism: Mechanism) = Unit

    /**
     * Called by [TileMapData] when the round ends.
     */
    open fun onEndOfRound() = Unit

    /**
     * Called just before the Effecter is destructed.
     */
    open fun onDestruct() = Unit
}
