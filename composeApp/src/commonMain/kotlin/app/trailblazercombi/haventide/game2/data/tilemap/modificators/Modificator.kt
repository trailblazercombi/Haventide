package app.trailblazercombi.haventide.game2.data.tilemap.modificators

import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.mfei.ModificatorHandler
import app.trailblazercombi.haventide.resources.ModificatorFireType
import app.trailblazercombi.haventide.resources.ModificatorType
import org.jetbrains.compose.resources.DrawableResource

/**
 * Modificator is a buff or debuff applied to a [Mechanism].
 * When a [condition][fireCondition] is fulfilled, Modificator [fires][onFire].
 * When a [different condition][destructCondition] is fulfilled, Modificator [self-destructs][onDestruct].
 *
 * __NOTE__: Methods for [onFire] and [onDestruct] and their affiliated
 * condition checks ([1][fireCondition], [2][destructCondition]) and ALREADY HANDLED by [ModificatorHandler].
 * Do NOT implement additional checks!
 */
abstract class Modificator(val icon: DrawableResource, val modificatorType: ModificatorType, protected val parent: ModificatorHandler) {
    /**
     * Fire the modificator.
     * This is a self-contained action, meaning the modificator
     * simply "does something" when a condition is fulfilled
     * (e.g. "heal 20 HP at the end of round")
     */
    open fun onFire() {}

    /**
     * Destruct the modificator.
     * This is a self-contained action, meaning the modificator
     * simply "does something" when a condition is fulfilled
     * (e.g. "heal 20 HP at the end of round").
     *
     * After this action, the modificator is destroyed.
     *
     * __NOTE:__ Destruction itself is ALREADY HANDLED by [ModificatorHandler].
     * Implement only the conseuqences of destructing this modificator here!
     */
    open fun onDestruct() {}

    /**
     * The conditions needing to be fulfilled
     * for [ModificatorHandler] to [fire][onFire] the [Modificator].
     *
     * __NOTE__ Do not even consider making this method `return true` unconditionally.
     * This method is called in []
     *
     * @return `false` by default. Override the method in subclasses to change this behaviour.
     */
    open fun fireCondition(fireType: ModificatorFireType): Boolean = false

    /**
     * The conditions needing to be fulfilled
     * for [ModificatorHandler] to [destruct][onDestruct] the [Modificator].
     * @return `false` by default. Override the method in subclasses to change this behaviour.
     */
    open fun destructCondition(): Boolean = false

    /**
     * Called by [ModificatorHandler] when new modificatos are added to the [Mechanism].
     */
    open fun onModificatorsAdded() {}

    /**
     * Called by [ModificatorHandler] when modificators are removed from the [Mechanism]
     */
    open fun onModificatorsRemoved() {}

    /**
     * Called by [HitPointsHandler] when the [Mechanism] takes damage.
     *
     * Use of this method requires the [Mechanism] to implement both [ModificatorHandler] AND [HitPointsHandler].
     *
     * __NOTE__: Respect [damage == 0][damage].
     * There are modificators that will nullify all damage, but it cannot be guaranteed that it'll be the first
     * or the last to modify the input. Hence, if input is [0][Int], the output should also be [0][Int].
     * @see HitPointsHandler.takeDamage
     * @param damage Damage input by the caller ([HitPointsHandler.takeDamage]).
     * @return Damage output by the [Modificator] after it has been modified.
     *
     * This will be either passed onto more [modificators][Modificator] to modify further,
     * or onto the target to take in its full power.
     *
     * By default, always returns the [input][damage].
     */
    open fun onDamageTaken(damage: Int): Int = damage

    /**
     * Called by [HitPointsHandler] when the [Mechanism] recieves healing.
     *
     * Use of this method requires the [Mechanism] to implement both [ModificatorHandler] AND [HitPointsHandler].
     *
     * __NOTE__: Respect [healing == 0][healing].
     * There are modificators that will nullify all healing, but it cannot be guaranteed that it'll be the first
     * or the last to modify the input. Hence, if input is [0][Int], the output should also be [0][Int].
     * @see HitPointsHandler.recieveHealing
     * @param healing Healing input by the called ([HitPointsHandler.recieveHealing])
     * @return Healing output by the [Modificator] after it has been modified.
     *
     * This will be either passed onto more [modificators][Modificator] to modify further,
     * or onto the target to take in its full power.
     *
     * By default, always returns the [input][healing].
     */
    open fun onHealingRecieved(healing: Int): Int = healing
}
