package app.trailblazercombi.haventide.game

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import app.trailblazercombi.haventide.game.mechanisms.ComposablePhoenixMechanismBall
import app.trailblazercombi.haventide.game.mechanisms.PhoenixMechanism
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

/**
 * A Mechanism that resides on [a Tile][TileData].
 * The Mechanisms interact with each other.
 *
 * Extend this class to build your own Mechanisms.
 *
 * @constructor Builds a mechanism with a specified [parent Tile][TileData].
 * @param parentTile The initial [tile][TileData]this Mechanism will reside on.
 * @param type The type of the Mechanism (deprecated, use `Mechanism is Subclass/Interface` instead)
 * @param teamAffiliation The [Team] this Mechanism belongs to. Managed automatically, if a Team is passed.
 *                        Pass `null` to create an independent mechanism. Such mechanisms will affect everyone
 *                        regardless of the [recipient][Mechanism]'s [Team].
 */
abstract class Mechanism(parentTile: TileData, private val type: MechanismType = MechanismType.ANY, val teamAffiliation: Team?) {
    init { postConstruct() } // Defers assigning team affiliation to prevent leaking this before subclasses are instantiatied
    private fun postConstruct() = teamAffiliation?.members?.add(this)
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
    EFFECTER_IMMIDIATE,
    ANY
    // TODO If this is not used for any checking, DELETE THIS BOOBCAKE
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
 * Designates that a [Mechanism] can recieve [modificators][Modificator].
 * Also allows updating all [modificators][Modificator] based on conditions.
 */
interface ModificatorHandler {
    /**
     * The set that contains all present modificators.
     *
     * __NOTE__: Do NOT modify this list directly.
     * To add a modificator, use the extension method [addModificator].
     * To remove a modificator, use [updateModificators] and fulfill a condition for its removal.
     */
    val modificators: MutableList<Modificator>

    /**
     * This is basically a fancy [MutableList.add] that also checks if the [Modificator]
     * can be added to this [Mechanism], and _then_ also also calls [Modificator.onModificatorsAdded]
     * for each [Modificator] currently on the [Mechanism].
     * @param modificator The [Modificator] to be added.
     */
    // canAddModificator() is checked twice: once here, and once in ModificatorInvoker.
    fun addModificator(modificator: Modificator) {
        if (canAddModificator(modificator)) {
            modificators.add(modificator)
            modificators.forEach { it.onModificatorsAdded() }
        }
    }

    /**
     * Calls [Modificator.onFire] and [Modificator.onDestruct] on every [Modificator]
     * currently on this [Mechanism], while checking for [Modificator.fireCondition]
     * and [Modificator.destructCondition]. If the latter check returns `true`, also handles
     * removal from the [MutableList] of [modificators].
     */
    // TODO Call this in the Game Loop... literally all the time
    fun updateModificators() {
        // 0. This will be used in step 2
        val junkModificators = mutableSetOf<Modificator>()
        // 1. Update modificators and collect junk
        modificators.forEach {
            if (it.fireCondition()) it.onFire()
            if (it.destructCondition()) {
                it.onDestruct()
                junkModificators.add(it)
            }
        }
        // 2. Clear out junk
        if (junkModificators.isNotEmpty()) {
            modificators.removeAll(junkModificators)
            modificators.forEach { it.onModificatorsRemoved() }
        }
    }

    /**
     * Checks if the specified [Modificator] would be happy to reside on this [Mechanism].
     *
     * Override this method in subclasses to achieve good results.
     * @param modificator The specified [Modificator]
     * @return `true` by default, but overriding this method
     * and denying this right to [modificators][Modificator] is very encouraged :)
     */
    fun canAddModificator(modificator: Modificator): Boolean = true
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
 * Invoke a [Modificator] upon the target [tile][TileData].
 */
interface ModificatorInvoker {

    /**
     * Specifies the Modificator pattern used for creation of this Modificator.
     */
    val invokable: ModificatorFactory

    /**
     * Add the [Modificator] to all [mechanisms][Mechanism] on the target [tile][TileData].
     */
    fun invokeModificator(invokable: ModificatorFactory = this.invokable, target: TileData) {
        val stack = target.getMechanismStack()
        stack.forEach {
            // 1. Checks. Two of checks.
            if (it !is ModificatorHandler) return@forEach
            val modificator = invokable.new(it)
            if (!canInvokeModificator(modificator, it)) return@forEach

            // 2. Invokation!
            it.addModificator(modificator)
        }
    }

    /**
     * (Optional) Check if the [Modificator] can be added to the desired [targets][Mechanism].
     */
    fun canInvokeModificator(modificator: Modificator, target: Mechanism): Boolean {
        return target is ModificatorHandler && target.canAddModificator(modificator)
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
    if (mechanism is PhoenixMechanism) ComposablePhoenixMechanismBall(mechanism)
}
