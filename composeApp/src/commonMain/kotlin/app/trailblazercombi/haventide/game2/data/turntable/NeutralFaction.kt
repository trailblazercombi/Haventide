package app.trailblazercombi.haventide.game2.data.turntable

import app.trailblazercombi.haventide.game.arena.Team

/**
 * This is a singleton that contains all [Mechanisms][Mechanism]
 * that are not affiliated with any [Team]
 * (they were passed `teamAffiliation = null` upon construction)
 */
data object NeutralFaction : Iterable<Mechanism> {
    private val members: MutableSet<Mechanism> = mutableSetOf()

    fun add(member: Mechanism) = this.members.add(member)
    fun remove(member: Mechanism) = this.members.remove(member)
    fun getMembers(): Set<Mechanism> = this.members.toSet()
    override fun iterator(): Iterator<Mechanism> = this.members.iterator()
}
