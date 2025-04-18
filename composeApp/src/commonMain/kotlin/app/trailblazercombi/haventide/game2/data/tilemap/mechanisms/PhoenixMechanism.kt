package app.trailblazercombi.haventide.game2.data.tilemap.mechanisms

import app.trailblazercombi.haventide.game.abilities.AbilityTemplate
import app.trailblazercombi.haventide.game.arena.Team
import app.trailblazercombi.haventide.resources.MechanismTemplate
import app.trailblazercombi.haventide.game2.data.tilemap.TileData
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.mfei.HitPointsHandler
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.mfei.ModificatorHandler
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.mfei.MovementEnabled
import app.trailblazercombi.haventide.game2.data.tilemap.modificators.Modificator
import app.trailblazercombi.haventide.resources.Res
import app.trailblazercombi.haventide.resources.ally
import app.trailblazercombi.haventide.resources.enemy

class PhoenixMechanism(
    parentTile: TileData,
    val template: MechanismTemplate.Phoenix,
    teamAffiliation: Team,
    override val modificators: MutableList<Modificator> = mutableListOf()
) : Mechanism(parentTile, teamAffiliation), ModificatorHandler, HitPointsHandler, MovementEnabled {
    override val maxHitPoints = template.maxHitPoints
    override var currentHitPoints = maxHitPoints

    var energyPoints = 0
    val maxEnergyPoints = template.maxEnergyPoints
    val neededEnergyPoints = template.energyForUltimate
    val teamIcon = if (parentTile.parentMap.gameLoop.localPlayer().team == teamAffiliation) Res.drawable.ally else Res.drawable.enemy

// METHOD OVERRIDES

    override fun onZeroHitPoints() {
        this.destruct()
    }

    fun ultimateReady() = this.energyPoints >= this.neededEnergyPoints

    override fun vetoTraversal(mechanism: Mechanism): Boolean {
        if (mechanism !is PhoenixMechanism) return true
        if (mechanism.teamAffiliation != teamAffiliation) return true
        return false
    }

    private fun abilityList(): List<AbilityTemplate> {
        return listOf(
            template.abilityBasic1,
            template.abilityBasic2,
//            template.abilityInnate1,
//            template.abilityInnate2,
//            template.abilityUltimate
        )
    }

    fun findFirstAvailableAbility(target: TileData): AbilityTemplate? {
        return abilityList().firstOrNull { this.isInRange(it, target) && it.executionCheck(this, target) }
    }

    fun findAllAvailableAbilities(target: TileData): List<AbilityTemplate> {
        return abilityList().filter { this.isInRange(it, target) && it.executionCheck(this, target) }
    }

    fun maxAbilityRange(): Double {
        return abilityList().maxOf { it.range }
    }

    private fun isInRange(it: AbilityTemplate, target: TileData): Boolean {
        return this.parentTile.position.distanceTo(target.position) <= it.range
    }
}
