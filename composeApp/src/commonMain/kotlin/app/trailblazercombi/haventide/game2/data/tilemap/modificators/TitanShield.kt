package app.trailblazercombi.haventide.game2.data.tilemap.modificators

import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.mfei.ModificatorHandler
import app.trailblazercombi.haventide.resources.ModificatorType

class TitanShield(parent: ModificatorHandler) : Modificator(ModificatorType.BUFF, parent) {
    private var destructOnNextCheck = false

    override fun onDamageTaken(damage: Int): Int {
        if (damage > 0) destructOnNextCheck = true
        return 0
    }

    override fun destructCondition(): Boolean {
        return destructOnNextCheck
    }
}
