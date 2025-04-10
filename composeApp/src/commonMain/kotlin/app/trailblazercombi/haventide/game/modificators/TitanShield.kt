package app.trailblazercombi.haventide.game.modificators

import app.trailblazercombi.haventide.game.mechanisms.ModificatorHandler

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
