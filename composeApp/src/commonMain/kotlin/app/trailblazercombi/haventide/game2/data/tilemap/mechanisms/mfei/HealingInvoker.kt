package app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.mfei

import app.trailblazercombi.haventide.game2.data.tilemap.TileData
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.Mechanism

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
