package app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.mfei

import app.trailblazercombi.haventide.game2.data.tilemap.modificators.Modificator

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

    /**
     * Check if the Mechanism is still alive -> as in, still has more than 0 hit points.
     */
    fun isAlive(): Boolean = currentHitPoints < 0
}
