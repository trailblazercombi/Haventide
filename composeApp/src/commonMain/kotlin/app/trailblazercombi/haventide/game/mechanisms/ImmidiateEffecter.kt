package app.trailblazercombi.haventide.game.mechanisms

import app.trailblazercombi.haventide.game.*

abstract class ImmidiateEffecter(parentTile: TileData) : Mechanism(parentTile, MechanismType.EFFECTER_IMMIDIATE), DestructImmidiately {
    override fun vetoTilemateAddition(tilemate: Mechanism): Boolean {
        return false
    }

    override fun destructImmidiately() {
        this.parentTile.removeMechanism(this)
    }
}

class ImmidiateDamageInvoker(damage: Int, parentTile: TileData) : ImmidiateEffecter(parentTile), DamageInvoker {
    init {
        invokeDamage(damage, parentTile)
        destructImmidiately()
    }
}

class ImmidiateHealingInvoker(healing: Int, parentTile: TileData) : ImmidiateEffecter(parentTile), HealingInvoker {
    init {
        invokeHealing(healing, parentTile)
        destructImmidiately()
    }
}

class ImmidiateModificatorInvoker(override val invokable: ModificatorFactory, parentTile: TileData) : ImmidiateEffecter(parentTile), ModificatorInvoker {
    init {
        invokeModificator(invokable, parentTile)
        destructImmidiately()
    }
}