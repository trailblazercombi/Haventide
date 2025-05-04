package app.trailblazercombi.haventide.playerdata

import app.trailblazercombi.haventide.game2.data.turntable.LocalPlayerInGame
import app.trailblazercombi.haventide.game2.data.turntable.PlayerInGame
import app.trailblazercombi.haventide.game2.data.turntable.RemotePlayerInGame
import app.trailblazercombi.haventide.game2.data.turntable.TurnTable
import app.trailblazercombi.haventide.resources.MechanismTemplate
import app.trailblazercombi.haventide.resources.PhoenixTemplates

/**
 * A data blob containing player information.
 *
 * This denotes a player outside the game and is also a component
 * of creating [PlayerInGame].
 */
data class PlayerProfile(val name: String, var activeRoster: List<MechanismTemplate.Phoenix>) {

    /**
     * Returns a new [PlayerInGame] with this [PlayerProfile].
     */
    fun toPlayerInGame(turnTable: TurnTable, local: Boolean = false): PlayerInGame {
        return if (local) LocalPlayerInGame(this, turnTable)
        else RemotePlayerInGame(this, turnTable)
    }

    constructor(args: String) : this(
        name = "Remotee",
        activeRoster = args.split("+").map { PhoenixTemplates.valueOf(it.uppercase()).template }
    )

    fun rosterAsPacket() = "${activeRoster[0].gameId}+${activeRoster[1].gameId}+${activeRoster[2].gameId}"

    override fun toString() = "Player $name"
}
