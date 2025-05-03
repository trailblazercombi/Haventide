package app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.effecters.aoe

import app.trailblazercombi.haventide.game2.data.tilemap.TileData
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.Mechanism
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.mfei.HitPointsHandler
import app.trailblazercombi.haventide.game2.data.turntable.NeutralFaction
import app.trailblazercombi.haventide.game2.data.turntable.Team
import app.trailblazercombi.haventide.resources.Res
import app.trailblazercombi.haventide.resources.ultimate_ayumi

class DispelStation(
    parentTile: TileData,
    teamAffiliation: Team?,
    override val maxHitPoints: Int = 80,
    override var currentHitPoints: Int = maxHitPoints,
) : AoEEffecter(parentTile, Res.drawable.ultimate_ayumi, teamAffiliation), HitPointsHandler {

    override fun onStepOnto(mechanism: Mechanism) {
        parentTile.getPhoenix()?.removeAllDebuffs()
    }

    override fun onZeroHitPoints() {
        if (this.canDestruct()) this.destruct()
    }
}
