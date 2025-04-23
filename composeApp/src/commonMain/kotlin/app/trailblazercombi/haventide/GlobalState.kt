package app.trailblazercombi.haventide

import app.trailblazercombi.haventide.game2.data.GameLoop
import app.trailblazercombi.haventide.playerdata.PlayerProfile
import kotlinx.coroutines.flow.MutableStateFlow

object GlobalState {
    val currentGame = MutableStateFlow<GameLoop?>(null)

    fun createGameLoop(mapName: String, localPlayer: PlayerProfile, remotePlayer: PlayerProfile, localPlayerStarts: Boolean) {
        currentGame.value = GameLoop(mapData = mapName, localPlayer = localPlayer, remotePlayer = remotePlayer, localPlayerStarts = localPlayerStarts)
    }
}
