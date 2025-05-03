package app.trailblazercombi.haventide.game2.data.tilemap.modificators

import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.mfei.ModificatorHandler
import app.trailblazercombi.haventide.resources.ModificatorType

class Burden(parent: ModificatorHandler) : Modificator(ModificatorType.DEBUFF, parent) {
    override fun onDamageTaken(damage: Int): Int {
        return damage * 2
    }

    override fun onHealingRecieved(healing: Int): Int {
        return healing / 2
    }
}
