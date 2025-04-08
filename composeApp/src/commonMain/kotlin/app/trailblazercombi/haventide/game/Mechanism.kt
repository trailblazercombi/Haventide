package app.trailblazercombi.haventide.game

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import app.trailblazercombi.haventide.game.mechanisms.ComposablePhoenixMechanismBall
import app.trailblazercombi.haventide.game.mechanisms.Phoenix
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * A Mechanism that resides on [a Tile][TileData].
 * The Mechanisms interact with each other.
 *
 * Extend this class to build your own Mechanisms.
 *
 * @constructor Builds a mechanism with a specified [parent Tile][TileData].
 */
abstract class Mechanism(parentTile: TileData, val type: MechanismType) {
    // TODO Add team affiliation
    /**
     * The parent tile of this Mechanism.
     *
     * __NOTE:__ This `var` is immutable (acts as `val`)
     * unless this [Mechanism] also implements [MovementEnabled].
     * @see MovementEnabled
     * @throws UnsupportedOperationException when attempting to modify it
     * on a [Mechanism] that doesn't implement [MovementEnabled]
     */
    var parentTile: TileData = parentTile
        set(value) {
        if (this !is MovementEnabled) throw UnsupportedOperationException(
            "Cannot change Parent Tile of a Mechanism that does not implement Movable"
        ) else field = value
    }

    /**
     * Denies the specified Mechanism the right to move
     * to the same [Tile][TileData] as this one.
     * @param tilemate The mechanism that would like to gain entry to this one's [parent Tile][TileData].
     * @return `true` or `false`, depending on circumstances (overrides by classes extending this).
     * By default, always returns `true` -- no other mechanism shall exist on the same tile.
     */
    open fun vetoTilemateAddition(tilemate: Mechanism): Boolean {
        return true
    }

    /**
     * Denies the specified Mechanism the right to move
     * over this mechanism's [parent Tile][TileData].
     * @param mechanism The mechanism that would like to traverse this one's [parent Tile][TileData].
     * @return `true` or `false`, depending on circumstances (overrides by classes extending this).
     * By default, always returns the same value as [vetoTilemateAddition].
     */
    open fun vetoTraversal(mechanism: Mechanism): Boolean {
        return vetoTilemateAddition(mechanism)
    }

    /**
     * Denies the specified Mechanism the right to leave [the Tile][TileData] they both currently share.
     * @param tilemate The mechanism that would like to leave this one's [parent Tile][TileData].
     * @return `true` or `false`, depending on circumstances (overrides by classes extending this).
     * By default, always returns `false` -- you may remove tilemates.
     */
    open fun vetoTilemateRemoval(tilemate: Mechanism): Boolean {
        return false
    }

    /**
     * Destructs this Mechanism.
     *
     * Destruction means detaching this Mechanism from all its references
     * and (hopefully) having the garbage collector throw it away.
     *
     * @throws UnsupportedOperationException When trying to call this upon a Mechanism
     * that cannot be destructed due to external consequences.
     */
    open fun destruct() {
        if (!canDestruct())
            throw UnsupportedOperationException("Cannot destruct Mechanism: canDestruct() check returned false")
        this.parentTile.removeMechanism(this)
        // TODO More detaching once this needs to be detached elsewhere
    }

    /**
     * Checks if this Mechanism can be destructed.
     */
    @Suppress("RedundantIf")
    open fun canDestruct(): Boolean {
        if (!this.parentTile.canRemoveMechanism(this)) return false
        // TODO More checks once this needs to be detached elsewhere
        return true
    }

    /**
     * Moves this Mechanism.
     *
     * Movement means untying and retying this Mechanism to a [new parent tile][TileData].
     * @param to The [new parent tile][TileData] in question.
     * @throws UnsupportedOperationException When trying to call this upon
     * a Mechanism that doesn't implement [MovementEnabled].
     */
    open fun move(to: TileData) {
        if (this !is MovementEnabled)
            throw UnsupportedOperationException("Cannot move Mechanism: Mechanism does not implement MovementEnabler")
    }

    open fun canMove(to: TileData): Boolean {
        return this.parentTile.canRemoveMechanism(this)
                && to.canAddMechanism(this)
    }
}

/**
 * The enum representing various types of [mechanisms][Mechanism].
 * Good for categorization, and not much else.
 */
enum class MechanismType {
    PHOENIX,
    EFFECTER_AOE,
    EFFECTER_IMMIDIATE
    // TODO I'm sure this enum will be useful, but
    //  in case I end up doing all checks via "is"/"!is" (instaneof),
    //  DELETE THIS BOOBCAKE
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// MECHANISM FUNCTIONALITY EXTENSION INTERFACES (MFEIs)
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

/**
 * Designates that a [Mechanism] can recieve damage and healing.
 * Also allows handling death/destruction using [onZeroHitPoints].
 */
interface HitPointsHandler {
    /**
     * The maximum hit points a [Mechanism] can have.
     */
    val maxHitPoints: Int

    /**
     * The current hit points of the [Mechanism].
     * When this number goes above [maxHitPoints], it gets truncated to [maxHitPoints].
     * When this number goes below zero, it gets truncated to [0][Int] and [onZeroHitPoints] is called.
     *
     * __NOTE:__ Do NOT modify this property directly.
     * Instead, use [takeDamage] to decrease it and [recieveHealing] to increase it.
     */
    var currentHitPoints: Int

    /**
     * Invoke damage.
     * This reduces the mechanism's hit points.
     * When hit points reach zero, calls [onZeroHitPoints].
     *
     * __NOTE__ This method, by default, does not [check if the target can take damage][canTakeDamage].
     * This is because that check is already performed by the callers of this method,
     * notably [DamageInvoker].
     *
     * @param damage The damage taken.
     * This will be processed by [modificators][Modificator] if `this` also implements
     * [ModificatorHandler], modifying the final number before the damage is taken.
     */
    fun takeDamage(damage: Int) {
        // 1. Find the true damage based on Modificators applied to Mechanism
        var trueDamage = damage
        if (this is ModificatorHandler)
            this.modificators.forEach { trueDamage = it.onDamageTaken(trueDamage) }

        // 2. Check if the number makes any sense as a damage number
        if (trueDamage <= 0) return

        // 3. If it's good, deal the damage! If it's too good, kill the Mechanism
        currentHitPoints -= trueDamage
        if (currentHitPoints <= 0) {
            currentHitPoints = 0
            onZeroHitPoints()
        }
    }

    /**
     * Checks if it's reasonable for the target to take damage.
     */
    fun canTakeDamage(): Boolean = currentHitPoints > 0

    /**
     * Invoke healing.
     * This increases the mechanism's hit points.
     *
     * __NOTE__ This method, by default, does not [check if the target can recieve healing][canRecieveHealing].
     * This is because that check is already performed by the callers of this method,
     * notably [HealingInvoker].
     *
     * @param healing The healing taken.
     * This will be processed by [modificators][Modificator] if `this` also implements
     * [ModificatorHandler], modyfing the final number before the healing is taken.
     */
    fun recieveHealing(healing: Int) {
        // 1. Find the true healing based on Modifiers applied to Mechanism
        var trueHealing = healing
        if (this is ModificatorHandler)
            this.modificators.forEach { trueHealing = it.onHealingRecieved(trueHealing) }

        // 2. Check if the number makes any sense as a healing number
        if (trueHealing <= 0) return

        // 3. Heal the Mechanism. If the healing's too good, don't heal it anymore
        currentHitPoints += trueHealing
        if (currentHitPoints <= maxHitPoints) {
            currentHitPoints = maxHitPoints
        }
    }

    /**
     * Checks if it's reasonable for the target to recieve healing.
     */
    fun canRecieveHealing(): Boolean = currentHitPoints < maxHitPoints

    /**
     * Called when [takeDamage] finds that [currentHitPoints] are zero or less.
     * Can also be called manually, in case it's necessary.
     */
    fun onZeroHitPoints()
}

/**
 * Invoke damage upon the target [tile][TileData].
 */
interface DamageInvoker {

    /**
     * Damage all [mechanisms][Mechanism] on the target [tile][TileData].
     * Also checks if damage can be invoked
     */
    fun invokeDamage(damage: Int, target: TileData) {
        target.getMechanismStack().forEach {
            // Check, cast, invoke!
            if (canInvokeDamage(it)) {
                it as HitPointsHandler
                it.takeDamage(damage)
            }
        }
    }

    /**
     * Check if the target(s) can take damage.
     */
    fun canInvokeDamage(target: Mechanism): Boolean {
        return (target is HitPointsHandler && target.canTakeDamage())
    }
}

/**
 * Invoke healing upon the target [tile][TileData].
 */
interface HealingInvoker {
    /**
     * Heal all or selected [mechanisms][Mechanism] on the target [tile][TileData].
     * Also checks if healing can be invoked.
     */
    fun invokeHealing(healing: Int, target: TileData) {
        target.getMechanismStack().forEach {
            // Check, cast, invoke!
            if (canInvokeHealing(it)) {
                it as HitPointsHandler
                it.recieveHealing(healing)
            }
        }
    }

    /**
     * Check if the [targets][Mechanism] can be healed.
     */
    fun canInvokeHealing(target: Mechanism): Boolean {
        return (target is HitPointsHandler) && target.canRecieveHealing()
    }
}

/**
 * Designates that a [Mechanism] can change its [parent Tile][TileData].
 *
 * The methods enabled by [MovementEnabled] are already implemented in [Mechanism] as they
 */
interface MovementEnabled

// See also: Modificator.kt

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// COMPOSABLES
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

@Composable
fun ComposableMechanism(mechanism: Mechanism, modifier: Modifier = Modifier) {
    if (mechanism is Phoenix) ComposablePhoenixMechanismBall(mechanism)
}
