package app.trailblazercombi.haventide.game.mechanisms

import app.trailblazercombi.haventide.game.*

abstract class AoEEffecter(parentTile: TileData) : Mechanism(parentTile, MechanismType.EFFECTER_AOE) {
    override fun vetoTilemateAddition(tilemate: Mechanism): Boolean {
        return false
    }
}

class AoEModificatorInvoker(
    override val invokable: ModificatorFactory,
    parentTile: TileData
) : AoEEffecter(parentTile), ModificatorInvoker

class AoEDamageInvoker(parentTile: TileData) : AoEEffecter(parentTile), DamageInvoker

class AoEHealingInvoker(parentTile: TileData) : AoEEffecter(parentTile), HealingInvoker
