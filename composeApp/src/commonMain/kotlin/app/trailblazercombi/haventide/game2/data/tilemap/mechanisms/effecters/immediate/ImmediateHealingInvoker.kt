package app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.effecters.immediate

import app.trailblazercombi.haventide.game2.data.tilemap.TileData
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.mfei.HealingInvoker

/**
 * Invokes healing, then self-destructs.
 */
class ImmediateHealingInvoker(healing: Int, parentTile: TileData) : ImmediateEffecter(parentTile), HealingInvoker {
    init {
        invokeHealing(healing, parentTile)
        destruct()
    }
}
