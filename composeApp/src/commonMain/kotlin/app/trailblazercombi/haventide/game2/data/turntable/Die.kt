package app.trailblazercombi.haventide.game2.data.turntable

import app.trailblazercombi.haventide.resources.MechanismTemplate
import app.trailblazercombi.haventide.resources.AbilityTemplate
import app.trailblazercombi.haventide.resources.DieType

/**
 * This class represents a Die. Every Die has a [type][DieType] - same as the
 * [Phoenixes][MechanismTemplate.Phoenix].
 *
 * If the Phoenix and Die type match, we say the Die is **aligned**.
 * If the types don't match, we say the Die is **scattered**.
 *
 * Dice are needed for Phoenixes to play [Abilities][AbilityTemplate].
 */
class Die(val type: DieType)
