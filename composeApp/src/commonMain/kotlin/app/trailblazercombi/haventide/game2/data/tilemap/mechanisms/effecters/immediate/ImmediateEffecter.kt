package app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.effecters.immediate

import app.trailblazercombi.haventide.game2.data.tilemap.TileData
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.Mechanism
import app.trailblazercombi.haventide.game2.data.turntable.Team

/**
 * This class represents an instant action.
 * Implementation-wise, it's a [Mechanism] that gets summoned, does something and then immidiately self-destructs.
 */
abstract class ImmediateEffecter(parentTile: TileData, teamAffiliation: Team? = null) : Mechanism(parentTile, teamAffiliation) {
    override fun vetoTilemateAddition(tilemate: Mechanism) = false
}
