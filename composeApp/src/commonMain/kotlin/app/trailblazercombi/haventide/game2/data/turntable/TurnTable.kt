package app.trailblazercombi.haventide.game2.data.turntable

import app.trailblazercombi.haventide.game.arena.GameLoop
import app.trailblazercombi.haventide.game.arena.PlayerInGame

class TurnTable(private val gameLoop: GameLoop) {
    private val thisTurnArray: MutableList<PlayerInGame> = mutableListOf()
    private val nextTurnArray: MutableList<PlayerInGame> = mutableListOf()
    private var currentPlayerIndex = 0

    fun initialize(vararg players: PlayerInGame) {
        thisTurnArray.addAll(players)
        thisTurnArray.forEach {
            it.startRound()
        }
    }

    fun currentPlayer(): PlayerInGame = thisTurnArray[currentPlayerIndex]

    fun allPlayers(): List<PlayerInGame> = thisTurnArray.toList()

    fun nextPlayerTurn(): PlayerInGame {
        var result: PlayerInGame
        // [COROUTINES] TODO Consider moving this into a Co-routine...
        // [GAME LOOP] Notify the user using Dialogue Windows...
        do {
            currentPlayerIndex = (currentPlayerIndex + 1) % thisTurnArray.size
            result = thisTurnArray[currentPlayerIndex]
            if (!result.team.stillHasAlivePhoenixes()) {
                thisTurnArray.remove(result)
                result.onGameOver()
                if (this.thisTurnArray.isEmpty()) gameLoop.declareDraw()
                else if (this.thisTurnArray.size == 1) gameLoop.declareWinner(this.thisTurnArray[0])
            }
        } while (!result.isValidForTurn())
        gameLoop.updatePlayerTurnState(result)
        gameLoop.viewModel.recompilePhoenixes()
        result.onTurnStart()
        return result
    }

    fun endRoundAndNextPlayerTurn() {
        val player = thisTurnArray[currentPlayerIndex]
        player.finishRound()
        thisTurnArray.remove(player)
        nextTurnArray.add(player)
        if (thisTurnArray.isEmpty()) return nextRound()
        nextPlayerTurn()
    }

    private fun nextRound() {
        thisTurnArray.clear()
        thisTurnArray.addAll(nextTurnArray)
        nextTurnArray.clear()
        currentPlayerIndex = 0
        thisTurnArray.forEach {
            it.startRound()
        }
        this.gameLoop.viewModel.startRoundDialog.value = true
    }
}
