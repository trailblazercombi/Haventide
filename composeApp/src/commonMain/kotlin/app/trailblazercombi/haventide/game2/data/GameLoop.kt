package app.trailblazercombi.haventide.game2.data

import app.trailblazercombi.haventide.game.arena.GameLoopViewModel
import app.trailblazercombi.haventide.game.arena.LocalPlayerInGame
import app.trailblazercombi.haventide.game.arena.PlayerInGame
import app.trailblazercombi.haventide.game.arena.PlayerProfile
import app.trailblazercombi.haventide.game.mechanisms.PhoenixMechanism
import app.trailblazercombi.haventide.game2.data.tilemap.TileMapData
import app.trailblazercombi.haventide.game2.data.turntable.TurnTable
import app.trailblazercombi.haventide.resources.GameResult
import app.trailblazercombi.haventide.resources.PlayerTurnStates

/**
 * This defines the entire Game Loop.
 * Yes, the entire thing.
 * Yes, from the start to the end.
 */
class GameLoop(profile1: PlayerProfile, profile2: PlayerProfile) {

    // Players and TurnTable
    val turnTable: TurnTable = TurnTable(this)

    private val player1: PlayerInGame = profile1.toPlayerInGame(turnTable, true)
    private val player2: PlayerInGame = profile2.toPlayerInGame(turnTable)

    internal val tileMap = TileMapData(turnTable, this)
    val viewModel: GameLoopViewModel = GameLoopViewModel(this)

    // [MAPS] TODO Add the TileMapData file loader, and positions, and stuff...

    init {
        turnTable.initialize(player1, player2)
    }

    // [MULTIPLAYER] TODO Fix this to work properly once multiplayer is involved...
    fun localPlayer() = player1

    fun declareWinner(winner: PlayerInGame, forfeit: Boolean = false) {
        gameOver(
            if (winner is LocalPlayerInGame) {
                if (forfeit) GameResult.VICTORY_FORFEIT else GameResult.VICTORY
            } else {
                if (forfeit) GameResult.DEFEAT_FORFEIT else GameResult.DEFEAT
            }
        )
    }

    fun declareDraw() {
        gameOver(GameResult.DRAW)
    }

    private fun gameOver(result: GameResult) {
        triggerGameOverDialog(result)
    }

    private fun triggerGameOverDialog(result: GameResult) {
        viewModel.gameOverDialogResult.value = result
        viewModel.gameOverDialog.value = true
    }

    internal fun updatePlayerTurnState(result: PlayerInGame) {
        if (!localPlayer().isValidForTurn()) viewModel.localPlayerTurn.value = PlayerTurnStates.ROUND_FINISHED
        else if (result == localPlayer()) viewModel.localPlayerTurn.value = PlayerTurnStates.THEIR_TURN
        else viewModel.localPlayerTurn.value = viewModel.localPlayerTurn.value.also {
            PlayerTurnStates.NOT_THEIR_TURN.consume(result)
        }
    }

    fun compileAlliedPhoenixes(): List<PhoenixMechanism> {
        return compilePhoenixes(localPlayer())
    }

    fun compileEnemyPhoenixes(): List<PhoenixMechanism> {
        return compilePhoenixes(player2) // TODO A more robust approach...
    }

    private fun compilePhoenixes(player: PlayerInGame): List<PhoenixMechanism> {
        val result: MutableList<PhoenixMechanism> = mutableListOf()
        player.team.forEach {
            if (it !is PhoenixMechanism) return@forEach
            result.add(it)
        }
        return result.toList()
    }

    fun forfeitMatch(player: PlayerInGame) {
        if (player == localPlayer()) declareWinner(player2, true) // FIXME needs a more robust approach anyways
        else declareWinner(player1, true)
        // TODO Propagate to the other client
    }
}
