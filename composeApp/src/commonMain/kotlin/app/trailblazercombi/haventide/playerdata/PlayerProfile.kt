package app.trailblazercombi.haventide.playerdata

import app.trailblazercombi.haventide.game.arena.LocalPlayerInGame
import app.trailblazercombi.haventide.game.arena.PlayerInGame
import app.trailblazercombi.haventide.game.arena.RemotePlayerInGame
import app.trailblazercombi.haventide.game2.data.turntable.TurnTable
import app.trailblazercombi.haventide.resources.MechanismTemplate

/**
 * A data blob containing player information.
 *
 * This denotes a player outside the game and is also a component
 * of creating [PlayerInGame].
 */
data class PlayerProfile(
    val name: String,
    val activeRoster: Set<MechanismTemplate.Phoenix>
    // [MENUS] TODO Add more info related to the profile itself
    //  Profile picture
    //  Medals
    //  Match history
    //  ELO...
) {
    /**
     * Returns a new [PlayerInGame] with this [PlayerProfile].
     */
    fun toPlayerInGame(turnTable: TurnTable, local: Boolean = false): PlayerInGame {
        return if (local) LocalPlayerInGame(this, turnTable)
        else RemotePlayerInGame(this, turnTable)
    }
}
