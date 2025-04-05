package app.trailblazercombi.haventide.game

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * A Mechanism that resides on [a Tile][TileData].
 * The Mechanisms interact with each other.
 *
 * Extend this class to build your own Mechanisms.
 *
 * @constructor Builds a mechanism with a specified [parent Tile][TileData].
 */
abstract class Mechanism(var parentTile: TileData) {
    /**
     * Denies the specified Mechanism the right to move
     * to the same [Tile][TileData] as this one.
     * @param tilemate The mechanism that would like to gain entry to this one's [parent Tile][TileData].
     * @return `true` or `false`, depending on circumstances (overrides by classes extending this).
     * By default, always returns FALSE.
     */
    open fun vetoTilemateAddition(tilemate: Mechanism): Boolean {
        return false
    }

    /**
     * Denies the specified Mechanism the right to move
     * over this mechanism's [parent Tile][TileData].
     * @param mechanism The mechanism that would like to traverse this one's [parent Tile][TileData].
     * @return `true` or `false`, depending on circumstances (overrides by classes extending this).
     * By default, always returns the same value as [vetoTilemateAddition].
     */
    open fun vetoTraversal(mechanism: Mechanism): Boolean {
        return vetoTilemateAddition(mechanism)
    }

    /**
     * Denies the specified Mechanism the right to leave [the Tile][TileData] they both currently share.
     * @param tilemate The mechanism that would like to leave this one's [parent Tile][TileData].
     * @return `true` or `false`, depending on circumstances (overrides by classes extending this).
     * By default, always returns `true`.
     */
    open fun vetoTilemateRemoval(tilemate: Mechanism): Boolean {
        return true
    }
}

@Composable
fun ComposableMechanism(modifier: Modifier = Modifier, mechanismData: Mechanism) {

}

@Composable
fun ComposableMechanismStack(modifier: Modifier = Modifier, mechanismData: Set<Mechanism>) {

}