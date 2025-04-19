package app.trailblazercombi.haventide.game2.data.turntable

import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.Mechanism
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.PhoenixMechanism
import app.trailblazercombi.haventide.resources.Res
import app.trailblazercombi.haventide.resources.enemy
import org.jetbrains.compose.resources.DrawableResource

/**
 * The actual Team definition and its members.
 * Used for various purposes.
 *
 * Contains [mechanisms][Mechanism], however, it should primarily contain
 * [Phoenixes][PhoenixMechanism]
 *
 * __NOTE:__ [Mechanism] drinks [Team] as a parameter in [the constructor][Mechanism.teamAffiliation].
 * As a result, it adds itself to [Team]. __UNDER NO CIRCUMSTANCES__ try attaching it manually!.
 *
 * __NOTE:__ Similar system is in effect when detaching. When [Mechanism.destruct] is called, the Mechanism automatically
 * detaches itself from the [Team]. __UNDER NO CIRCUMSTANCES__ try detaching it manually!
 */
data class Team(
    private val members: MutableSet<Mechanism> = mutableSetOf(),
    val icon: DrawableResource = Res.drawable.enemy
) : Iterable<Mechanism> {

    /**
     * Add a [Mechanism] to this [Team].
     * There are no checks performed. The mechanism simply gets added.
     */
    fun add(member: Mechanism) = this.members.add(member)

    /**
     * Remove a [Mechanism] from this [Team].
     * There are no chceks performed. The mechanism simply gets removed.
     */
    fun remove(member: Mechanism) = this.members.remove(member)

    /**
     * @return An immutable [Set] of all [Mechanism]s currently affiliated with [NeutralFaction].
     */
    fun getMembers(): Set<Mechanism> = this.members.toSet()

    override fun iterator(): Iterator<Mechanism> = this.members.iterator()

    /**
     * Checks if there are any [PhoenixMechanism]s in this [Team].
     *
     * __NOTE__: If this returns `true`, the [PhoenixMechanism]s checked are alive.
     * If the [PhoenixMechanism] was dead, it would have long removed itself from this [Team]
     * upon having [PhoenixMechanism.destruct] called.
     * @return `true` if so, `false` otherwise.
     */
    fun hasPhoenixes(): Boolean {
        getMembers().forEach { member -> if (member is PhoenixMechanism) return true }
        return false
    }
}
