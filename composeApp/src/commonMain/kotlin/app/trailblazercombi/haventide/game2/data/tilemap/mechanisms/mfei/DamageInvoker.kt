package app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.mfei

import app.trailblazercombi.haventide.game2.data.tilemap.TileData
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.Mechanism

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
