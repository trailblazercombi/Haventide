package app.trailblazercombi.haventide.game.arena

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * This defines the entire Game Loop.
 * Yes, the entire thing.
 * Yes, from the start to the end.
 */
class GameLoop(profile1: PlayerProfile, profile2: PlayerProfile) {
    // [MAPS] TODO Add the TileMapData file loader, and positions, and stuff...
    var roundNumber = mutableStateOf(0)

    private val player1: PlayerInGame = profile1.toPlayerInGame()
    private val player2: PlayerInGame = profile2.toPlayerInGame()

    private val turnTable = TurnTable(mutableListOf(player1, player2))
    internal val tileMap = TileMapData(turnTable, this)

    fun toViewModel() = GameLoopViewModel(this)

    fun localPlayer() = player1 // TODO Fix this to work properly once multiplayer is involved...
}

class TurnTable(private var thisTurnArray: MutableList<PlayerInGame>) {
    private val nextTurnArray: MutableList<PlayerInGame> = mutableListOf()
    private var currentPlayerIndex = 0

    fun currentPlayer(): PlayerInGame = thisTurnArray[currentPlayerIndex]

    fun allPlayers(): List<PlayerInGame> = thisTurnArray.toList()

    fun nextPlayerTurn(): PlayerInGame {
        var result: PlayerInGame
        // [COROUTINES] TODO Consider moving this into a Co-routine...
        // [GAME LOOP] Notify the user using Dialogue Windows...
        do {
            currentPlayerIndex = (currentPlayerIndex + 1) % thisTurnArray.size
            result = thisTurnArray[currentPlayerIndex]
        } while (!result.isValidForTurn())
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
        thisTurnArray = nextTurnArray
        nextTurnArray.clear()
        currentPlayerIndex = 0
        thisTurnArray.forEach {
            it.startRound()
        }
    }

    init {
        thisTurnArray.forEach {
            it.startRound()
        }
    }
}

/**
 * The ViewModel for the game.
 */
class GameLoopViewModel(gameLoop: GameLoop): ViewModel() {
    val gameLoopState = MutableStateFlow(gameLoop)
}

@Composable
fun ComposableGameScreen(viewModel: GameLoopViewModel, modifier: Modifier = Modifier) {
    val viewModelState = viewModel.gameLoopState.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        ComposableTileMap(viewModelState.value.tileMap)
    }
}
