package app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.effecters.immediate

import app.trailblazercombi.haventide.game2.data.tilemap.TileData
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.mfei.DamageInvoker

/**
 * Invokes damage, then self-destructs.
 */
class ImmediateDamageInvoker(damage: Int, parentTile: TileData) : ImmediateEffecter(parentTile), DamageInvoker {
    init {
        invokeDamage(damage, parentTile)
        destruct()
    }
}
