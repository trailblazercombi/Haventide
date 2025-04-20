package app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.effecters.aoe

import app.trailblazercombi.haventide.game2.data.tilemap.TileData
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.Mechanism

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
