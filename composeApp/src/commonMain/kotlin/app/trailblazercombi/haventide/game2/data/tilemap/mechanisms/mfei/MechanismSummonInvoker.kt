package app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.mfei

import app.trailblazercombi.haventide.game.arena.Team
import app.trailblazercombi.haventide.game.mechanisms.Mechanism
import app.trailblazercombi.haventide.resources.MechanismTemplate
import app.trailblazercombi.haventide.game2.data.tilemap.Position
import app.trailblazercombi.haventide.game2.data.tilemap.TileData

/**
 * Invoke a new [Mechanism] using [MechanismTemplate].
 */
interface MechanismSummonInvoker {
    val mechanismTemplate: MechanismTemplate
    val summonPattern: (Position) -> Set<Position>

    /**
     * Summons the [Mechanism(s)][Mechanism] around the specified tile according to the pattern.
     */
    fun summonMechanism(onTile: TileData, teamAffiliation: Team?) {
        summonPattern(onTile.position).forEach {
            val summonTile = onTile.parentMap[it] ?: return@forEach
            mechanismTemplate.build(summonTile, teamAffiliation)
        }
    }
}
