package app.trailblazercombi.haventide.game.abilities

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.sun.jdi.AbsentInformationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.random.Random

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
        repeat(howMany - dice.size) {
            dice.add(RandomDie())
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

fun RandomDie(): Die {
    return when (Random.nextInt(0, 2)) {
        0 -> Die(DieType.VANGUARD)
        1 -> Die(DieType.SENTINEL)
        2 -> Die(DieType.MEDIC)
        else -> throw AbsentInformationException("It is not possible to create an INVALID die :: RandomDie.new()")
    }
}

@Composable
fun ComposableDie(type: DieType, modifier: Modifier = Modifier) {
    // [GAME LOOP] TODO()
}

@Composable
fun ComposableDiceStack(diceStack: DiceStack, modifier: Modifier = Modifier) {
    val diceList by diceStack.diceAsState.collectAsState()
    // [GAME LOOP] TODO()
}
