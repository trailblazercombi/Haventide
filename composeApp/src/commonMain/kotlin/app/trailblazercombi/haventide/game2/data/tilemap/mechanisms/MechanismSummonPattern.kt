package app.trailblazercombi.haventide.game2.data.tilemap.mechanisms

import app.trailblazercombi.haventide.game2.data.tilemap.Position

/**
 * This object contains functions that return sets of [Position].
 * The purpose behind all this is to aid with summoning [Mechanisms][Mechanism].
 *
 * __NOTE__: When trying to reference this object by name, __DON'T__.
 * Use `(Position) -> Set<Position>` to denote the type instead.
 */
object MechanismSummonPattern {
    fun Itself(position: Position): Set<Position> = setOf(position)

    fun HollowPlus(position: Position): Set<Position> = position.surroundings(1.toDouble())
    fun FilledPlus(position: Position): Set<Position> = HollowPlus(position) + Itself(position)

    fun Hollow3x3(position: Position): Set<Position> = position.surroundings()
    fun Filled3x3(position: Position): Set<Position> = Hollow3x3(position) + Itself(position)
}
