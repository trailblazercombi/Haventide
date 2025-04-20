package app.trailblazercombi.haventide.game2.viewModel

import app.trailblazercombi.haventide.game2.data.tilemap.TileData
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.Mechanism
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.PhoenixMechanism
import app.trailblazercombi.haventide.game2.data.turntable.Die
import app.trailblazercombi.haventide.game2.data.turntable.countedDiceMatch
import app.trailblazercombi.haventide.resources.AbilityTemplate
import app.trailblazercombi.haventide.resources.DieType

/**
 * A supporting class that handles the ability preview from above.
 * Only handles the local player abilities.
 */
data class AbilityPreview(val template: AbilityTemplate, val doer: Mechanism, val target: TileData, var consume: List<Die>) {
    /**
     * @return The [DieType] of the doer, if the doer is a [PhoenixMechanism]. Otherwise, returns `null`.
     */
    fun doerType(): DieType? = if (doer is PhoenixMechanism) doer.template.phoenixType else null

    /**
     * @return The required number of aligned dice.
     */
    fun alignedCost() = template.alignedCost

    /**
     * @return The required number of scattered dice.
     */
    fun scatteredCost() = template.scatteredCost

    /**
     * @return Whether the selected dice are good for consumption.
     */
    fun checkConsumption() = countedDiceMatch(
        actual = consume.count { it.type == doerType() } to consume.count { it.type != doerType() },
        required = alignedCost() to scatteredCost()
    )

    /**
     * Change the list of dice for consumption.
     */
    fun updateConsume(newConsume: List<Die>) { consume = newConsume }
}
