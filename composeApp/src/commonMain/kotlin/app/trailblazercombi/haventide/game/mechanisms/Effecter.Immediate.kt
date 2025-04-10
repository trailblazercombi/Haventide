package app.trailblazercombi.haventide.game.mechanisms

import app.trailblazercombi.haventide.game.arena.Position
import app.trailblazercombi.haventide.game.arena.Team
import app.trailblazercombi.haventide.game.arena.TileData
import app.trailblazercombi.haventide.game.modificators.Modificators

/**
 * This class represents an instant action.
 * Implementation-wise, it's a [Mechanism] that gets summoned, does something and then immidiately self-destructs.
 */
abstract class ImmediateEffecter(parentTile: TileData, teamAffiliation: Team? = null) : Mechanism(parentTile, teamAffiliation) {
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
    override val invokable: Modificators, parentTile: TileData
) : ImmediateEffecter(parentTile), ModificatorInvoker {
    init {
        invokeModificator(invokable, parentTile)
        destruct()
    }
}

class ImmediateMechanismSummoner(
    override val mechanismTemplate: MechanismTemplate,
    override val summonPattern: (Position) -> Set<Position> = MechanismSummonPattern::Itself,
    parentTile: TileData,
    teamAffiliation: Team? = null
) : ImmediateEffecter(parentTile, teamAffiliation), MechanismSummonInvoker {
    init {
        summonMechanism(parentTile, teamAffiliation)
        destruct()
    }
}
