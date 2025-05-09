package app.trailblazercombi.haventide.game2.data.tilemap.modificators

import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.PhoenixMechanism
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.mfei.HitPointsHandler
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.mfei.ModificatorHandler
import app.trailblazercombi.haventide.resources.ModificatorFireType
import app.trailblazercombi.haventide.resources.ModificatorType
import app.trailblazercombi.haventide.resources.Res
import app.trailblazercombi.haventide.resources.ability_ayumi
import app.trailblazercombi.haventide.resources.modificator_blessing_description
import app.trailblazercombi.haventide.resources.modificator_blessing_name

class Blessing(parent: ModificatorHandler) : Modificator(Res.drawable.ability_ayumi, ModificatorType.BUFF, parent) {
    var destroyMe: Boolean = false

    override fun onDamageTaken(damage: Int): Int {
        destroyMe = true
        return damage
    }

    override fun destructCondition() = destroyMe

    override fun onFire() {
        if (parent is HitPointsHandler) parent.recieveHealing(51)
    }

    override fun fireCondition(fireType: ModificatorFireType): Boolean {
        return fireType == ModificatorFireType.ON_ROUND_FINISHED
    }

    override val name = Res.string.modificator_blessing_name
    override val description = Res.string.modificator_blessing_description
    override fun equals(other: Any?) = other is Blessing
}
