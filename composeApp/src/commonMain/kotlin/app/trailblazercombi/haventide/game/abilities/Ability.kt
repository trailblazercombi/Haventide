package app.trailblazercombi.haventide.game.abilities

import app.trailblazercombi.haventide.game.arena.TileData
import app.trailblazercombi.haventide.game.mechanisms.DummyImmediateEffecter
import app.trailblazercombi.haventide.game.mechanisms.ImmediateEffecters
import app.trailblazercombi.haventide.game.mechanisms.Mechanism
import kotlin.math.sqrt

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// ABILITY AND RELATED CLASSES
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

enum class Abilities(val template: AbilityTemplate) {
    // Basic universal abilities
    BASIC_MOVE(
        AbilityTemplate(
            alignedCost = 0,
            scatteredCost = 1,
            range = sqrt(2.toDouble()),
            executionCheck = { doer, target -> doer.canMove(target) },
            execution = { doer, target -> doer.move(target) }
        )
    ),
    BASIC_STRIKE(
        AbilityTemplate(
            alignedCost = 0,
            scatteredCost = 2,
            range = 2.65,
            executionCheck = { doer, target -> (target.findTeams() - doer.teamAffiliation).isNotEmpty() },
            execution = { doer, target ->
                ImmediateEffecters.DamageInvokers.BASIC_STRIKE.template.build(target, doer.teamAffiliation) },
        )
    ),
    BARRIER(
        AbilityTemplate(
            alignedCost = 2,
            scatteredCost = 2,
            range = 3.27,
            executionCheck = { _, target -> target.canAddMechanism(DummyImmediateEffecter(target)) },
            execution = { doer, target ->
                ImmediateEffecters.MechanismSummoners.BARRIER_MAKER.template.build(target, doer.teamAffiliation) },
        )
    )
}

data class AbilityTemplate(
    val alignedCost: Int,
    val scatteredCost: Int,
    val energyCost: Int = 0,
    val range: Double,
    val executionCheck: (Mechanism, TileData) -> Boolean,
    val execution: (Mechanism, TileData) -> Unit,
)

enum class TargetType { ALLY, ENEMY, EMPTY_TILE }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// DICE
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// COMPOSABLES
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
