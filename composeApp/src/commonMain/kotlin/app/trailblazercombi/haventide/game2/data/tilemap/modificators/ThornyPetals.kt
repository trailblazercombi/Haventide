package app.trailblazercombi.haventide.game2.data.tilemap.modificators

import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.mfei.HitPointsHandler
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.mfei.ModificatorHandler
import app.trailblazercombi.haventide.resources.ModificatorFireType
import app.trailblazercombi.haventide.resources.ModificatorType
import app.trailblazercombi.haventide.resources.Res
import app.trailblazercombi.haventide.resources.ability_sylvia
import app.trailblazercombi.haventide.resources.modificator_thornypetals_description
import app.trailblazercombi.haventide.resources.modificator_thornypetals_name
import org.jetbrains.compose.resources.StringResource

class ThornyPetals(parent: ModificatorHandler) : Modificator(Res.drawable.ability_sylvia, ModificatorType.DEBUFF, parent) {
    private var damageNow = 10
    private var destructible = false
    var hasMoved = false

    override fun onFire() {
        if (damageNow > 40) {
            destructible = true
            return
        }

        (parent as? HitPointsHandler)?.takeDamage(damageNow)
        hasMoved = false
        damageNow * 2
    }

    override fun fireCondition(fireType: ModificatorFireType): Boolean {
        return fireType == ModificatorFireType.ON_TURN_FINISHED && hasMoved
    }

    override fun destructCondition() = destructible

    override val name = Res.string.modificator_thornypetals_name
    override val description = Res.string.modificator_thornypetals_description
    override fun equals(other: Any?) = other is ThornyPetals
}
