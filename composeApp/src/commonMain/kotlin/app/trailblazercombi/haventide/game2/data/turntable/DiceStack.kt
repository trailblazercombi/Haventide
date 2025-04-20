package app.trailblazercombi.haventide.game2.data.turntable

/**
 * This class is the data layer for a stack of Dice, normally up to eight.
 * (For reasons, this class supports arbitrary number of dies).
 *
 * To add dies to
 */
class DiceStack(vararg dice: Die) {
    // SPREAD OPERATOR (*) NEEDED, otherwise it becomes mutableListOf<Array<Die.kt>>
    private val diceStack = mutableListOf(*dice)

    /**
     * Roll random dice.
     *
     * The dice will be stored in a private variable. Get them by calling [getDice].
     * @param howMany Specify, how many dice should be in the stack at the end of the roll.
     * @param freshRoll Discard all dice first. Then roll the exacat amount specified.
     */
    fun roll(howMany: Int, freshRoll: Boolean = false) {
        if (freshRoll) { discardAllDice() }
        repeat(howMany - diceStack.size) { diceStack.add(randomDie()) }
        diceStack.sortBy { it.type.order }
    }

    /**
     * Consume dice, for example, for ability execution.
     * @param consumedDice The dice consumed.
     */
    fun consume(consumedDice: List<Die>) = consumedDice.forEach { diceStack.remove(it) }

    /**
     * Check if the specified dice are present in the [DiceStack].
     * @param consumedDice The dice checked.
     */
    fun hasDice(consumedDice: List<Die>) = consumedDice.all { diceStack.contains(it) }

    /**
     * Discard all dice.
     */
    fun discardAllDice() = diceStack.clear()

    /**
     * @return The list of [Dice][Die] currently in the stack.
     */
    fun getDice() = diceStack.toList()
}
