package app.trailblazercombi.haventide.game2.data.tilemap

import app.trailblazercombi.haventide.game.arena.NeutralFaction
import app.trailblazercombi.haventide.game.arena.Position
import app.trailblazercombi.haventide.game.arena.Team
import app.trailblazercombi.haventide.game.arena.TileMapData
import app.trailblazercombi.haventide.game.mechanisms.ImmediateEffecter
import app.trailblazercombi.haventide.game.mechanisms.Mechanism
import app.trailblazercombi.haventide.game.mechanisms.PhoenixMechanism
import app.trailblazercombi.haventide.resources.UniversalColorizer
import app.trailblazercombi.haventide.resources.UniversalColorizer.NO_INTERACTIONS
import app.trailblazercombi.haventide.resources.UniversalColorizer.NO_INTERACTIONS_WITH_OUTLINE
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * This is the class representing a single tile on the map,
 * including its content (Mechanisms)
 */
class TileData(
    // BASIC DATA
    val parentMap: TileMapData,
    val position: Position,
    private val mechanismStack: MutableSet<Mechanism> = mutableSetOf(),

    // COMPOSE STATES: COLORIZERS
    private var clickStateColorizer: MutableStateFlow<UniversalColorizer> = MutableStateFlow(
        NO_INTERACTIONS_WITH_OUTLINE
    ),
    private var highlightStateColorizer: MutableStateFlow<UniversalColorizer> = MutableStateFlow(NO_INTERACTIONS),
    private var hoverStateColorizer: MutableStateFlow<UniversalColorizer> = MutableStateFlow(NO_INTERACTIONS)
) {
    // TODO Figure out a better view model
    private val mechanismStackState = MutableStateFlow(mechanismStack.toSet())

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
            updateMechanismStackState()
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
            mechanismStack.remove(mechanism)
            updateMechanismStackState()
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
//        // 0: Do not check ImmediateEffecters, thank you :)
//        if (mechanism is ImmediateEffecter && mechanism !is DummyImmediateEffecter) throw IllegalArgumentException(
//            "Cannot check for ImmediateEffecters. " +
//                    "It would immediately explode and leave behind consequences. " +
//                    "Even if the check failed."
//        )
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

        // 2: Check if it's not necessary for another mechanism to exist
        for (mechie in mechanismStack) {
            if (mechie === mechanism) continue
            if (mechie.vetoTilemateRemoval(mechanism)) return false
        }

        // All checks passed: Can remove mechanism
        return true
    }

    fun getPhoenix(): PhoenixMechanism? {
        this.mechanismStack.forEach { if (it is PhoenixMechanism) return it }
        return null
    }

    fun getMechanismStack(): Set<Mechanism> {
        return mechanismStack.toSet()
    }

    // TODO Move the below to ViewModel...

    private fun updateMechanismStackState() {
        mechanismStackState.value = mechanismStack.toSet()
    }

    /**
     * Send the click event from [the user][ComposableTile] upwards, to the parent [map][TileMapData].
     * The [map][TileMapData] handles the rest.
     */
    fun tileClickEvent() {
        parentMap.viewModel.tileClickEvent(this)
    }

    /**
     * Update the __click state__ of the Composable Tile.
     * This dictates the base fill and outline color.
     * @param clickStateColorizer The new color of this state, as defined by [UniversalColorizer]
     */
    internal fun updateClickState(clickStateColorizer: UniversalColorizer) {
        this.clickStateColorizer.value = clickStateColorizer
    }

    /**
     * Update the __highlight__ state of the Composable Tile.
     * This dictates the outline color, for tiles which would actually be useful to click.
     * @param highlightStateColorizer The new color of this state, as defined by [UniversalColorizer]
     */
    internal fun updateHighlightState(highlightStateColorizer: UniversalColorizer) {
        this.highlightStateColorizer.value = highlightStateColorizer
    }

    /**
     * Update the __hover__ state of the Composable Tile.
     * This dictates the fill and outline glass for pointer devices.
     * @param hoverStateColorizer The new color of this state, as defined by [UniversalColorizer]
     */
    internal fun updateHoverState(hoverStateColorizer: UniversalColorizer) {
        this.hoverStateColorizer.value = hoverStateColorizer
    }

    /**
     * Find and return all [teams][Team] that have a presence on this tile.
     * [NeutralFaction] is considered to be always present.
     */
    fun findTeams(): Set<Team> {
        val result: MutableSet<Team> = mutableSetOf()
        this.mechanismStack.forEach {
            if (it.teamAffiliation != null) result.add(it.teamAffiliation)
        }
        return result.toSet()
    }
}
