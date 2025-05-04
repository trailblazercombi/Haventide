package app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.effecters.immediate

import app.trailblazercombi.haventide.game2.data.tilemap.TileData
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.Mechanism
import app.trailblazercombi.haventide.game2.data.turntable.Team
import app.trailblazercombi.haventide.resources.Res
import app.trailblazercombi.haventide.resources.*
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

/**
 * This class represents an instant action.
 * Implementation-wise, it's a [Mechanism] that gets summoned, does something and then immidiately self-destructs.
 */
abstract class ImmediateEffecter(parentTile: TileData, teamAffiliation: Team? = null) : Mechanism(parentTile, teamAffiliation) {
    override fun vetoTilemateAddition(tilemate: Mechanism) = false

    override val icon: DrawableResource = Res.drawable.emptytile
    override val name: StringResource = Res.string.its_an_effecter
    override val description: StringResource = Res.string.its_an_effecter
}
