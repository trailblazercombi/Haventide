package app.trailblazercombi.haventide

import app.trailblazercombi.haventide.game2.data.GameLoop
import app.trailblazercombi.haventide.playerdata.PlayerProfile
import app.trailblazercombi.haventide.resources.PlaceholderPlayers
import kotlinx.coroutines.flow.MutableStateFlow

object Global {

    // GAME LOOP
    val gameLoop = MutableStateFlow<GameLoop?>(null)
    val localPlayer: PlayerProfile = PlaceholderPlayers.PLAYER_ONE.toProfile() // FIXME Dynamic allocation later

    fun createGameLoop(mapName: String, localPlayer: PlayerProfile, remotePlayer: PlayerProfile, localPlayerStarts: Boolean) {
        gameLoop.value = GameLoop(mapData = mapName, localPlayer = localPlayer, remotePlayer = remotePlayer, localPlayerStarts = localPlayerStarts)
    }
}
