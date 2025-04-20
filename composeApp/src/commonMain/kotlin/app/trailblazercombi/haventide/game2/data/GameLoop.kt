package app.trailblazercombi.haventide.game2.data

import app.trailblazercombi.haventide.game2.data.tilemap.Position
import app.trailblazercombi.haventide.game2.data.tilemap.TileData
import app.trailblazercombi.haventide.game2.data.tilemap.TileMapData
import app.trailblazercombi.haventide.game2.data.turntable.LocalPlayerInGame
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.PhoenixMechanism
import app.trailblazercombi.haventide.game2.data.turntable.PlayerInGame
import app.trailblazercombi.haventide.game2.data.turntable.TurnTable
import app.trailblazercombi.haventide.game2.viewModel.GameLoopViewModel
import app.trailblazercombi.haventide.playerdata.PlayerProfile
import app.trailblazercombi.haventide.resources.GameResult

/**
 * This defines the entire Game Loop.
 * Yes, the entire thing.
 * Yes, from the start to the end.
 */
class GameLoop(player1: PlayerProfile, player2: PlayerProfile) {

    var gameResult: GameResult = GameResult.GAME_ONGOING
        private set(value) {
            field = value
            viewModel.gameResult.value = value
        }

    var gameIsOver = false
        private set(value) {
            field = value
            viewModel.gameIsOver.value = value
        }

    val turnTable = TurnTable(this)
    val tileMap = TileMapData(this)
    val viewModel: GameLoopViewModel

    private val player1: PlayerInGame = player1.toPlayerInGame(turnTable = turnTable, local = true)
    private val player2: PlayerInGame = player2.toPlayerInGame(turnTable = turnTable)

    init {
        turnTable.initialize(this.player1, this.player2)
        tileMap.initialize(this.player1, this.player2)
        viewModel = GameLoopViewModel(this)
        turnTable.startGame()
    }

    /**
     * @return The __local__ player that's currently present in the game.
     * @see LocalPlayerInGame
     */
    // [MULTIPLAYER] TODO Fix this to work properly once multiplayer is involved...
    fun localPlayer(): PlayerInGame = player1

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

    /**
     * Get all [PhoenixMechanism] of the current [LocalPlayerInGame].
     */
    fun compileAlliedPhoenixes() = compilePhoenixes(localPlayer())

    /**
     * Get all [PhoenixMechanism] that do not belong to [LocalPlayerInGame].
     */
    fun compileEnemyPhoenixes() = compilePhoenixes(player2) // TODO Needs to be more robust...

    private fun compilePhoenixes(player: PlayerInGame) = player.compilePhoenixes()

    /**
     * Declare the match forfeited by the specified [PlayerInGame].
     */
    fun forfeitMatch(player: PlayerInGame) {
        if (player == localPlayer()) declareWinner(player2, true)
        else declareWinner(player1, true)
        // TODO Propagate to the other client
        // FIXME needs a more robust approach anyways
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
        if (player1.hasPhoenixes() && player2.hasPhoenixes()) return

        if (player1.hasPhoenixes()) declareWinner(player1, false)
        else if (player2.hasPhoenixes()) declareWinner(player2, false)
        else declareDraw()
    }
}
