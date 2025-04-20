package app.trailblazercombi.haventide.game2.viewModel

import app.trailblazercombi.haventide.game2.data.tilemap.Position
import app.trailblazercombi.haventide.game2.data.tilemap.TileData
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.effecters.immediate.DummyImmediateEffecter

/**
 * A supporting function that generates surroundings that any ability is castable to.
 */
fun abilitySurroundings(tile: TileData): Set<Position> = tile.position.traversableSurroundings(
    tile.parentMap, DummyImmediateEffecter(tile), tile.getPhoenix()?.maxAbilityRange() ?: 0.0)
