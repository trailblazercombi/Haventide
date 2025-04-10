package app.trailblazercombi.haventide.game.mechanisms

import app.trailblazercombi.haventide.game.arena.TileData
import app.trailblazercombi.haventide.game.modificators.ModificatorFactory

abstract class AoEEffecter(parentTile: TileData) : Mechanism(parentTile, null) {
    override fun vetoTilemateAddition(tilemate: Mechanism): Boolean {
        return false
    }

    // TODO Functionality
}

class AoEModificatorInvoker(
    override val invokable: ModificatorFactory, parentTile: TileData
) : AoEEffecter(parentTile), ModificatorInvoker

class AoEDamageInvoker(val damage: Int, parentTile: TileData) : AoEEffecter(parentTile), DamageInvoker

class AoEHealingInvoker(val healing: Int, parentTile: TileData) : AoEEffecter(parentTile), HealingInvoker
