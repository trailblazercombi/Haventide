package app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.effecters.immediate

import app.trailblazercombi.haventide.game2.data.tilemap.TileData
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.effecters.aoe.AoEEffecter

/**
 * This is used for checking traversal and tilemate addition for ImmediateEffecters.
 *
 * The reason? ImmediateEffecters, normally, immediately destroy themselves upon creation.
 */
class DummyImmediateEffecter(parentTile: TileData) : ImmediateEffecter(parentTile)
