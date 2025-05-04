package app.trailblazercombi.haventide.game2.data.tilemap.mechanisms

import app.trailblazercombi.haventide.game2.data.tilemap.TileData
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.effecters.aoe.AoEEffecter
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.effecters.immediate.ImmediateEffecter
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.mfei.MovementEnabled
import app.trailblazercombi.haventide.game2.data.turntable.NeutralFaction
import app.trailblazercombi.haventide.game2.data.turntable.Team
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

/**
 * A Mechanism that resides on [a Tile][TileData].
 * The Mechanisms interact with each other.
 *
 * Extend this class to build your own Mechanisms.
 *
 * @constructor Builds a mechanism with a specified [parent Tile][TileData].
 * @param parentTile The initial [tile][TileData]this Mechanism will reside on.
 * @param type The type of the Mechanism (deprecated, use `Mechanism is Subclass/Interface` instead)
 * @param teamAffiliation The [Team] this Mechanism belongs to. Managed automatically, if a Team is passed.
 *                        Pass `null` to create an independent mechanism. Such mechanisms will affect everyone
 *                        regardless of the [recipient][Mechanism]'s [Team].
 */
abstract class Mechanism(parentTile: TileData, val teamAffiliation: Team?) {
    /**
     * The parent tile of this Mechanism.
     *
     * __NOTE:__ This `var` is immutable (acts as `val`)
     * unless this [Mechanism] also implements [MovementEnabled].
     * @see MovementEnabled
     * @throws UnsupportedOperationException when attempting to modify it
     * on a [Mechanism] that doesn't implement [MovementEnabled]
     */
    var parentTile: TileData = parentTile
        private set

    /**
     * Denies the specified Mechanism the right to move
     * to the same [Tile][TileData] as this one.
     * @param tilemate The mechanism that would like to gain entry to this one's [parent Tile][TileData].
     * @return `true` or `false`, depending on circumstances (overrides by classes extending this).
     * By default, always returns `true` -- no other mechanism shall exist on the same tile.
     */
    open fun vetoTilemateAddition(tilemate: Mechanism): Boolean {
        return tilemate !is ImmediateEffecter
    }

    /**
     * Denies the specified Mechanism the right to move
     * over this mechanism's [parent Tile][TileData].
     * @param mechanism The mechanism that would like to traverse this one's [parent Tile][TileData].
     * @return `true` or `false`, depending on circumstances (overrides by classes extending this).
     * By default, always returns the same value as [vetoTilemateAddition].
     */
    open fun vetoTraversal(mechanism: Mechanism): Boolean {
        return true
    }

    /**
     * Denies the specified Mechanism the right to leave [the Tile][TileData] they both currently share.
     * @param tilemate The mechanism that would like to leave this one's [parent Tile][TileData].
     * @return `true` or `false`, depending on circumstances (overrides by classes extending this).
     * By default, always returns `false` -- you may remove tilemates.
     */
    open fun vetoTilemateRemoval(tilemate: Mechanism): Boolean {
        return false
    }

    /**
     * Destructs this Mechanism.
     *
     * Destruction means detaching this Mechanism from all its references
     * and (hopefully) having the garbage collector throw it away.
     *
     * @throws UnsupportedOperationException When trying to call this upon a Mechanism
     * that cannot be destructed due to external consequences.
     */
    fun destruct() {
        if (!canDestruct())
            throw UnsupportedOperationException("Cannot destruct Mechanism: canDestruct() check returned false")

        if (this is AoEEffecter) onDestruct()

        this.parentTile.removeMechanism(this)
        this.teamAffiliation?.remove(this) ?: NeutralFaction.remove(this)
        // [THROUGHOUT] TODO More detaching once this needs to be detached elsewhere
    }

    /**
     * Checks if this Mechanism can be destructed.
     */
    @Suppress("RedundantIf", "MemberVisibilityCanBePrivate")
    fun canDestruct(): Boolean {
        if (!this.parentTile.canRemoveMechanism(this)) return false
        // [THROUGHOUT] TODO More checks once this needs to be detached elsewhere
        return true
    }

    /**
     * Moves this Mechanism.
     *
     * Movement means untying and retying this Mechanism to a [new parent tile][TileData].
     * @param to The [new parent tile][TileData] in question.
     * @throws UnsupportedOperationException When trying to call this upon
     * a Mechanism that doesn't implement [MovementEnabled].
     */
    fun move(to: TileData) {
        if (this !is MovementEnabled)
            throw UnsupportedOperationException("Cannot move Mechanism: Mechanism does not implement MovementEnabler")
        this.parentTile.removeMechanism(this)
        this.parentTile = to
        this.parentTile.addMechanism(this)
    }

    /**
     * Checks if this Mechanism can be removed from its current tile and added to the destination.
     *
     * __NOTE__: This method DOES NOT implement [Position.trajectoryTo]!
     * Trajectory traversability is checked upon ability screening (so when [Position.traversableSurroundings]
     * is called by a [Mechanism] / [the tile map][TileMapData] / [button click][TileMapData.tileClickEvent]).
     * No more trajectory checks are done, only the checks to see if Abilities can be played to that spot.
     * If you need to check the trajectory to somewhere for something else, DO SO MANUALLY!
     *
     * @param to The [new parent tile][TileData] in quesiton.
     */
    fun canMove(to: TileData): Boolean {
        return this is MovementEnabled
                && this.parentTile.canRemoveMechanism(this)
                && to.canAddMechanism(this)
    }

    init { postConstruct() } // Defers assigning team affiliation to prevent leaking this before subclasses are instantiatied
    private fun postConstruct() {
        if (this is ImmediateEffecter) return
        teamAffiliation?.add(this) ?: NeutralFaction.add(this)
        parentTile.addMechanism(this)
    }

    abstract val icon: DrawableResource
    abstract val name: StringResource
    abstract val description: StringResource
}
