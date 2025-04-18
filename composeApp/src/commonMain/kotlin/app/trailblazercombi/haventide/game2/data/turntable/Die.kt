package app.trailblazercombi.haventide.game2.data.turntable

import app.trailblazercombi.haventide.game.abilities.AbilityTemplate
import app.trailblazercombi.haventide.game2.jetpack.gamescreen.panels.components.DieType
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * This class represents a Die. Every Die has a [type][DieType] - same as the
 * [Phoenixes][app.trailblazercombi.haventide.game.mechanisms.MechanismTemplate.Phoenix].
 *
 * If the Phoenix and Die type match, we say the Die is **aligned**.
 * If the types don't match, we say the Die is **scattered**.
 *
 * Dice are needed for Phoenixes to play [Abilities][AbilityTemplate].
 */
class Die(val type: DieType) {
    // self-contained view model
    val potential = MutableStateFlow(false)
    val selected = MutableStateFlow(false)

    fun copy() = Die(type)
}
