package app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.effecters.immediate

import app.trailblazercombi.haventide.game2.data.tilemap.TileData
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.mfei.ModificatorInvoker
import app.trailblazercombi.haventide.resources.Modificators

/**
 * Invokes [modificators][Modificators], then self-destructs.
 */
class ImmediateModificatorInvoker(
    override val invokable: Modificators, parentTile: TileData
) : ImmediateEffecter(parentTile), ModificatorInvoker {
    init {
        invokeModificator(invokable, parentTile)
        destruct()
    }
}
