package app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.effecters.immediate

import app.trailblazercombi.haventide.game2.data.tilemap.TileData
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.mfei.ModificatorHandler
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.mfei.ModificatorRemover

class ImmediateModificatorRemover(
    parentTile: TileData
) : ImmediateEffecter(parentTile), ModificatorRemover {
    override fun removeAllDebuffs() {
        parentTile.getMechanismStack().forEach {
            (it as? ModificatorHandler)?.removeAllDebuffs()
        }
    }

    init {
        removeAllDebuffs()
    }
}
