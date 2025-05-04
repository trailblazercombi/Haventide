package app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.effecters.aoe

import app.trailblazercombi.haventide.game2.data.tilemap.TileData
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.Mechanism
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.mfei.HitPointsHandler
import app.trailblazercombi.haventide.game2.data.turntable.Team
import app.trailblazercombi.haventide.resources.Res
import app.trailblazercombi.haventide.resources.mechanism_aoeeffecter_thorns_description
import app.trailblazercombi.haventide.resources.mechanism_aoeeffecter_thorns_name
import app.trailblazercombi.haventide.resources.ultimate_sylvia

class Thorns(parentTile: TileData)
    : AoEEffecter(parentTile, Res.drawable.ultimate_sylvia) {

    override fun onStepOnto(mechanism: Mechanism) {
        (mechanism as? HitPointsHandler)?.takeDamage(40)
    }

    override val icon = Res.drawable.ultimate_sylvia
    override val name = Res.string.mechanism_aoeeffecter_thorns_name
    override val description = Res.string.mechanism_aoeeffecter_thorns_description
}
