package app.trailblazercombi.haventide.game.abilities

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.painter.Painter
import androidx.lifecycle.ViewModel
import app.trailblazercombi.haventide.game.abilities.DieHighlightState.*
import app.trailblazercombi.haventide.resources.*
import app.trailblazercombi.haventide.resources.DieStyle.DicePerRow
import com.sun.jdi.AbsentInformationException
import kotlinx.coroutines.flow.MutableStateFlow
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import kotlin.math.ceil
import kotlin.random.Random

/**
 * This class represents a Die. Every Die has a [type][DieType] - same as the
 * [Phoenixes][app.trailblazercombi.haventide.game.mechanisms.MechanismTemplate.Phoenix].
 *
 * If the Phoenix and Die type match, we say the Die is **aligned**.
 * If the types don't match, we say the Die is **scattered**.
 *
 * Dice are needed for Phoenixes to play [Abilities][AbilityTemplate].
 */
class Die(val type: DieType) { // self-contained view model
    val potential = MutableStateFlow(false)
    val selected = MutableStateFlow(false)
}

/**
 * This class is the data layer for a stack of Dice, normally up to eight.
 * (For reasons, this class supports arbitrary number of dies).
 *
 * To add dies to
 */
// WARNING ::
class DiceStack(vararg dice: Die) {
    private val diceStack = mutableListOf(*dice) // SPREAD OPERATOR (*) NEEDED, otherwise it becomes mutableListOf<Array<Die>>ä
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
}

/**
 * The [ViewModel] for [DiceStack].
 */
class DiceStackViewModel(private val diceStack: DiceStack) : ViewModel() {
    val diceStackAsState = MutableStateFlow(diceStack.getDice())

    /**
     * Contains dice that are selected, and appear yellow on screen.
     */
    private val selectedDice = mutableListOf<Die>()

    /**
     * Contains dice that are preferred, and appear with white icon on the screen.
     */
    private val preferredDice = mutableListOf<Die>()

    /**
     * Processes a click, which selects die.
     * This does not do anything if no Phoenix is selected on the Tile Map.
     */
    fun processClick(die: Die) {
        if (preferredDice.isEmpty()) return
        if (selectedDice.contains(die)) deselectDie(die) else selectDie(die)
    }

    /**
     * Selects the most appropriate dice for the desired move.
     * These dice are manually editable later.
     */
    fun autoSelectDice(type: DieType, aligned: Int, scattered: Int) {
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
        selectedDice.toList().forEach {
            deselectDie(it)
        }
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

@Composable
fun ComposableDie(stackViewModel: DiceStackViewModel, die: Die, small: Boolean = true, modifier: Modifier = Modifier) {
    val dieType: Painter = painterResource(die.type.icon)
    val dieSize = if (small) DieStyle.DieSize else DieStyle.DieSize * 2

    val potential by die.potential.collectAsState()
    val selected by die.selected.collectAsState()

    Box(
        modifier = modifier
            .width(dieSize)
            .height(dieSize)
    ) {
        Surface(
            shape = RoundedCornerShape(DieStyle.CornerRounding),
            color = DieHighlightState(potential, selected).fillColor,
            border = BorderStroke(DieStyle.OutlineThickness, DieHighlightState(potential, selected).outlineColor),
            modifier = modifier.clickable(
                indication = LocalIndication.current,
                interactionSource = remember { MutableInteractionSource() },
                onClick = {
                    stackViewModel.processClick(die)
                }
            )
        ) {
            Image(
                painter = dieType,
                contentDescription = null,
                colorFilter = ColorFilter.tint(DieHighlightState(potential, selected).contentColor),
                modifier = modifier
                    .align(Alignment.Center)
                    .padding(DieStyle.InnerPadding)
            )
        }
    }
}

@Composable
fun ComposableDiceStack(viewModel: DiceStackViewModel, small: Boolean = true, modifier: Modifier = Modifier) {
    val diceList by viewModel.diceStackAsState.collectAsState()

    val rows = ceil((diceList.size.toDouble() / DicePerRow)).toInt()
    val finalRow = diceList.size % DicePerRow

    Box (
        contentAlignment = Alignment.BottomStart,
    ) {
        Column {
            Row {
                repeat(finalRow) { ComposableDie(viewModel, diceList[it],
                    small, modifier.padding(DieStyle.Separation / 2))
                }
            }
            repeat(if (finalRow == 0) rows else rows - 1) { row ->
                Row {
                    repeat(DicePerRow) { column ->
                        ComposableDie(
                            viewModel, diceList[finalRow + (DicePerRow * row + column)],
                            small, modifier.padding(DieStyle.Separation / 2)
                        )
                    }
                }
            }
        }
    }
}

enum class DieHighlightState(val fillColor: Color, val outlineColor: Color, val contentColor: Color) {
    IDLE(Palette.Abyss50.compositeOver(Palette.FullGrey), Palette.FullGrey, Palette.FullGrey),
    POTENTIAL_IDLE(Palette.Abyss50.compositeOver(Palette.FullGrey), Palette.FullWhite, Palette.FullWhite),
    SELECTED(
        Palette.Abyss70.compositeOver(Palette.FillYellow),
        Palette.Abyss40.compositeOver(Palette.FillYellow),
        Palette.Abyss40.compositeOver(Palette.FillYellow)
    ),
    POTENTIAL_SELECTED(Palette.Abyss70.compositeOver(Palette.FillYellow), Palette.FillYellow, Palette.FillYellow);
}

fun DieHighlightState(potential: Boolean, selected: Boolean): DieHighlightState =
    if (potential) { if (selected) POTENTIAL_SELECTED else POTENTIAL_IDLE }
    else { if (selected) SELECTED else IDLE }

enum class DieType(val icon: DrawableResource, val title: StringResource, val order: Int) {
    VANGUARD(Res.drawable.vanguard, Res.string.die_type_vanguard, 0),
    SENTINEL(Res.drawable.sentinel, Res.string.die_type_sentinel, 1),
    MEDIC(Res.drawable.medic, Res.string.die_type_medic, 2)
}
