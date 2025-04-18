package app.trailblazercombi.haventide.game2.viewModel

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import app.trailblazercombi.haventide.game2.data.turntable.DiceStack
import app.trailblazercombi.haventide.game2.data.turntable.Die
import app.trailblazercombi.haventide.game2.jetpack.gamescreen.panels.components.DieType
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * The [ViewModel] for [DiceStack].
 */
class DiceStackViewModel(private val diceStack: DiceStack) : ViewModel() {
    val diceStackAsState = MutableStateFlow(diceStack.getDice())

    /**
     * Contains dice that are selected, and appear yellow on screen.
     */
    private val selectedDice = mutableListOf<Die>().toMutableStateList()

    /**
     * Contains dice that are preferred, and appear with white icon on the screen.
     */
    private val preferredDice = mutableListOf<Die>().toMutableStateList()

    internal val typeNeeded = MutableStateFlow<DieType?>(null)
    internal val alignedNeeded = MutableStateFlow(0)
    internal val scatteredNeeded = MutableStateFlow(0)

    private var lastAutoSelection: Triple<DieType, Int, Int>? = null

    /**
     * Processes a click, which selects die.
     * This does not do anything if no Phoenix is selected on the Tile Map.
     */
    fun processClick(die: Die) {
        if (typeNeeded.value == null) return
        if (selectedDice.contains(die)) deselectDie(die) else selectDie(die)
    }

    /**
     * Selects the most appropriate dice for the desired move.
     * These dice are manually editable later.
     */
    fun autoSelectDice(type: DieType, aligned: Int, scattered: Int) {
        // Save the settings
        lastAutoSelection = Triple(type, aligned, scattered)

        // Deselect all dice
        deselectAllDice()

        // Save the specifics of the move previewed
        this.typeNeeded.value = type
        this.alignedNeeded.value = aligned
        this.scatteredNeeded.value = scattered

        // Count how many are left
        var alignedNeeded = aligned
        var scatteredNeeded = scattered // in fact, any dice will do

        // Go through the entire stack...
        diceStack.getDice().forEach {
            // ...find aligned dice
            if (alignedNeeded > 0 && it.type == type) {
                selectDie(it)
                alignedNeeded--
                // ...find scattered dice, prioritize the ones that are of a different type
            } else if (scatteredNeeded > 0 && it.type != type) {
                selectDie(it)
                scatteredNeeded--
            }
        }
        // ...if you don't have enough dice after that
        // If you're missing aligned dice, you're screwed.
        // If you're missing scattered dice, fill them in with aligned dice instead.
        if (scatteredNeeded > 0) {
            diceStack.getDice().forEach {
                if (scatteredNeeded > 0 && !selectedDice.contains(it)) {
                    selectDie(it)
                    scatteredNeeded--
                }
            }
        }
    }

    /**
     * Re-does the last auto-selection.
     */
    fun autoSelectDice() {
        lastAutoSelection?.let {
            autoSelectDice(
                it.first,
                it.second,
                it.third
            )
        }
    }

    private fun selectDie(die: Die) {
        die.selected.value = true
        selectedDice.add(die)
    }

    private fun deselectDie(die: Die) {
        die.selected.value = false
        selectedDice.remove(die)
    }

    fun setDiePreference(type: DieType?) {
        preferredDice.forEach { it.potential.value = false; }.also { preferredDice.clear() }
        if (type == null) return
        diceStack.getDice().filter { type == it.type }.forEach {
            it.potential.value = true
            preferredDice.add(it)
        }
    }

    fun deselectAllDice() {
        selectedDice.toList().forEach { deselectDie(it) }
        typeNeeded.value = null
    }

    fun countSelectedDice(type: DieType): Pair<Int, Int> { // aligned, scattered
        return selectedDice.filter { it.type == type }.size to selectedDice.filter { it.type != type }.size
    }

    fun countAlignedDice(type: DieType): Int {
        return diceStack.getDice().filter { it.type == type }.size
    }

    fun countDice(): Int {
        return diceStack.getDice().size
    }

    fun getSelectedDice() = selectedDice.toList()
}
