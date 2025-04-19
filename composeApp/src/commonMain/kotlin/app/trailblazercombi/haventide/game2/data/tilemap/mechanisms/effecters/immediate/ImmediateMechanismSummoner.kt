package app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.effecters.immediate

import app.trailblazercombi.haventide.resources.MechanismTemplate
import app.trailblazercombi.haventide.game2.data.tilemap.Position
import app.trailblazercombi.haventide.game2.data.tilemap.TileData
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.MechanismSummonPattern
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.mfei.MechanismSummonInvoker
import app.trailblazercombi.haventide.game2.data.turntable.Team

/**
 * Summons [mechanism(s)][Mechanism], then self-destructs.
 */
class ImmediateMechanismSummoner(
    override val mechanismTemplate: MechanismTemplate,
    override val summonPattern: (Position) -> Set<Position> = MechanismSummonPattern::Itself,
    parentTile: TileData,
    teamAffiliation: Team? = null
) : ImmediateEffecter(parentTile, teamAffiliation), MechanismSummonInvoker {
    init {
        // [ABILITY STACK] TODO Summon pattern does not look properly installed
        summonMechanism(parentTile, teamAffiliation)
        destruct()
    }
}
