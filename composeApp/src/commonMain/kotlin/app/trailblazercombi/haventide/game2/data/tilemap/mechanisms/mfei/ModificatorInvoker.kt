package app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.mfei

import app.trailblazercombi.haventide.game.mechanisms.Mechanism
import app.trailblazercombi.haventide.game.mechanisms.ModificatorHandler
import app.trailblazercombi.haventide.game2.data.tilemap.TileData
import app.trailblazercombi.haventide.game2.data.tilemap.modificators.Modificator
import app.trailblazercombi.haventide.resources.Modificators

/**
 * Invoke a [Modificator] upon the target [tile][TileData].
 */
interface ModificatorInvoker {

    /**
     * Specifies the Modificator pattern used for creation of this Modificator.
     */
    val invokable: Modificators

    /**
     * Add the [Modificator] to all [mechanisms][Mechanism] on the target [tile][TileData].
     */
    fun invokeModificator(invokable: Modificators = this.invokable, target: TileData) {
        val stack = target.getMechanismStack()
        stack.forEach {
            // 1. Checks. Two of checks.
            if (it !is ModificatorHandler) return@forEach
            val modificator = invokable.build(it)
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
