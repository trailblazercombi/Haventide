package app.trailblazercombi.haventide.game2.viewModel

import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import app.trailblazercombi.haventide.game.abilities.AbilityTemplate
import app.trailblazercombi.haventide.game.mechanisms.PhoenixMechanism
import app.trailblazercombi.haventide.resources.GameResult
import app.trailblazercombi.haventide.resources.PlayerTurnStates
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * The ViewModel for the game.
 */
class GameLoopViewModel(private val gameLoop: GameLoop) : ViewModel() {
    val roundCount = MutableStateFlow(0)
    val gameLoopState = MutableStateFlow(gameLoop)
    val gameOverDialog = MutableStateFlow(false)

    val gameOverDialogResult = MutableStateFlow(GameResult.UNKNOWN)
    val forfeitAreYouSureDialog = MutableStateFlow(false)
    val pauseMenuDialog = MutableStateFlow(false)
    val startRoundDialog = MutableStateFlow(true)
    val endRoundDialog = MutableStateFlow(false)

    val localPlayerDice = MutableStateFlow(gameLoop.localPlayer().dice)

    val backdropColor = MutableStateFlow(gameLoop.tileMap.backdropColor)
    val turnTableState = MutableStateFlow(gameLoop.turnTable)
    val localPlayerTurn = MutableStateFlow(PlayerTurnStates.NOT_THEIR_TURN)
    val alliedPhoenixEntries = MutableStateFlow(gameLoop.compileAlliedPhoenixes())

    val enemyPhoenixEntries = MutableStateFlow(gameLoop.compileEnemyPhoenixes())

    fun recompilePhoenixes() {
        alliedPhoenixEntries.value = gameLoopState.value.compileAlliedPhoenixes()
        enemyPhoenixEntries.value = gameLoopState.value.compileEnemyPhoenixes()
    }

    val screenWidth = MutableStateFlow((-1).dp)
    val screenHeight = MutableStateFlow((-1).dp)

    fun previewMoveOnDiceStack(doer: PhoenixMechanism, ability: AbilityTemplate) {
        localPlayerDice.value.viewModel.autoSelectDice(
            doer.template.phoenixType, ability.alignedCost, ability.scatteredCost
        )
    }

    fun endLocalPlayerRound() {
        gameLoop.turnTable.endRoundAndNextPlayerTurn()
        gameLoop.tileMap.viewModel.updateAvailableTiles()
    }
}
