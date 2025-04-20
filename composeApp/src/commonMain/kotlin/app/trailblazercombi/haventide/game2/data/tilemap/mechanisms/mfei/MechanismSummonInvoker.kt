package app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.mfei

import app.trailblazercombi.haventide.resources.MechanismTemplate
import app.trailblazercombi.haventide.game2.data.tilemap.Position
import app.trailblazercombi.haventide.game2.data.tilemap.TileData
import app.trailblazercombi.haventide.game2.data.turntable.Team

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
        onTile.summonMechanismsInPattern(mechanismTemplate, summonPattern, teamAffiliation)
    }
}
