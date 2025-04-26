package app.trailblazercombi.haventide.game2.data

import app.trailblazercombi.haventide.Global
import app.trailblazercombi.haventide.game2.data.tilemap.Position
import app.trailblazercombi.haventide.game2.data.tilemap.TileData
import app.trailblazercombi.haventide.game2.data.tilemap.TileMapData
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.Mechanism
import app.trailblazercombi.haventide.game2.data.turntable.LocalPlayerInGame
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.PhoenixMechanism
import app.trailblazercombi.haventide.game2.data.turntable.Die
import app.trailblazercombi.haventide.game2.data.turntable.PlayerInGame
import app.trailblazercombi.haventide.game2.data.turntable.RemotePlayerInGame
import app.trailblazercombi.haventide.game2.data.turntable.TurnTable
import app.trailblazercombi.haventide.game2.viewModel.GameLoopViewModel
import app.trailblazercombi.haventide.netcode.TcpClient
import app.trailblazercombi.haventide.playerdata.PlayerProfile
import app.trailblazercombi.haventide.resources.AbilityTemplates
import app.trailblazercombi.haventide.resources.GameResult
import app.trailblazercombi.haventide.resources.MechanismTemplate

/**
 * This defines the entire Game Loop.
 * Yes, the entire thing.
 * Yes, from the start to the end.
 */
class
GameLoop(
    mapData: String,
    localPlayer: PlayerProfile,
    remotePlayer: PlayerProfile,
    private val localPlayerStarts: Boolean
) {
    init {
        Global.gameLoop.value = this
    }

    var gameResult: GameResult = GameResult.GAME_ONGOING
        private set(value) {
            field = value
            viewModel.gameResult.value = value
        }

    var gameIsOver: Boolean = false
        private set(value) {
            field = value
            viewModel.gameIsOver.value = value
        }

    val turnTable = TurnTable(this)

    val localPlayer: LocalPlayerInGame = localPlayer.toPlayerInGame(turnTable, true) as LocalPlayerInGame
    val remotePlayer: RemotePlayerInGame = remotePlayer.toPlayerInGame(turnTable) as RemotePlayerInGame

//    init {
//        if (localPlayerStarts) {
//            player1 = localPlayer.toPlayerInGame(turnTable = turnTable, local = true)
//            player2 = remotePlayer.toPlayerInGame(turnTable = turnTable)
//        } else {
//            player2 = localPlayer.toPlayerInGame(turnTable = turnTable, local = true)
//            player1 = remotePlayer.toPlayerInGame(turnTable = turnTable)
//        }
//        println("[G.LOOP] Players online! Player 1: $player1, Player 2: $player2")
//    }

    val tileMap = TileMapData(this, mapData)
    val viewModel: GameLoopViewModel

    init {
        println("[G.LOOP] Starting game loop...")
        if (localPlayerStarts) {
            turnTable.initialize(this.localPlayer, this.remotePlayer)
            tileMap.initialize(this.localPlayer, this.remotePlayer)
        } else {
            turnTable.initialize(this.remotePlayer, this.localPlayer)
            tileMap.initialize(this.remotePlayer, this.localPlayer)
        }
        viewModel = GameLoopViewModel(this)
        turnTable.startGame()
        println("[G.LOOP] Game Loop Online!")
    }

    internal fun declareWinner(winner: PlayerInGame, forfeit: Boolean = false) {
        gameOver(
            if (winner is LocalPlayerInGame) {
                if (forfeit) GameResult.VICTORY_FORFEIT else GameResult.VICTORY
            } else {
                if (forfeit) GameResult.DEFEAT_FORFEIT else GameResult.DEFEAT
            }
        )
    }

    internal fun declareDraw() {
        gameOver(GameResult.DRAW)
    }

    private fun gameOver(result: GameResult) {
        this.gameResult = result
        this.gameIsOver = true
        viewModel.showGameOverDialog()
    }

    fun firstPlayer() = if (localPlayerStarts) localPlayer else remotePlayer
    fun secondPlayer() = if (localPlayerStarts) remotePlayer else localPlayer

    /**
     * Get all [PhoenixMechanism] of the current [LocalPlayerInGame].
     */
    fun compileAlliedPhoenixes() = compilePhoenixes(localPlayer)

    /**
     * Get all [PhoenixMechanism] that do not belong to [LocalPlayerInGame].
     */
    fun compileEnemyPhoenixes() = compilePhoenixes(remotePlayer)

    private fun compilePhoenixes(player: PlayerInGame) = player.compilePhoenixes()

    /**
     * Declare the match forfeited by the specified [PlayerInGame].
     */
    fun forfeitMatch(player: PlayerInGame) {
        if (player === localPlayer) {
            declareWinner(remotePlayer, true)
            TcpClient.sendToRemoteServer("YATTA_BOSSHU")
        } else {
            declareWinner(localPlayer, true)
        }
    }

    /**
     * Exposes [TileMapData.getAll]
     */
    fun getAllTiles(): Map<Position, TileData?> = tileMap.getAll()

    /**
     * Check if the game is over.
     * If so, make the game over.
     * If not, keep the game running.
     */
    fun checkGameResult() {
        if (localPlayer.hasPhoenixes() && remotePlayer.hasPhoenixes()) return

        if (localPlayer.hasPhoenixes()) declareWinner(localPlayer, false)
        else if (remotePlayer.hasPhoenixes()) declareWinner(remotePlayer, false)
        else declareDraw()
    }

    internal fun getPhoenix(playerNumber: Int, phoenixNumber: Int): MechanismTemplate.Phoenix {
        val player: PlayerInGame = when (playerNumber) {
            1 -> if (localPlayerStarts) localPlayer else remotePlayer
            2 -> if (localPlayerStarts) remotePlayer else localPlayer
            else -> throw IllegalArgumentException("Player $playerNumber does not exist")
        }
        return player.profile.activeRoster[phoenixNumber - 1]
    }

    // EXPOSURES FOR TCP
    fun remotePlayerMove(template: String, doer: String, target: String) {
        println("[R.PL.MO] Remote player moved! Template $template, Doer $doer, Target $target")
        val ability = AbilityTemplates.valueOf(template.uppercase()).template
        val doer = remotePlayer.findDoer(doer)
        val coords = target.split('+').map { it.toInt() }
        val target = tileMap[coords[0], coords[1]] ?: throw IllegalArgumentException("$target does not exist")
        // This is the same as the local player...
        remotePlayer.executeAbility(ability, doer as Mechanism, target)
        processEndMoveEvent()
    }

    fun remotePlayerFinishedRound() {
        println("[P.PL.FR] Remote player finished round!")
        remotePlayer.finishRound()
        turnTable.endRoundAndNextPlayerTurn()
        viewModel.localPlayerTurn.value = turnTable.currentPlayer() === localPlayer
        viewModel.updateTileHighlights()
    }

    fun remotePlayerForfeited() {
        declareWinner(localPlayer, true)
    }

    /**
     * Interfaces [GameLoopViewModel.processForfeitMatchEvent].
     */
    fun processForfeitMatchEvent() {
        forfeitMatch(localPlayer)
        TcpClient.sendToRemoteServer("YATTA_BOSSHU")
    }

    /**
     * Interfaces [GameLoopViewModel.pushDiceChanges].
     */
    fun pushDiceChanges(dice: List<Die>) = viewModel.pushDiceChanges(dice)

    internal fun localPlayerDisconnected() {
        declareDraw()
    }

    internal fun remotePlayerOfferedDraw() {
        viewModel.showAcceptDrawDialog()
    }

    internal fun remotePlayerDisconnected() {
        declareDraw()
        TcpClient.sendToRemoteServer("YATTA_DOROWU")
    }

    /**
     * Invoke upon ANY user finishing a move.
     */
    fun processEndMoveEvent() {
        checkGameResult()
        turnTable.nextPlayerTurn()

        viewModel.localPlayerTurn.value = turnTable.currentPlayer() === localPlayer
        viewModel.updateTileHighlights()
    }

    internal fun remotePlayerRefusedDraw() {
        viewModel.remotePlayerRefusedDraw()
    }
}
