package app.trailblazercombi.haventide.game2.data.tilemap.modificators

import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.mfei.ModificatorHandler
import app.trailblazercombi.haventide.resources.ModificatorType
import app.trailblazercombi.haventide.resources.Res
import app.trailblazercombi.haventide.resources.ability_finnian
import app.trailblazercombi.haventide.resources.modificator_titanshield_description
import app.trailblazercombi.haventide.resources.modificator_titanshield_name
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

class TitanShield(parent: ModificatorHandler) : Modificator(Res.drawable.ability_finnian, ModificatorType.BUFF, parent) {
    private var destructOnNextCheck = false

    override fun onDamageTaken(damage: Int): Int {
        if (destructOnNextCheck) return damage
        if (damage > 0) destructOnNextCheck = true
        println("$damage damage nullified!")
        return 0
    }

    override fun destructCondition(): Boolean {
        return destructOnNextCheck
    }

    override val name = Res.string.modificator_titanshield_name
    override val description = Res.string.modificator_titanshield_description
}
