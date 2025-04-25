package app.trailblazercombi.haventide.game2.viewModel

import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import app.trailblazercombi.haventide.game2.data.GameLoop
import app.trailblazercombi.haventide.game2.data.tilemap.TileData
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.Mechanism
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.PhoenixMechanism
import app.trailblazercombi.haventide.game2.data.turntable.Die
import app.trailblazercombi.haventide.netcode.TcpClient
import app.trailblazercombi.haventide.resources.*
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * This class handles EVERYTHING that happens on the screen while in-game.
 *
 * There is no delegation to the data layer. PERIOD.
 */
class GameLoopViewModel(
    private val gameLoop: GameLoop,
) : ViewModel(), ScreenSizeProvider {
///////////////////////////////////////////////////////////////////////
// REFERENCES TO PASS AROUND
    val mapData = gameLoop.tileMap
    private val turnTable = gameLoop.turnTable

    override val screenWidth = MutableStateFlow(0.dp)
    override val screenHeight = MutableStateFlow(0.dp)

///////////////////////////////////////////////////////////////////////
// DIALOGS OPEN MANAGEMENT
    val gameOverDialog = MutableStateFlow(false)
    fun showGameOverDialog() { gameOverDialog.value = true }

    val pauseMenuDialog = MutableStateFlow(false)
    fun showPauseMenuDialog() { pauseMenuDialog.value = true }
    fun hidePauseMenuDialog() { pauseMenuDialog.value = false }

    val forfeitConfirmationDialog = MutableStateFlow(false)
    fun showForfeitConfirmationDialog() { forfeitConfirmationDialog.value = true }
    fun hideForfeitConfirmationDialog() { forfeitConfirmationDialog.value = false }

    val endRoundDialog = MutableStateFlow(false)
    fun showEndRoundDialog() { endRoundDialog.value = true }
    fun hideEndRoundDialog() { endRoundDialog.value = false }

///////////////////////////////////////////////////////////////////////
// THE GAME'S RESULT -- pulled direct from GameLoop...
    val gameResult = MutableStateFlow(gameLoop.gameResult)
    val gameIsOver = MutableStateFlow(gameLoop.gameIsOver)

///////////////////////////////////////////////////////////////////////
// TOP BUBBLE STATE MANAGEMENT
    val currentPlayer = MutableStateFlow(turnTable.currentPlayer())

    val roundNumber = MutableStateFlow(turnTable.roundCount)
    val localPlayerTurn = MutableStateFlow(false)
    val localPlayerRoundOver = MutableStateFlow(false)

    val alliedPhoenixes = MutableStateFlow(gameLoop.compileAlliedPhoenixes())
    val enemyPhoenixes = MutableStateFlow(gameLoop.compileEnemyPhoenixes())

///////////////////////////////////////////////////////////////////////
// TILE STATE MANAGEMENT
    private val tileStates: TileStateMap = generateTileStates(gameLoop)

    val abilityPreview: MutableStateFlow<AbilityPreview?> = MutableStateFlow(null)

    private var tileSelected1: TileData? = null
        set(value) {
            field = value
            if (value != null) highlightAlignedDice(tileSelected1!!.getPhoenix()!!.template.phoenixType)
            else localDiceStates.resetAligned()
        }
    private var tileSelected2: TileData? = null
    private var tileSelected3: TileData? = null

    private var tilesAvailable1: Set<TileData> = setOf()
    private var tilesAvailable2: Set<TileData> = setOf()

    /**
     * @return The Click State for the specified [TileData] as [MutableStateFlow].
     */
    fun tileClickStateFor(tile: TileData) = tileStates.getClick(tile)

    /**
     * @return The Highlight State for the specified [TileData] as [MutableStateFlow].
     */
    fun tileHighlightStateFor(tile: TileData) = tileStates.getHighlight(tile)

    /**
     * Invoke upon user clicking a tile.
     */
    fun processTileClickEvent(tile: TileData) {
        tileSelected3 = null

        if (currentPlayer.value !== gameLoop.localPlayer) {
            tileSelected3 = tile
            resetAbilityPreview()
        } else if (abilityPreview.value != null && tile == tileSelected2) { // If a move is ready and you click the white tile again
            if (!processAbilityInvokeEvent()) return
            processEndMoveEvent()

            tileSelected1 = null
            tileSelected2 = null
            tileSelected3 = null
            resetAbilityPreview()
            localDiceStates.resetAligned()
        } else if (tileSelected1 != null) { // If a yellow tile is selected...
            when (tile) {
                tileSelected1 -> { // ...and if you click that tile, deselect it
                    tileSelected1 = null
                    tileSelected2 = null
                    resetAbilityPreview()
                } in tilesAvailable2 -> { // ...and if you click a white outlined tile, select it + preview first move
                    tileSelected2 = tile
                    createAbilityPreview()
                } in tilesAvailable1 -> { // ...and if you click another yellow outlined tile, change the selection
                    tileSelected2 = null
                    tileSelected1 = tile
                    resetAbilityPreview()
                } else -> { // last resort: get rid of everything and select an unrelated grey tile
                    tileSelected1 = null
                    tileSelected2 = null
                    tileSelected3 = tile
                    resetAbilityPreview()
                }
            }
        } else { // If no yellow tile is selected...
            if (tile in tilesAvailable1) { // ...and if you click a yellow outlined tile, select it
                tileSelected2 = null
                tileSelected1 = tile
                resetAbilityPreview()
                highlightAlignedDice(tileSelected1!!.getPhoenix()!!.template.phoenixType)
            } else { // last resort: get rid of everything and select an unrelated grey tile
                tileSelected1 = null
                tileSelected2 = null
                tileSelected3 = tile
                resetAbilityPreview()
            }
        }

        updateTileHighlights()
    }

    private fun createAbilityPreview() {
        tileSelected1!!.getPhoenix()?.let { abilityPreview.value = getMovePreview(it, tileSelected2!!) }
        autoSelectDice()
    }

    private fun resetAbilityPreview() {
        abilityPreview.value = null
        this.deselectAllDice()
    }

    internal fun updateTileHighlights() {
        resetTileHighlights()
        if (localPlayerTurn.value) {
            tileSelected3?.let { tileStates.getClick(it)?.value = UniversalColorizer.CLICKED_TERTIARY }
            return
        }

        // Deal with selected tile highlights
        tileSelected1?.let { tileStates.getClick(it)?.value = UniversalColorizer.CLICKED_PRIMARY }
        tileSelected2?.let { tileStates.getClick(it)?.value = UniversalColorizer.CLICKED_SECONDARY }
        tileSelected3?.let { tileStates.getClick(it)?.value = UniversalColorizer.CLICKED_TERTIARY }

        // Mark available tiles
        tilesAvailable1 = findAvailableTiles1()
        tilesAvailable2 = findAvailableTiles2()

        // Deal with tile highlights
        tilesAvailable1.forEach { tileStates.getHighlight(it)?.value = UniversalColorizer.HIGHLIGHT_PRIMARY }
        tilesAvailable2.forEach { tileStates.getHighlight(it)?.value = UniversalColorizer.HIGHLIGHT_SECONDARY }
    }

    private fun resetTileHighlights() {
        tilesAvailable1 = emptySet()
        tilesAvailable2 = emptySet()

        tileSelected1?.let { tileStates.getClick(it)?.value = UniversalColorizer.NO_INTERACTIONS_WITH_OUTLINE }
        tileSelected2?.let { tileStates.getClick(it)?.value = UniversalColorizer.NO_INTERACTIONS_WITH_OUTLINE }
        tileSelected3?.let { tileStates.getClick(it)?.value = UniversalColorizer.NO_INTERACTIONS_WITH_OUTLINE }

        tileStates.unpack().values.forEach {
            it.first.value = UniversalColorizer.NO_INTERACTIONS_WITH_OUTLINE
            it.second.value = UniversalColorizer.NO_INTERACTIONS
        }
    }

    private fun findAvailableTiles1(): Set<TileData> {
        if (!localPlayerTurn.value) return emptySet()

        val result = mutableSetOf<TileData>()
        gameLoop.localPlayer.compilePhoenixes().forEach { phoenix -> phoenix.parentTile.let { result.add(it) }
        }
        return result.toSet()
    }

    private fun findAvailableTiles2(): Set<TileData> {
        if (!localPlayerTurn.value) return emptySet()
        if (tileSelected1 == null) return emptySet()
        val localPhoenix = tileSelected1?.getPhoenix() ?: return emptySet()

        val result = mutableSetOf<TileData>()
        abilitySurroundings(tileSelected1!!).forEach {
            position -> mapData[position].let {
                tile -> if (tile?.let { getMovePreview(localPhoenix, tile) } != null) result.add(tile)
            }
        }
        return result.toSet()
    }

    private fun getMovePreview(abilityTemplate: AbilityTemplate, doer: Mechanism, target: TileData): AbilityPreview {
        return AbilityPreview(abilityTemplate, doer, target, mutableListOf())
    }

    private fun getMovePreview(doer: PhoenixMechanism, target: TileData): AbilityPreview? {
        return doer.findFirstAvailableAbility(target)?.let { getMovePreview(it, doer, target) }
    }

///////////////////////////////////////////////////////////////////////
// TURN TABLE FLOW EVENTS

    /**
     * Invoke upon user finishing a move.
     */
    private fun processEndMoveEvent() {
        gameLoop.checkGameResult()
        turnTable.nextPlayerTurn()
        updateTileHighlights()

        localPlayerTurn.value = turnTable.currentPlayer() === gameLoop.localPlayer
    }

    /**
     * Invoke upon user deciding to end the round.
     */
    fun processEndRoundEvent() {
        println("Processing end round event! ${gameLoop.localPlayer}")
        if (turnTable.currentPlayer() === gameLoop.localPlayer)
            localPlayerRoundOver.value = true

        gameLoop.checkGameResult()
        turnTable.endRoundAndNextPlayerTurn()
        updateTileHighlights()
    }
    /**
     * Invoke upon new round starting.
     */
    fun processStartRoundEvent() {
        println("Starting round! The first player is ${turnTable.currentPlayer()}")
        localPlayerRoundOver.value = false
        localPlayerTurn.value = turnTable.currentPlayer() == gameLoop.localPlayer
    }

    /**
     * Invoke upon an Ability played.
     */
    private fun processAbilityInvokeEvent(): Boolean {
        if (abilityPreview.value == null) return false
        if (countedDiceMatch()) {
            currentPlayer.value.executeAbility(abilityPreview.value!!)
            return true
        } else return false
    }

    /**
     * Invoke upon local user accepting to FORFEIT the match.
     */
    fun processForfeitMatchEvent() {
        gameLoop.processForfeitMatchEvent()
    }

    /**
     * External processor for invoking abilities.
     */
    fun processExternalAbilityExecution() {
        if (!processAbilityInvokeEvent()) return
        processEndMoveEvent()

        tileSelected1 = null
        tileSelected2 = null
        tileSelected3 = null
        resetAbilityPreview()
        localDiceStates.resetAligned()

        updateTileHighlights()
    }

///////////////////////////////////////////////////////////////////////
// DICE ECONOMY -- only for local player!!!
    val localDiceList = MutableStateFlow(emptyList<Die>())
    private var localDiceStates = DiceStateMap(emptyList())

    val alignedSelectedDiceCount = MutableStateFlow(0)
    val scatteredSelectedDiceCount = MutableStateFlow(0)

    private fun updateDiceCountStates() {
        alignedSelectedDiceCount.value = localDiceStates.countAlignedSelected()
        scatteredSelectedDiceCount.value = localDiceStates.countScatteredSelected()
    }

    /**
     * The final stop when pushing dice updates.
     */
    fun pushDiceChanges(newList: List<Die>) {
        localDiceList.value = newList
        localDiceStates = DiceStateMap(newList)
    }

    /**
     * Highlight all aligned dice of the specified type.
     */
    private fun highlightAlignedDice(type: DieType) = localDiceStates.setAligned(type)

    /**
     * Deselect all dice.
     */
    private fun deselectAllDice() {
        localDiceStates.resetSelected()
        updateDiceCountStates()
    }

    private fun updateAbilityPreviewDiceConsumption() {
        if (abilityPreview.value == null) return
        abilityPreview.value!!.updateConsume(localDiceStates.getAllSelectedDice())
    }

    /**
     * Exposes [DiceStateMap.selectedStateOf]
     */
    fun selectedStateOf(die: Die) = localDiceStates.selectedStateOf(die)

    /**
     * Exposes [DiceStateMap.alignedStateOf]
     */
    fun alignedStateOf(die: Die) = localDiceStates.alignedStateOf(die)

    /**
     * Invoke when the user clicks on a Die.
     */
    fun processDieClickEvent(die: Die) {
        if (abilityPreview.value == null) return
        selectedStateOf(die)!!.value = !(selectedStateOf(die)!!.value)
        updateAbilityPreviewDiceConsumption()
        updateDiceCountStates()
    }

    /**
     * Calculate the currently selected dice as `aligned to scattered`.
     */
    private fun getActualDiceCounts(): Pair<Int, Int> {
        if (abilityPreview.value == null) return 0 to 0
        val aligned = localDiceStates.countAlignedSelected()
        val scattered = localDiceStates.countScatteredSelected()
        return aligned to scattered // aligned + scattered = totalSelected
    }

    /**
     * Calculate the dice required to process an ability as `aligned to scattered`.
     */
    private fun getRequiredDiceCounts(): Pair<Int, Int> {
        if (abilityPreview.value == null) return 0 to 0
        return abilityPreview.value!!.alignedCost() to abilityPreview.value!!.scatteredCost()
    }

    /**
     * Figure out if [getActualDiceCounts] == [getRequiredDiceCounts].
     */
    private fun countedDiceMatch(actual: Pair<Int, Int>, required: Pair<Int, Int>): Boolean =
        app.trailblazercombi.haventide.game2.data.turntable.countedDiceMatch(actual, required)

    /**
     * Figure out if [getActualDiceCounts] == [getRequiredDiceCounts].
     */
    fun countedDiceMatch() = countedDiceMatch(getActualDiceCounts(), getRequiredDiceCounts())

    /**
     * Automatically select dice based on move preview.
     */
    fun autoSelectDice() {
        // Deselect all dice
        this.deselectAllDice()

        // Check if there is any reason to auto-select anything
        if (abilityPreview.value == null) return

        // Save the specifics of the move previewed
        val typeNeeded = abilityPreview.value!!.doerType()
        val alignedNeeded = abilityPreview.value!!.alignedCost()
        val scatteredNeeded = abilityPreview.value!!.scatteredCost()

        // Count how many are left
        var alignedLeft = alignedNeeded
        var scatteredLeft = scatteredNeeded // in fact, any dice will do

        // Go through the entire stack...
        val keys = localDiceStates.getKeys()
        keys.forEach {
            // ...find aligned dice
            if (alignedLeft > 0 && it.type == typeNeeded) {
                localDiceStates.setSelected(it)
                alignedLeft--
                // ...find scattered dice, prioritize the ones that are of a different type
            } else if (scatteredLeft > 0 && it.type != typeNeeded) {
                localDiceStates.setSelected(it)
                scatteredLeft--
            }
        }
        // ...if you don't have enough dice after that
        // If you're missing aligned dice, you're screwed.
        // If you're missing scattered dice, fill them in with aligned dice instead.
        if (scatteredLeft > 0) {
            keys.forEach {
                if (scatteredLeft > 0 && !selectedStateOf(it)!!.value) {
                    localDiceStates.setSelected(it)
                    scatteredLeft--
                }
            }
        }

        // Finally, update the actual consumption state
        updateAbilityPreviewDiceConsumption()
        this.updateDiceCountStates()
    }

    /**
     * @return Whether or not the local player still has any dice.
     */
    fun playerHasDiceLeft() = localDiceStates.getKeys().isNotEmpty()

    // INIT
    init {
        updateTileHighlights()
    }
}

// :3
