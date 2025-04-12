package app.trailblazercombi.haventide.game.arena

import app.trailblazercombi.haventide.game.mechanisms.Mechanism
import app.trailblazercombi.haventide.game.mechanisms.MechanismTemplate
import app.trailblazercombi.haventide.game.mechanisms.PhoenixMechanism
import app.trailblazercombi.haventide.resources.Res
import app.trailblazercombi.haventide.resources.enemy
import org.jetbrains.compose.resources.DrawableResource

/**
 * The actual Team definition and its members.
 * Used for various purposes. Stored in [the tile map][TileMapData] for easy access.
 *
 * Contains [mechanisms][Mechanism], however, it should primarily contain
 * [Phoenixes][app.trailblazercombi.haventide.game.mechanisms.PhoenixMechanism]
 *
 * __NOTE:__ [Mechanism] drinks [Team] as a parameter in [the constructor][Mechanism.teamAffiliation].
 * As a result, it adds itself to [Team]. __UNDER NO CIRCUMSTANCES__ try attaching it manually!.
 *
 * __NOTE:__ Similar system is in effect when detaching. When [Mechanism.destruct] is called, the Mechanism automatically
 * detaches itself from the [Team]. __UNDER NO CIRCUMSTANCES__ try detaching it manually!
 */
data class Team(
    private val members: MutableSet<Mechanism> = mutableSetOf(),
    val icon: DrawableResource = Res.drawable.enemy // TileMapData.init is resposible for this being correct
) : Iterable<Mechanism> {
    fun add(member: Mechanism) = this.members.add(member)
    fun remove(member: Mechanism) = this.members.remove(member)
    fun getMembers(): Set<Mechanism> = this.members.toSet()
    override fun iterator(): Iterator<Mechanism> = this.members.iterator()

    fun stillHasAlivePhoenixes(): Boolean {
        getMembers().forEach { member -> if (member is PhoenixMechanism) return true }
        return false
    }
}

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
