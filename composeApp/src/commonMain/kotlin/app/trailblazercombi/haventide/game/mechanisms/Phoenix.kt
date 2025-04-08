package app.trailblazercombi.haventide.game.mechanisms

import app.trailblazercombi.haventide.game.*

class Phoenix(
    parentTile: TileData,
    override val modificators: MutableList<Modificator> = mutableListOf(),
    override val maxHitPoints: Int,
    override var currentHitPoints: Int = maxHitPoints,
) : Mechanism(parentTile, MechanismType.PHOENIX), ModificatorHandler, HitPointsHandler {

// METHOD OVERRIDES

    override fun onZeroHitPoints() {
        parentTile.removeMechanism(this)
    }

    override fun vetoTraversal(mechanism: Mechanism): Boolean {
        TODO("Team affiliation, allow movements for team members and their invokations")
    }


}