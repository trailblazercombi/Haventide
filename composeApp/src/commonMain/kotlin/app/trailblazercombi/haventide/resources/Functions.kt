@file:Suppress("PackageDirectoryMismatch")

package app.trailblazercombi.haventide.resources.functions

import app.trailblazercombi.haventide.game2.data.turntable.Die
import app.trailblazercombi.haventide.resources.DieHighlightState
import app.trailblazercombi.haventide.resources.DieType
import com.sun.jdi.AbsentInformationException
import kotlin.random.Random

fun DieHighlightState(potential: Boolean, selected: Boolean): DieHighlightState =
    if (potential) { if (selected) DieHighlightState.POTENTIAL_SELECTED else DieHighlightState.POTENTIAL_IDLE }
    else { if (selected) DieHighlightState.SELECTED else DieHighlightState.IDLE }

fun countedDiceMatch(actual: Pair<Int, Int>, required: Pair<Int, Int>): Boolean {
    // 1. Check if we have enough aligned dice, or if we have spare...
    val spareAligned = actual.first - required.first
    if (spareAligned < 0) return false // If this failed, there aren't enough aligned dice...
    return actual.second + spareAligned == required.second
}

fun randomDie(): Die {
    return when (Random.nextInt(0, 3)) {
        0 -> Die(DieType.VANGUARD)
        1 -> Die(DieType.SENTINEL)
        2 -> Die(DieType.MEDIC)
        else -> throw AbsentInformationException(
            "It is not possible to create an INVALID die :: RandomDie.new()"
        )
    }
}
