package app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.effecters.aoe

import app.trailblazercombi.haventide.game2.data.tilemap.TileData
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.Mechanism
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.PhoenixMechanism
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.mfei.HitPointsHandler
import app.trailblazercombi.haventide.resources.Res
import app.trailblazercombi.haventide.resources.*
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

class DispelStation(
    parentTile: TileData,
    override val maxHitPoints: Int = 80,
    override var currentHitPoints: Int = maxHitPoints,
) : AoEEffecter(parentTile, Res.drawable.ultimate_ayumi), HitPointsHandler {

    override fun onStepOnto(mechanism: Mechanism) {
        println("[DISPEL] Dispelling debuffs from $mechanism!!!")
        (mechanism as? PhoenixMechanism)?.removeAllDebuffs()
    }

    override fun onZeroHitPoints() {
        if (this.canDestruct()) this.destruct()
    }

    override val icon: DrawableResource = Res.drawable.ultimate_ayumi
    override val name: StringResource = Res.string.mechanism_aoeeffecter_dispelstation_name
    override val description: StringResource = Res.string.mechanism_aoeeffecter_dispelstation_description
}
