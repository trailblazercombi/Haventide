package app.trailblazercombi.haventide.netcode

import app.trailblazercombi.haventide.Global
import app.trailblazercombi.haventide.playerdata.PlayerProfile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 * This object handles [TcpClient] -> [TcpServer] peer handshakes.
 *
 * Exceptions include the **pairing handshake**, which is handled directly
 * by the [TcpClient] and [TcpServer] themselves.
 *
 * Messages sent around are parsed by the native methods [TcpServer.onMessageRecieved]
 * and [TcpClient.onMessageRecieved], which then call the appropriate Handshaker messages.
 *
 * __NOTE__: [TcpClient] primarily sends messages and mostly recieves just `ACK` from the [TcpServer].
 * Any further replies are handled by the remote [TcpClient] sending it to the local [TcpServer].
 * The entire purpose of [Handshaker] is to keep the send-reply process at leaset somewhat tidy.
 */
object Handshaker {
    private var gameRequested = GameRqState.NONE
        set(value) {
            field = value
            gameIsRequested.value = field != GameRqState.NONE
        }

    val gameIsRequested = MutableStateFlow(false)

    private enum class GameRqState { NONE, LOCAL, REMOTE }

    private var mapName: String? = null
    private var localPlayer: PlayerProfile? = null
    private var remotePlayer: PlayerProfile? = null
    private var localPlayerStarts: Boolean? = null

    val waitingScope = CoroutineScope(Dispatchers.Default)
    var waitingJob: Job? = null

    /**
     * Request a game against the paired peer.
     */
    fun requestGameFromRemote(mapName: String, remotePlayer: PlayerProfile, playerStarts: Boolean): Job {
        if (gameRequested != GameRqState.NONE) throw IllegalStateException("Game already requested! :: 33")
        gameRequested = GameRqState.REMOTE
        this.mapName = mapName
        this.remotePlayer = remotePlayer
        this.localPlayerStarts = !playerStarts // playerStarts returns the status of the other player
        waitingJob = waitingScope.launch { while (localPlayer != null) delay(110) }
        return waitingJob!!
    }

    /**
     * Request a game from the local machine.
     */
    fun requestGameFromLocal(mapName: String, localPlayer: PlayerProfile, playerStarts: Boolean): Job {
        if (gameRequested != GameRqState.NONE) throw IllegalStateException("Game already requested! :: 41")
        gameRequested = GameRqState.LOCAL
        this.mapName = mapName
        this.localPlayer = localPlayer
        this.localPlayerStarts = playerStarts
        waitingJob = waitingScope.launch { while (Handshaker.remotePlayer != null) delay(110) }
        return waitingJob!!
    }

    /**
     * Finish the game request: Add the other player (REMOTE REQUEST)
     */
    fun finishRemoteGameRequest(localPlayer: PlayerProfile) {
        if (gameRequested != GameRqState.REMOTE) throw IllegalStateException("Game request is not remote!")
        this.localPlayer = localPlayer
        val result = finishGameRequest()
        if (!result) {
            cancelGameRequest()
            throw IllegalStateException("Game request could not be finished!")
        }
    }

    /**
     * Finish the game request: Add the other player (LOCAL REQUEST)
     */
    fun finishLocalGameRequest(remotePlayer: PlayerProfile) {
        if (gameRequested != GameRqState.LOCAL) throw IllegalStateException("Game request is not local!")
        this.remotePlayer = remotePlayer
        val result = finishGameRequest()
        if (!result) {
            cancelGameRequest()
            throw IllegalStateException("Game request could not be finished!")
        }
    }

    /**
     * Stop the game request at once.
     *
     * __NOTE:__ This method only tidies up the [Handshaker].
     * Everything else needs to be cleaned up manually!!!
     */
    fun cancelGameRequest() {
        mapName = null
        localPlayer = null
        remotePlayer = null
        localPlayerStarts = null
        gameRequested = GameRqState.NONE
    }

    /**
     * Finalize the game request and create the [GameLoop][app.trailblazercombi.haventide.game2.data.GameLoop].
     */
    private fun finishGameRequest(): Boolean {
        println("$gameRequested, $mapName, ${localPlayer?.name ?: "null"}, ${remotePlayer?.name ?: "null"}, $localPlayerStarts")
        if (gameRequested == GameRqState.NONE) return false
        Global.createGameLoop(
            mapName = mapName ?: return false,
            localPlayer = localPlayer ?: return false,
            remotePlayer = remotePlayer ?: return false,
            localPlayerStarts = localPlayerStarts ?: return false
        )
        gameRequested = GameRqState.NONE
        return true
    }

    /**
     * Generate a random map name.
     */
    fun randomMapName(): String {
        val chosenMap = "parkingLot" // TODO Edit this once more maps are in the pool
        return "files/maps/$chosenMap"
    }
}
