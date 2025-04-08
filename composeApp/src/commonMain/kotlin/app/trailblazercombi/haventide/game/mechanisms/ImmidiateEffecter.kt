package app.trailblazercombi.haventide.game.mechanisms

import app.trailblazercombi.haventide.game.*

abstract class ImmidiateEffecter(parentTile: TileData) : Mechanism(parentTile, MechanismType.EFFECTER_IMMIDIATE) {
    override fun vetoTilemateAddition(tilemate: Mechanism): Boolean {
        return false
    }
}

class ImmidiateDamageInvoker(damage: Int, parentTile: TileData) : ImmidiateEffecter(parentTile), DamageInvoker {
    init {
        invokeDamage(damage, parentTile)
        destruct()
    }
}

class ImmidiateHealingInvoker(healing: Int, parentTile: TileData) : ImmidiateEffecter(parentTile), HealingInvoker {
    init {
        invokeHealing(healing, parentTile)
        destruct()
    }
}

class ImmidiateModificatorInvoker(override val invokable: ModificatorFactory, parentTile: TileData) : ImmidiateEffecter(parentTile), ModificatorInvoker {
    init {
        invokeModificator(invokable, parentTile)
        destruct()
    }
}
