package app.trailblazercombi.haventide.game.mechanisms

import app.trailblazercombi.haventide.game.arena.TileData
import app.trailblazercombi.haventide.game.arena.TileMapData

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

class Barrier(parentTile: TileData) : AoEEffecter(parentTile) {
    override fun vetoTilemateAddition(tilemate: Mechanism): Boolean {
        return true
    }

    override fun vetoTraversal(mechanism: Mechanism): Boolean {
        return true
    }

    // There is no conflict when placing this upon Phoenixes.
    // Phoenix won't veto the ImmidiateEffecter summoning this 3x3 Barrier build,
    // but it will definitely veto the Barrier itself.
}
