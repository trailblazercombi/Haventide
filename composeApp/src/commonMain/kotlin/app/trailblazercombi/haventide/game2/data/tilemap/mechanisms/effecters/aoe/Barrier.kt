package app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.effecters.aoe

import app.trailblazercombi.haventide.game2.data.tilemap.TileData
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.Mechanism
import app.trailblazercombi.haventide.resources.Res
import app.trailblazercombi.haventide.resources.ultimate_finnian

class Barrier(parentTile: TileData) : AoEEffecter(parentTile, Res.drawable.ultimate_finnian) {
    override fun vetoTilemateAddition(tilemate: Mechanism): Boolean {
        return true
    }

    override fun vetoTraversal(mechanism: Mechanism): Boolean {
        return true
    }

    override fun onEndOfRound() {
        if (this.canDestruct()) this.destruct()
    }

    // There is no conflict when placing this upon Phoenixes.
    // Phoenix won't veto the ImmidiateEffecter summoning this 3x3 Barrier build,
    // but it will definitely veto the Barrier itself.
}
