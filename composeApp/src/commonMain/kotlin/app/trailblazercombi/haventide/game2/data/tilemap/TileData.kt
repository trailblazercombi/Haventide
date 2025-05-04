package app.trailblazercombi.haventide.game2.data.tilemap

import app.trailblazercombi.haventide.Global
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.Mechanism
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.PhoenixMechanism
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.effecters.aoe.AoEEffecter
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.effecters.immediate.ImmediateEffecter
import app.trailblazercombi.haventide.game2.data.turntable.Team
import app.trailblazercombi.haventide.resources.MechanismTemplate
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * This is the class representing a single tile on the map,
 * including its content (Mechanisms)
 */
class TileData(
    // BASIC DATA
    val parentMap: TileMapData,
    internal val position: Position,
    private val mechanismStack: MutableSet<Mechanism> = mutableSetOf(),

    val mechanismStackStack: MutableStateFlow<Set<Mechanism>> = MutableStateFlow(emptySet())
) {

    //  About the CHECKING SYSTEM:
    //  Tile:    All calls for mechanism operations check themselves
    //  TileMap: All calls for mechanism operations check themselves
    //  Mechanism:
    //      Invokers:   "Check, cast, call" on TileMap MechanismStack
    //      Handlers:   Do not perform any checking themselves
    //      destruct(): Performs a full check for destruction
    //                  but thorws an exception if the check failed
    //      move():     Performs a check for "is MovementEnabled",
    //                  and throws an exception if the check failed
    //                  (performs no other checks)

    /**
     * Runs [checks][canAddMechanism] on the specified [mechanism][Mechanism].
     * If [all checks][canAddMechanism] are passed, adds the [mechanism][Mechanism] to this tile.
     *
     * @param mechanism The [mechanism][Mechanism] in question.
     */
    fun addMechanism(mechanism: Mechanism) {
        if (canAddMechanism(mechanism)) {
            mechanismStack.add(mechanism)
            (mechanismStack.toList() - mechanism).forEach {
                if (it is AoEEffecter) it.onStepOnto(mechanism)
            }
            if (mechanism is AoEEffecter) mechanism.onPlacement()
            updateStackStack()
        }
    }

    /**
     * Runs [checks][canAddMechanism] on the specified [mechanism][Mechanism].
     * If [all checks][canAddMechanism] are passed, removes the [mechanism][Mechanism] from this tile.
     *
     * @param mechanism The [mechanism][Mechanism] in question.
     */
    fun removeMechanism(mechanism: Mechanism) {
        if (canRemoveMechanism(mechanism)) {
            if (mechanism is AoEEffecter) mechanism.onDestruct()
            mechanismStack.remove(mechanism)
            mechanismStack.forEach {
                if (it is AoEEffecter) it.onLeave(mechanism)
            }
            updateStackStack()
        }
    }

    /**
     * Checks if the specified [mechanism][Mechanism] can be added to this tile.
     * @param mechanism The [mechanism][Mechanism] in question.
     * @return `true` if the [mechanism][Mechanism] can be added,
     * `false` if the [mechanism][Mechanism] is a duplicate of one already on this tile,
     * or if any of the [mechanisms][Mechanism] on this tile veto the operation.
     */
    @Suppress("MemberVisibilityCanBePrivate")
    fun canAddMechanism(mechanism: Mechanism): Boolean {
        // 1: Check if it's duplicate
        if (mechanismStack.contains(mechanism)) return false
        // 2: Check if it's compatible with the current mechanism stack
        for (mechie in mechanismStack) {
            if (mechie.vetoTilemateAddition(mechanism)) return false
        }
        // All checks passed: Can move mechanism
        return true
    }

    /**
     * Checks if the specified [mechanism][Mechanism] can traverse through this tile.
     * @param mechanism The [mechanism][Mechanism] in question.
     * @return `true` if the [mechanism][Mechanism] can do so,
     * `false` if any of the [mechanisms][Mechanism] on this tile veto the operation.
     */
    fun canTraverse(mechanism: Mechanism): Boolean {
        for (mechie in mechanismStack) {
            if (mechie.vetoTraversal(mechanism)) return false
        }
        return true
    }

    /**
     * Checks if the specified [mechanism][Mechanism] can be removed from this tile.
     * @param mechanism The [mechanism][Mechanism] in question.
     * @return `true` if the [mechanism][Mechanism] can be removed,
     * `false` if the [mechanism][Mechanism] doesn't exist on this tile,
     * or if any of the other [mechanisms][Mechanism] on this tile veto the operation.
     */
    @Suppress("MemberVisibilityCanBePrivate")
    fun canRemoveMechanism(mechanism: Mechanism): Boolean {
        // 1: Check if it's here
        if (mechanism is ImmediateEffecter) return true // Special case, these need to be always destructed
        if (!mechanismStack.contains(mechanism)) return false

        println("[CRM] $this contains $mechanism")

        // 2: Check if it's not necessary for another mechanism to exist
        for (mechie in mechanismStack) {
            if (mechie === mechanism) continue
            if (mechie.vetoTilemateRemoval(mechanism)) return false
        }

        println("[CRM] $this didn't veto $mechanism's removal")

        // All checks passed: Can remove mechanism
        return true
    }

    /**
     * Find and return a [PhoenixMechanism], if it's present on this tile.
     * @return The [PhoenixMechanism] present on this tile.
     *  There should never be mutliple present (because [PhoenixMechanism] vetoes any other [PhoenixMechanism]'s
     *  request to enter), but if there are, returns the first one it finds. Or `null` if there is 0.
     */
    fun getPhoenix() = this.mechanismStack.firstOrNull { it is PhoenixMechanism } as? PhoenixMechanism

    fun getMechanismStack(): Set<Mechanism> {
        return mechanismStack.toSet()
    }

    /**
     * Find and return all [teams][Team] that have a presence on this tile.
     * To find if [NeutralFaction] is present, use [findNeutralFaction].
     */
    fun findTeams(): Set<Team> {
        val result: MutableSet<Team> = mutableSetOf()
        this.mechanismStack.forEach {
            if (it.teamAffiliation != null) result.add(it.teamAffiliation)
        }
        return result.toSet()
    }

    /**
     * Find the presence of [Neutral Faction][app.trailblazercombi.haventide.game2.data.turntable.NeutralFaction].
     */
    fun findNeutralFaction(): Boolean {
        this.mechanismStack.forEach {
            if (it.teamAffiliation == null) return true
        }
        return false
    }

    // FUNCITON EXPOSURE

    /**
     * Exposes [TileMapData.summonMechanismsInPattern].
     */
    fun summonMechanismsInPattern(
        mechanismTemplate: MechanismTemplate,
        summonPattern: (Position) -> Set<Position>,
        teamAffiliation: Team?
    ) {
        this.parentMap.summonMechanismsInPattern(mechanismTemplate, summonPattern(this.position), teamAffiliation)
    }

    /**
     * Exposes [Position.distanceTo].
     */
    fun distanceTo(other: TileData): Double {
        return this.position.distanceTo(other.position)
    }

    private fun updateStackStack() {
        this.mechanismStackStack.value = mechanismStack.toSet()
    }

    fun getTileViewInfoPack(): TileViewInfo {
        val phoenix = getPhoenix()
        return if (phoenix != null) {
            if (phoenix.teamAffiliation == Global.gameLoop.value?.localPlayer?.team)
                TileViewInfo.Ally(phoenix)
            else TileViewInfo.Enemy(phoenix.template)
        } else if (mechanismStack.isEmpty()) {
            TileViewInfo.Empty()
        } else {
            mechanismStack.first().let {
                TileViewInfo.Mechanism(
                    it.icon,
                    it.name,
                    it.description
                )
            }
        }
    }

    /**
     * Exposes mechanismStack.isEmpty.
     */
    fun isEmpty() = mechanismStack.isEmpty()
}
