package app.trailblazercombi.haventide.game2.data.tilemap.modificators

import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.mfei.HitPointsHandler
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.mfei.ModificatorHandler
import app.trailblazercombi.haventide.resources.ModificatorFireType
import app.trailblazercombi.haventide.resources.ModificatorType
import app.trailblazercombi.haventide.resources.Res
import app.trailblazercombi.haventide.resources.ability_yumio
import app.trailblazercombi.haventide.resources.modificator_frostbite_description
import app.trailblazercombi.haventide.resources.modificator_frostbite_name
import org.jetbrains.compose.resources.StringResource

class Frostbite(parent: ModificatorHandler) : Modificator(Res.drawable.ability_yumio, ModificatorType.DEBUFF, parent) {
    var itsSoOver = false

    override fun fireCondition(fireType: ModificatorFireType) = fireType == ModificatorFireType.ON_TURN_FINISHED

    override fun onFire() {
        (parent as? HitPointsHandler)?.takeDamage(3)
    }

    override fun onDamageTaken(damage: Int): Int {
        return damage + 7
    }

    override fun onHealingRecieved(healing: Int): Int {
        return healing - 7
    }

    override fun destructCondition() = itsSoOver

    override val name = Res.string.modificator_frostbite_name
    override val description = Res.string.modificator_frostbite_description
    override fun equals(other: Any?) = other is Frostbite
}
