package app.trailblazercombi.haventide.game2.data.turntable

import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.Mechanism
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.PhoenixMechanism

/**
 * A singleton for all [Mechanism]s that do not belong to a [PlayerInGame].
 * Used for various purposes.
 *
 * Contains [Mechanism]s.
 *
 * __NOTE:__ [Mechanism] drinks [Team] as a parameter in [the constructor][Mechanism.teamAffiliation].
 * As a result, it adds itself to [Team]. __UNDER NO CIRCUMSTANCES__ try attaching it manually!.
 *
 * __NOTE:__ Similar system is in effect when detaching. When [Mechanism.destruct] is called, the Mechanism automatically
 * detaches itself from the [Team]. __UNDER NO CIRCUMSTANCES__ try detaching it manually!
 */
data object NeutralFaction : Iterable<Mechanism> {
    private val members: MutableSet<Mechanism> = mutableSetOf()

    /**
     * Add a [Mechanism] to the [NeutralFaction].
     * There are no checks performed. The mechanism simply gets added.
     */
    fun add(member: Mechanism) = this.members.add(member)

    /**
     * Remove a [Mechanism] from the [NeutralFaction].
     * There are no chceks performed. The mechanism simply gets removed.
     */
    fun remove(member: Mechanism) = this.members.remove(member)

    /**
     * @return An immutable [Set] of all [Mechanism]s currently affiliated with [NeutralFaction].
     */
    fun getMembers(): Set<Mechanism> = this.members.toSet()

    override fun iterator(): Iterator<Mechanism> = this.members.iterator()
}
