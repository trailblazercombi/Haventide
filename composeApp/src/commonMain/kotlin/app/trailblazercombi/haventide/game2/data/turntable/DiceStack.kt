package app.trailblazercombi.haventide.game2.data.turntable

import app.trailblazercombi.haventide.game.abilities.DiceStackViewModel
import app.trailblazercombi.haventide.game.abilities.Die
import com.sun.jdi.AbsentInformationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.random.Random

/**
 * This class is the data layer for a stack of Dice, normally up to eight.
 * (For reasons, this class supports arbitrary number of dies).
 *
 * To add dies to
 */
class DiceStack(vararg dice: Die) {
    // SPREAD OPERATOR (*) NEEDED, otherwise it becomes mutableListOf<Array<Die.kt>>
    private val diceStack = mutableListOf(*dice)
    val viewModel = DiceStackViewModel(this)

    /**
     * Roll random dice.
     *
     * The dice will be stored in a private variable. Get them by calling [getDice].
     * @param howMany Specify, how many dice should be in the stack at the end of the roll.
     * @param freshRoll Discard all dice first. Then roll the exacat amount specified.
     */
    fun roll(howMany: Int, freshRoll: Boolean = false) {
        if (freshRoll) { diceStack.clear() }
        repeat(howMany - diceStack.size) { diceStack.add(randomDie()) }
        diceStack.sortBy { it.type.order }
        pushToViewModel()
    }

    fun consume(consumedDice: List<Die>) {
        consumedDice.forEach { diceStack.remove(it) }
        pushToViewModel()
    }

    fun discardAllDice() {
        diceStack.clear()
        pushToViewModel()
    }

    /**
     * @return The list of [Dice][Die] currently in the stack.
     */
    fun getDice() = diceStack.toList()

    private fun randomDie(): Die {
        return when (Random.nextInt(0, 3)) {
            0 -> Die(DieType.VANGUARD)
            1 -> Die(DieType.SENTINEL)
            2 -> Die(DieType.MEDIC)
            else -> throw AbsentInformationException(
                "It is not possible to create an INVALID die :: RandomDie.new()"
            )
        }
    }

    private fun pushToViewModel() {
        viewModel.diceStackAsState.value = getDice()
    }

    fun freshListState(): MutableStateFlow<List<Die>> {
        val result = mutableListOf<Die>()
        getDice().forEach { result.add(it.copy()) }
        return MutableStateFlow(result.toList())
    }
}
