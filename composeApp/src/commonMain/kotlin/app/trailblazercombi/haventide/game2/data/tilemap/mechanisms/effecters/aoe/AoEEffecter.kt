package app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.effecters.aoe

import app.trailblazercombi.haventide.game2.data.tilemap.TileData
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.Mechanism

/**
 * This class represents an Effecter ([Mechanism]) that's placed on a [tile][TileData],
 * sits there and does something to its known scope (the [tile][TileData]).
 */
abstract class AoEEffecter(parentTile: TileData) : Mechanism(parentTile, null) {
    open override fun vetoTilemateAddition(tilemate: Mechanism): Boolean {
        return false
    }

    /**
     * Called when the Effecter is placed.
     */
    open fun onPlacement() {}

    /**
     * Called when another [Mechanism] steps onto the same tile as this Effecter.
     */
    open fun onStepOnto() {}

    /**
     * Called when another [Mechanism] leaves the same tile as this Effecter.
     */
    open fun onLeave() {}

    /**
     * Called by [TileMapData] when the round ends.
     */
    open fun onEndOfRound() {}

    /**
     * Called just before the Effecter is destructed.
     */
    open fun onDestruct() {}
}
