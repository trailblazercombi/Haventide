package app.trailblazercombi.haventide.game.arena

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.max
import androidx.lifecycle.ViewModel
import app.trailblazercombi.haventide.game.abilities.AbilityTemplate
import app.trailblazercombi.haventide.game.abilities.ComposableDiceStack
import app.trailblazercombi.haventide.game.abilities.Die
import app.trailblazercombi.haventide.game.mechanisms.PhoenixMechanism
import app.trailblazercombi.haventide.game.mechanisms.PhoenixMiniature
import app.trailblazercombi.haventide.resources.*
import app.trailblazercombi.haventide.resources.GameScreenTopBubbleStyle.StandardButtonWidth
import kotlinx.coroutines.flow.MutableStateFlow
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

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

    init {
        turnTable.initialize(player1, player2)
    }

    // The Tile Map and loader
    internal val tileMap = TileMapData(turnTable, this)
    // [MAPS] TODO Add the TileMapData file loader, and positions, and stuff...

    val viewModel: GameLoopViewModel = GameLoopViewModel(this)

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
    }
}

/**
 * The ViewModel for the game.
 */
class GameLoopViewModel(gameLoop: GameLoop) : ViewModel() {
    val roundCount = MutableStateFlow(0)
    val gameLoopState = MutableStateFlow(gameLoop)

    val gameOverDialog = MutableStateFlow(false)
    val gameOverDialogResult = MutableStateFlow(GameResult.UNKNOWN)
    val forfeitAreYouSureDialog = MutableStateFlow(false)
    val pauseMenuDialog = MutableStateFlow(false)

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

    val screenWidth = mutableStateOf((-1).dp)
    val screenHeight = mutableStateOf((-1).dp)

    fun previewMoveOnDiceStack(doer: PhoenixMechanism, ability: AbilityTemplate) {
        localPlayerDice.value.viewModel.autoSelectDice(
            doer.template.phoenixType, ability.alignedCost, ability.scatteredCost
        )
    }
}

enum class GameResult(val string: StringResource, val color: Color) {
    VICTORY(Res.string.game_over_dialog_result_good, Palette.FillGreen),
    VICTORY_FORFEIT(Res.string.game_over_dialog_result_contumation, Palette.FillGreen),
    DEFEAT(Res.string.game_over_dialog_result_bad, Palette.FillRed),
    DEFEAT_FORFEIT(Res.string.game_over_dialog_result_forfeit, Palette.FillRed),
    DRAW(Res.string.game_over_dialog_result_neutral, Palette.FullGrey),
    UNKNOWN(Res.string.game_over_dialog_result_error, Palette.FillYellow)
}

enum class PlayerTurnStates(val string: StringResource) {
    THEIR_TURN(Res.string.game_turn_state_good),
    NOT_THEIR_TURN(Res.string.game_turn_state_bad),
    ROUND_FINISHED(Res.string.game_turn_state_over);

    private var player: String = "Unnamed"

    fun consume(player: PlayerInGame) {
        this.player = player.profile.name
    }

    fun retrieve(): String = this.player
}

@Composable
fun ComposableGameScreen(viewModel: GameLoopViewModel, modifier: Modifier = Modifier) {
    val viewModelState = viewModel.gameLoopState.collectAsState()

    // Find the current screen size...
    ScreenSizeFinder(viewModel.screenWidth, viewModel.screenHeight)

    // The contents of the game screen
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        ComposableTileMap(viewModelState.value.tileMap)
        GameStatusBar(viewModel)
        DiceInfoPanel(viewModel)
    }

    // The dialogs
    GameOverDialog(viewModel)
    PauseMenuDialog(viewModel)
    YesNoDialog(
        viewModel = viewModel,
        openDialogState = viewModel.forfeitAreYouSureDialog,
        title = Res.string.yes_no_dialog_forfeit_title,
        acceptLabel = Res.string.yes_no_dialog_forfeit_yes,
        declineLabel = Res.string.yes_no_dialog_forfeit_no,
        onAccept = {
            viewModel.forfeitAreYouSureDialog.value = false
            viewModel.pauseMenuDialog.value = false
            viewModel.gameLoopState.value.forfeitMatch(viewModel.gameLoopState.value.localPlayer())
        },
        onDecline = { viewModel.forfeitAreYouSureDialog.value = false },
        acceptSeverity = ButtonSeverity.DESTRUCTIVE,
        declineSeverity = ButtonSeverity.NEUTRAL,
    )

    // Once all is done, start the game - TODO Make it better
    viewModel.gameLoopState.value.localPlayer().startRound()
}

@Composable
fun DiceInfoPanel(viewModel: GameLoopViewModel, modifier: Modifier = Modifier) {
    val backdropColor by viewModel.backdropColor.collectAsState()

    Box (
        contentAlignment = Alignment.BottomStart,
        modifier = modifier.fillMaxSize()
    ) {
        Surface(
            color = Palette.Glass20.compositeOver(backdropColor),
            border = BorderStroke(
                GameScreenTopBubbleStyle.OutlineThickness,
                Palette.Glass40.compositeOver(backdropColor)
            ),
            modifier = modifier,
        ) {
            ComposableDiceStack(viewModel.localPlayerDice.value.viewModel)
        }
    }
}

@Composable
fun EndRoundDialog(viewModel: GameLoopViewModel, modifier: Modifier = Modifier) {
    val renderDice by viewModel.localPlayerDice.collectAsState()

    // TODO
}

@Composable
fun StartRoundDialog(viewModel: GameLoopViewModel, modifier: Modifier = Modifier) {
    val renderDice by viewModel.localPlayerDice.collectAsState()

    // TODO
}

@Composable
fun GameStatusBar(viewModel: GameLoopViewModel, modifier: Modifier = Modifier) {
    val roundCount by viewModel.roundCount.collectAsState()
    val backdropColor by viewModel.backdropColor.collectAsState()

    val localPlayerTurn by viewModel.localPlayerTurn.collectAsState()
    val allies by viewModel.alliedPhoenixEntries.collectAsState()
    val enemies by viewModel.enemyPhoenixEntries.collectAsState()

    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = modifier
            .padding(
                if (viewModel.screenWidth.value > 576.dp) GameScreenTopBubbleStyle.OffsetFromEdge
                else 0.dp
            )
            .fillMaxSize()
    ) {
        Surface(
            shape = if (viewModel.screenWidth.value > 576.dp) RoundedCornerShape(
                GameScreenTopBubbleStyle.CornerRounding
            ) else RoundedCornerShape(0.dp),
            color = Palette.Glass10.compositeOver(backdropColor),
            contentColor = Palette.FullWhite,
            border = BorderStroke(
                GameScreenTopBubbleStyle.OutlineThickness,
                Palette.Glass30.compositeOver(backdropColor)
            ),
            elevation = GameScreenTopBubbleStyle.Elevation,
            modifier = modifier
                .clickable(
                    indication = LocalIndication.current,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = { viewModel.pauseMenuDialog.value = true }
                )
                .intrinsicWidth()
                .height(
                    if (viewModel.screenWidth.value > 532.dp) GameScreenTopBubbleStyle.BubbleHeight
                    else GameScreenTopBubbleStyle.DoubleBubbleHeight,
                )
        ) {
            Box( // ALLIES
                contentAlignment = Alignment.BottomStart,
                modifier = modifier
                    .fillMaxHeight()
                    .padding(0.dp, GameScreenTopBubbleStyle.InnerOffset)
            ) {
                PhoenixMiniatureStrip(allies, viewModel)
            }
            Box( // ENEMIES
                contentAlignment = Alignment.BottomEnd,
                modifier = modifier
                    .fillMaxHeight()
                    .padding(0.dp, GameScreenTopBubbleStyle.InnerOffset)
            ) {
                PhoenixMiniatureStrip(enemies, viewModel)
            }
            // HERE STARTS THE ROUND COUNTER / TEAM TO MOVE DISPLAY
            if (viewModel.screenWidth.value > 532.dp) {
                Column(
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = stringResource(Res.string.game_turn_state_round_counter, roundCount),
                        textAlign = TextAlign.Center,
                        fontSize = GameScreenTopBubbleStyle.RoundCounterTextSize,
                        lineHeight = GameScreenTopBubbleStyle.RoundCounterTextSize,
                        color = Palette.FullWhite,
                        modifier = modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(
                                GameScreenTopBubbleStyle.InnerOffset,
                                GameScreenTopBubbleStyle.InnerOffset / 2,
                                GameScreenTopBubbleStyle.InnerOffset,
                                0.dp
                            )
                    )
                    Text(
                        text = stringResource(localPlayerTurn.string, localPlayerTurn.retrieve()),
                        textAlign = TextAlign.Center,
                        fontSize = GameScreenTopBubbleStyle.TeamTurnTextSize,
                        lineHeight = GameScreenTopBubbleStyle.TeamTurnTextSize,
                        color = Palette.FullWhite,
                        modifier = modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(
                                start = GameScreenTopBubbleStyle.InnerOffset,
                                top = 0.dp,
                                end = GameScreenTopBubbleStyle.InnerOffset,
                                bottom = GameScreenTopBubbleStyle.InnerOffset / 2,
                            )
                    )
                }
            } else {
                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = stringResource(Res.string.game_turn_state_round_counter, roundCount) +
                                " / ${stringResource(localPlayerTurn.string, localPlayerTurn.retrieve())}",
                        textAlign = TextAlign.Center,
                        fontSize = GameScreenTopBubbleStyle.UnifiedRoundTeamTextSize,
                        lineHeight = GameScreenTopBubbleStyle.UnifiedRoundTeamTextSize,
                        color = Palette.FullWhite,
                        modifier = modifier
                            .padding(0.dp, GameScreenTopBubbleStyle.InnerOffset)
                    )
                }
            }
        }
    }
}

@Composable
private fun PhoenixMiniatureStrip(
    allies: List<PhoenixMechanism>,
    viewModel: GameLoopViewModel
) {
    Row {
        allies.forEach {
            PhoenixMiniature(
                it,
                min(
                    a = GameScreenTopBubbleStyle.MiniatureWidth,
                    b = max(
                        a = (viewModel.screenWidth.value
                                - (GameScreenTopBubbleStyle.OffsetFromEdge * 2)
                                - (GameScreenTopBubbleStyle.InnerOffset * 5)
                                ) / 6,
                        b = GameScreenTopBubbleStyle.MiniatureHeight
                                + (GameScreenTopBubbleStyle.InnerOffset * 2)
                        // Make sure they're always at least a square
                    )
                ),
                if (viewModel.screenWidth.value > 532.dp) GameScreenTopBubbleStyle.BubbleHeight - (GameScreenTopBubbleStyle.InnerOffset * 2)
                else GameScreenTopBubbleStyle.MiniatureHeight
            )
        }
    }
}

@Composable
private fun Modifier.intrinsicWidth(): Modifier {
    return this.width(
        GameScreenTopBubbleStyle.InnerOffset * 4 * 2
                + GameScreenTopBubbleStyle.MiniatureWidth * 3 * 2
                + GameScreenTopBubbleStyle.RoundCounterWidth
    )
}

@Composable
fun GameOverDialog(viewModel: GameLoopViewModel, modifier: Modifier = Modifier) {
    // val openDialog by viewModel.gameOverDialog.collectAsState()
    val openDialog by viewModel.gameOverDialog.collectAsState()
    val openDialogResult by viewModel.gameOverDialogResult.collectAsState()

    DialogGenerics(
        openDialogState = viewModel.gameOverDialog,
        onDismissRequest = {
            viewModel.gameOverDialog.value = false
            // [NAVHOST] TODO Navigate to summary screen
        },
    )

    AnimatedVisibility(openDialog, enter = scaleIn(), exit = scaleOut()) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .fillMaxSize()
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = modifier
            ) {
                Surface(
                    color = Palette.Abyss90.compositeOver(openDialogResult.color),
                    shape = RoundedCornerShape(GameScreenDialogBoxStyle.OuterCornerRounding),
                    contentColor = Palette.FullWhite,
                    border = BorderStroke(GameScreenDialogBoxStyle.OutlineThickness, openDialogResult.color),
                    elevation = GameScreenDialogBoxStyle.Elevation,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(
                            if (viewModel.screenWidth.value > 340.dp)
                                GameScreenDialogBoxStyle.StretchedDialogOffsetFromEdge
                            else GameScreenDialogBoxStyle.GameOverInnerPadding
                        )
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = modifier
                            .padding(GameScreenDialogBoxStyle.GameOverInnerPadding),
                    ) {
                        Text(
                            text = stringResource(resource = openDialogResult.string),
                            color = Palette.FullWhite,
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = GameScreenDialogBoxStyle.LargeTextSize,
                                lineHeight = GameScreenDialogBoxStyle.LargeTextLineSeparation
                            ),
                            textAlign = TextAlign.Center,
                            modifier = modifier
                                .fillMaxWidth()
                        )
                    }
                }
            }
        }
    }

    AnimatedVisibility(openDialog, enter = fadeIn(), exit = fadeOut()) {
        Box (modifier.fillMaxSize()) {
            Text(
                text = stringResource(resource = Res.string.game_over_dialog_confirm_button),
                textAlign = TextAlign.Center,
                color = Palette.FullGrey,
                modifier = modifier
                    .align(Alignment.BottomCenter)
                    .padding(0.dp, GameScreenDialogBoxStyle.TapAnywhereLabelOffset)
            )
        }
    }
}

@Composable
fun PauseMenuDialog(viewModel: GameLoopViewModel, modifier: Modifier = Modifier) {
    val openDialog by viewModel.pauseMenuDialog.collectAsState()

    DialogGenerics(
        openDialogState = viewModel.pauseMenuDialog,
        onDismissRequest = { viewModel.pauseMenuDialog.value = false },
        modifier = modifier
    )

    AnimatedVisibility(openDialog, enter = slideInVertically(), exit = slideOutVertically()) {
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Surface(
                color = Palette.Abyss40.compositeOver(viewModel.backdropColor.value),
                shape = RoundedCornerShape(GameScreenDialogBoxStyle.OuterCornerRounding),
                contentColor = Palette.FullWhite,
                border = BorderStroke(
                    GameScreenDialogBoxStyle.OutlineThickness,
                    Palette.Abyss10.compositeOver(viewModel.backdropColor.value),
                ),
                elevation = GameScreenDialogBoxStyle.Elevation,
                modifier = modifier.padding(
                    top = GameScreenTopBubbleStyle.BubbleHeight + GameScreenTopBubbleStyle.OffsetFromEdge * 2
                )
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = modifier
                ) {
                    Column(
                        modifier = modifier
                            .width(StandardButtonWidth)
                            .padding(GameScreenDialogBoxStyle.InnerPadding),
                    ) {
                        /* RETURN   */ MenuButton({ viewModel.pauseMenuDialog.value = false },
                                           Res.string.pause_dialog_return_button, ButtonSeverity.NEUTRAL)
                        /* DRAW     */ MenuButton({ /* TODO */ },
                                           Res.string.pause_dialog_offer_draw_button, ButtonSeverity.NEUTRAL)
                        /* SETTINGS */ MenuButton({ /* TODO */ },
                                           Res.string.pause_dialog_settings_shortcut_button, ButtonSeverity.NEUTRAL)
                        /* FORFEIT  */ MenuButton(onClick = { viewModel.forfeitAreYouSureDialog.value = true },
                                           Res.string.pause_dialog_forfeit_button, ButtonSeverity.DESTRUCTIVE_MINOR)
                    }
                }
            }
        }
    }
}

@Composable
private fun DialogGenerics(
    openDialogState: MutableStateFlow<Boolean>,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    val openDialog by openDialogState.collectAsState()

    DismissHandling { onDismissRequest() }
    AnimatedVisibility(openDialog, enter = fadeIn(), exit = fadeOut()) {
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = modifier
                .fillMaxSize()
                .background(Palette.Abyss50)
                .clickable {
                    onDismissRequest()
                }
        ) {}
    }
}

@Composable
private fun MenuButton(
    onClick: () -> Unit,
    label: StringResource,
    severity: ButtonSeverity,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        enabled = true,
        shape = RoundedCornerShape(GameScreenDialogBoxStyle.ButtonCornerRounding),
        border = BorderStroke(
            GameScreenDialogBoxStyle.OutlineThickness,
            severity.outlineColor
        ),
        colors = ButtonDefaults.textButtonColors(
            backgroundColor = severity.fillColor,
            contentColor = severity.labelColor,
        ),
        contentPadding = PaddingValues(GameScreenDialogBoxStyle.OutlineThickness),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(label),
            fontSize = GameScreenDialogBoxStyle.ButtonTextSize,
            textAlign = TextAlign.Center,
            modifier = modifier
        )
    }
}


@Composable
fun YesNoDialog(
    viewModel: GameLoopViewModel,
    openDialogState: MutableStateFlow<Boolean>,
    title: StringResource,
    acceptLabel: StringResource,
    declineLabel: StringResource,
    onAccept: () -> Unit,
    onDecline: () -> Unit,
    acceptSeverity: ButtonSeverity,
    declineSeverity: ButtonSeverity,
    modifier: Modifier = Modifier
) {
    val openDialog by openDialogState.collectAsState()

    DialogGenerics(
        openDialogState = openDialogState,
        onDismissRequest = { openDialogState.value = false },
        modifier = modifier
    )

    AnimatedVisibility(openDialog, enter = scaleIn(), exit = scaleOut()) {
        Box (
            contentAlignment = Alignment.Center,
            modifier = modifier.fillMaxSize()
        ) {
            Surface(
                color = Palette.Abyss90.compositeOver(Palette.Abyss60),
                shape = RoundedCornerShape(GameScreenDialogBoxStyle.OuterCornerRounding),
                contentColor = Palette.FullWhite,
                border = BorderStroke(
                    GameScreenDialogBoxStyle.OutlineThickness,
                    Palette.Abyss90.compositeOver(Palette.FillLightPrimary)
                ),
                elevation = GameScreenDialogBoxStyle.Elevation,
                modifier = modifier.padding(GameScreenDialogBoxStyle.StretchedDialogOffsetFromEdge, 0.dp)
            ) {
                Column (horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(title),
                        textAlign = TextAlign.Center,
                        fontSize = GameScreenDialogBoxStyle.TitleTextSize,
                        modifier = modifier
                            .padding(GameScreenDialogBoxStyle.InnerPadding)
                    )
                    if (viewModel.screenWidth.value > 528.dp) {
                        Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                            RowMenuButton(
                                viewModel, onDecline, declineSeverity, declineLabel,
                                GameScreenDialogBoxStyle.YesNoDialogButtonMaxWidth
                            )
                            RowMenuButton(
                                viewModel, onAccept, acceptSeverity, acceptLabel,
                                GameScreenDialogBoxStyle.YesNoDialogButtonMaxWidth
                            )
                        }
                    } else {
                        Column (
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = modifier.padding(GameScreenDialogBoxStyle.InnerPadding)
                        ) {
                            MenuButton(onAccept, acceptLabel, acceptSeverity)
                            MenuButton(onDecline, declineLabel, declineSeverity)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RowMenuButton(
    viewModel: GameLoopViewModel,
    onClick: () -> Unit,
    severity: ButtonSeverity,
    label: StringResource,
    width: Dp,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        border = BorderStroke(GameScreenDialogBoxStyle.OutlineThickness, severity.outlineColor),
        colors = ButtonDefaults.textButtonColors(
            backgroundColor = severity.fillColor,
            contentColor = severity.labelColor
        ),
        contentPadding = PaddingValues(GameScreenDialogBoxStyle.InnerPadding),
        modifier = modifier
            .padding(GameScreenDialogBoxStyle.InnerPadding)
            .width(
                min(a = (viewModel.screenWidth.value) / 3,
                    b = width)
            )
    ) {
        Text(stringResource(label))
    }
}

@Composable
fun ScreenSizeFinder(screenWidth: MutableState<Dp>, screenHeight: MutableState<Dp>) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        screenWidth.value = this.maxWidth
        screenHeight.value = this.maxHeight
        println("${screenWidth.value} x ${screenHeight.value}") // FIXME Remove println
    }
}

@Composable
expect fun DismissHandling(onDismiss: () -> Unit)

enum class ButtonSeverity(val fillColor: Color, val outlineColor: Color, val labelColor: Color) {
    NEUTRAL(Palette.Glass00, Palette.FillLightPrimary, Palette.FillLightPrimary),
    PREFERRED_MINOR(Palette.Glass00, Palette.FillYellow, Palette.FillYellow),
    PREFERRED(Palette.FillYellow, Palette.FillYellow, Palette.FullBlack),
    DESTRUCTIVE_MINOR(Palette.Glass00, Palette.FillRed, Palette.FillRed),
    DESTRUCTIVE(Palette.FillRed, Palette.FillRed, Palette.FullWhite)
}
