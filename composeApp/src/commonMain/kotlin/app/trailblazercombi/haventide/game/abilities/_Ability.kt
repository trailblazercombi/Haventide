package app.trailblazercombi.haventide.game.abilities

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import app.trailblazercombi.haventide.game.arena.TileData
import app.trailblazercombi.haventide.game.mechanisms.ImmediateEffecters
import app.trailblazercombi.haventide.game.mechanisms.Mechanism
import com.sun.jdi.AbsentInformationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.math.sqrt
import kotlin.random.Random

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
        execution = { doer, target ->
            ImmediateEffecters.DamageInvokers.BASIC_STRIKE.template.build(target, doer.teamAffiliation) },
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
    // [GAME LOOP] TODO Finish this and integrate it into the loop...
    private val dice = die.toMutableSet()
    val diceAsState = MutableStateFlow<Set<Die>>(dice)

    private fun updateState() {
        diceAsState.value = dice.toSet()
    }

    private fun removeMatchingDie(die: DieType) {
        dice.forEach {
            if (it.type == die) dice.remove(it).also { return }
        }
    }

    fun roll(howMany: Int) {
        repeat (howMany - dice.size) {
            dice.add(RandomDie.new())
        }
        updateState()
    }

    fun discard(vararg dice: DieType) {
        dice.forEach {
            removeMatchingDie(it)
        }
        updateState()
    }

    fun discardAll() {
        dice.clear()
        updateState()
    }
}

enum class DieType { VANGUARD, SENTINEL, MEDIC }

object RandomDie {
    fun new(): Die {
        return when (Random.nextInt(0, 2)) {
            0 -> Die(DieType.VANGUARD)
            1 -> Die(DieType.SENTINEL)
            2 -> Die(DieType.MEDIC)
            else -> throw AbsentInformationException("It is not possible to create an INVALID die :: RandomDie.new()")
        }
    }
}

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
