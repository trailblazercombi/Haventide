package app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.mfei

import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.Mechanism
import app.trailblazercombi.haventide.game2.data.tilemap.modificators.Modificator

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
    // [GAME LOOP] TODO Call this in the Game Loop... literally all the time
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
