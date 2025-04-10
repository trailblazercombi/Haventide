package app.trailblazercombi.haventide.game.mechanisms

import app.trailblazercombi.haventide.game.arena.Position
import app.trailblazercombi.haventide.game.arena.TileData
import app.trailblazercombi.haventide.game.modificators.ModificatorFactory

abstract class ImmediateEffecter(parentTile: TileData) : Mechanism(parentTile, null) {
    override fun vetoTilemateAddition(tilemate: Mechanism): Boolean {
        return false
    }
}

class ImmediateDamageInvoker(damage: Int, parentTile: TileData) : ImmediateEffecter(parentTile), DamageInvoker {
    init {
        invokeDamage(damage, parentTile)
        destruct()
    }
}

class ImmediateHealingInvoker(healing: Int, parentTile: TileData) : ImmediateEffecter(parentTile), HealingInvoker {
    init {
        invokeHealing(healing, parentTile)
        destruct()
    }
}

class ImmediateModificatorInvoker(
    override val invokable: ModificatorFactory, parentTile: TileData
) : ImmediateEffecter(parentTile), ModificatorInvoker {
    init {
        invokeModificator(invokable, parentTile)
        destruct()
    }
}

class ImmediateMechanismSummoner(
    override val invokable: MechanismTemplate,
    override val summonPattern: (Position) -> Set<Position> = MechanismSummonPattern::Itself,
    parentTile: TileData,
) : ImmediateEffecter(parentTile), MechanismSummonInvoker {
    init {

    }
}
