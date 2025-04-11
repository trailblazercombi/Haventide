package app.trailblazercombi.haventide.game.abilities

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import app.trailblazercombi.haventide.game.arena.TileData
import app.trailblazercombi.haventide.game.mechanisms.ImmediateEffecters
import app.trailblazercombi.haventide.game.mechanisms.Mechanism
import app.trailblazercombi.haventide.game.mechanisms.MovementEnabled
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.math.sqrt

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// ABILITY AND RELATED CLASSES
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

enum class Abilities(val template: AbilityTemplate) {
    // Basic universal abilities
    BASIC_MOVE(AbilityTemplate(
        alignedCost = 0,
        scatteredCost = 1,
        range = sqrt(2.toDouble()),
        executionCheck = { doer, target -> doer.canMove(target) },
        execution = { doer, target -> doer.move(target) }
    )),
    BASIC_STRIKE(AbilityTemplate(
        alignedCost = 0,
        scatteredCost = 2,
        range = 2.65,
        executionCheck = { doer, target -> (target.findTeams() - doer.teamAffiliation).isNotEmpty() },
        execution = { doer, target -> target.addMechanism(
            ImmediateEffecters.DamageInvokers.BASIC_STRIKE.template.build(target, doer.teamAffiliation)
        ) },
        // TODO Why does immediate effecter have a parameter for teamAffiliation???
    ))
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

data class Die(val type: DieType)

class DiceStack(vararg die: Die) {
    private val dice = die.toMutableSet()
    val diceAsState = MutableStateFlow<Set<Die>>(dice)

    private fun updateState() {
        diceAsState.value = dice.toSet()
    }

    fun addDie(die: Die) {
        dice.add(die)
        updateState()
    }

    fun removeDie(die: Die) {
        dice.remove(die)
        updateState()
    }
}

enum class DieType { VANGUARD, SENTINEL, MEDIC }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// COMPOSABLES
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

@Composable
fun ComposableDie(type: DieType, modifier: Modifier = Modifier) {
    // [GAME LOOP] TODO()
}

@Composable
fun ComposableDiceStack(diceStack: DiceStack, modifier: Modifier = Modifier) {
    val diceList by diceStack.diceAsState.collectAsState()
    // [GAME LOOP] TODO()
}
