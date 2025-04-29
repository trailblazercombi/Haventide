package app.trailblazercombi.haventide.game2.data.tilemap.mechanisms

import app.trailblazercombi.haventide.game2.data.tilemap.TileData
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.mfei.HitPointsHandler
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.mfei.ModificatorHandler
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.mfei.MovementEnabled
import app.trailblazercombi.haventide.game2.data.tilemap.modificators.Modificator
import app.trailblazercombi.haventide.game2.data.turntable.Team
import app.trailblazercombi.haventide.resources.*

class PhoenixMechanism(
    parentTile: TileData,
    val template: MechanismTemplate.Phoenix,
    teamAffiliation: Team,
    override val modificators: MutableList<Modificator> = mutableListOf()
) : Mechanism(parentTile, teamAffiliation), ModificatorHandler, HitPointsHandler, MovementEnabled {
    override val maxHitPoints = template.maxHitPoints
    override var currentHitPoints = maxHitPoints

    private var energyPoints = 0
//    private val maxEnergyPoints = template.maxEnergyPoints
//    private val neededEnergyPoints = template.energyForUltimate
    val teamIcon = teamAffiliation.icon

    override fun onZeroHitPoints() {
        this.destruct()
    }

    override fun vetoTraversal(mechanism: Mechanism): Boolean {
        if (mechanism !is PhoenixMechanism) return true
        if (mechanism.teamAffiliation != teamAffiliation) return true
        return false
    }

//    /**
//     * Check if the Ultimate Ability is ready, aka if the Phoenix has enough Energy to cast it.
//     */
//    fun ultimateReady() = this.energyPoints >= this.neededEnergyPoints

    private fun abilityList(): List<AbilityTemplate> {
        return listOf(
            template.abilityBasic1,
            template.abilityBasic2,
//            template.abilityInnate,
//            template.abilityUltimate // TODO
        )
    }

    /**
     * Check the target [TileData] and find available abilities.
     * @return the first [AbilityTemplate] that can be cast there.
     * Returns `null` if no Ability fits that description.
     */
    fun findFirstAvailableAbility(target: TileData): AbilityTemplate? {
        return abilityList().firstOrNull { this.isInRange(it, target) && it.executionCheck(this, target) }
    }

    /**
     * Check the target [TileData] and find available abilities.
     * @return all available [AbilityTemplate]s that can be cast there.
     * Returns an empty list if no Ability fits that description.
     */
    fun findAllAvailableAbilities(target: TileData): List<AbilityTemplate> {
        return abilityList().filter { this.isInRange(it, target) && it.executionCheck(this, target) }
    }

    /**
     * @return The maximum range of any ability this Phoenix has available.
     */
    fun maxAbilityRange(): Double {
        return abilityList().maxOf { it.range }
    }

    /**
     * Check if the desired ability is in range.
     * @param it The [AbilityTemplate] to check.
     * @param target The unfortunate [TileData] targeted.
     * @return `true` if the ability is in range, otherwise `false`.
     */
    private fun isInRange(it: AbilityTemplate, target: TileData): Boolean {
        return this.parentTile.distanceTo(target) <= it.range
    }

    override fun toString(): String {
        return template.gameId.uppercase()
    }
}
