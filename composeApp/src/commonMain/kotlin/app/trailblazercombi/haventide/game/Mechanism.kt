package app.trailblazercombi.haventide.game

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import app.trailblazercombi.haventide.game.mechanisms.ImmidiateEffecter

/**
 * A Mechanism that resides on [a Tile][TileData].
 * The Mechanisms interact with each other.
 *
 * Extend this class to build your own Mechanisms.
 *
 * @constructor Builds a mechanism with a specified [parent Tile][TileData].
 */
abstract class Mechanism(var parentTile: TileData, val type: MechanismType) {
    // TODO Add team affiliation
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
// INTERFACES : Extending Mechanism Functionality
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
 * Special case intended for [ImmidiateEffecter] to ensure its proper destruction
 * right after use.
 */
interface DestructImmidiately {
    fun destructImmidiately()
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// COMPOSABLES
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

@Composable
fun ComposableMechanism(modifier: Modifier = Modifier, mechanismData: Mechanism) {
    TODO(
        "Figure out how a single Mechanism should be displayed, based on the input Mechanism data." +
                "This will be slightly different from calling an entire Stack, in that it's just the lone Mechanism." +
                "It might not even be displayed this way on the finlal Mechanism Stack. But oh well, BOOBCAKE."
    )
}

@Composable
fun ComposableMechanismStack(modifier: Modifier = Modifier, mechanismData: Set<Mechanism>) {
    TODO(
        "Figure out how all the Mechanisms should be displayed, based on the input Mechanism Stack." +
                "TileData will be calling this with its Mechanism Stack." +
                "I guess you can make use of Universal Colorizer."
    )
}