package app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.effecters.aoe

import app.trailblazercombi.haventide.game2.data.tilemap.TileData
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.Mechanism
import app.trailblazercombi.haventide.resources.Res
import app.trailblazercombi.haventide.resources.*
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

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

    override val icon: DrawableResource = Res.drawable.ultimate_finnian
    override val name: StringResource = Res.string.mechanism_aoeeffecter_barrier_name
    override val description: StringResource = Res.string.mechanism_aoeeffecter_barrier_description

    // There is no conflict when placing this upon Phoenixes.
    // Phoenix won't veto the ImmidiateEffecter summoning this 3x3 Barrier build,
    // but it will definitely veto the Barrier itself.
}
