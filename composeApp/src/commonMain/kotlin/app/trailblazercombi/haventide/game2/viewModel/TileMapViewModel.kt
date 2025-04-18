package app.trailblazercombi.haventide.game2.viewModel

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import app.trailblazercombi.haventide.game.abilities.AbilityTemplate
import app.trailblazercombi.haventide.game.abilities.DieType
import app.trailblazercombi.haventide.game.abilities.countedDiceMatch
import app.trailblazercombi.haventide.game.mechanisms.*
import app.trailblazercombi.haventide.game2.data.tilemap.TileData
import app.trailblazercombi.haventide.game2.data.tilemap.TileMapData
import app.trailblazercombi.haventide.resources.UniversalColorizer.*
import kotlin.math.*

// TODO THIS THIS THIS THIS THIS
class TileMapViewModel(val tileMap: TileMapData) : ViewModel() {

    private var preparedAbility: AbilityPrepEncapsule? = null

    class AbilityPrepEncapsule(
        val template: AbilityTemplate, val dieType: DieType, val doer: Mechanism, val target: TileData
    )

    private var selectedTile1: TileData? = null
        set(value) {
            field?.updateClickState(NO_INTERACTIONS_WITH_OUTLINE)
            field = value
            field?.updateClickState(CLICKED_PRIMARY)
            tileMap.gameLoop.viewModel.localPlayerDice.value.viewModel.setDiePreference(field?.getPhoenix()?.template?.phoenixType)
            selectedTile2 = null
        }

    /**
     * Describes the secondary selected tile (white).
     * The white tile had to have an outline before selection (was in availableTiles2).
     */
    private var selectedTile2: TileData? = null
        set(value) {
            field?.updateClickState(NO_INTERACTIONS_WITH_OUTLINE)
            field = value
            field?.updateClickState(CLICKED_SECONDARY)
        }

    /**
     * Describes the tertiary selected tile (grey).
     */
    private var selectedTile3: TileData? = null
        set(value) {
            field?.updateClickState(NO_INTERACTIONS_WITH_OUTLINE)
            field = value
            field?.updateClickState(CLICKED_TERTIARY)
        }

    /**
     * A [MutableSet] containing all [tiles][TileData] valid for primary highlight.
     */
    private val availableTiles1 = mutableSetOf<TileData>()

    /**
     * A [MutableSet] containing all [tiles][TileData] valid for secondary highlight.
     */
    private val availableTiles2 = mutableSetOf<TileData>()

    private fun addToAvailableTiles1(tileData: TileData) {
        this.availableTiles1.add(tileData)
        tileData.updateHighlightState(HIGHLIGHT_PRIMARY)
    }

    private fun addToAvailableTiles2(tileData: TileData) {
        this.availableTiles2.add(tileData)
        tileData.updateHighlightState(HIGHLIGHT_SECONDARY)
    }

    private fun clearAvailableTiles1() {
        for (tileData in availableTiles1) {
            tileData.updateHighlightState(NO_INTERACTIONS)
        }
        this.availableTiles1.clear()
    }

    private fun clearAvailableTiles2() {
        for (tileData in availableTiles2) {
            tileData.updateHighlightState(NO_INTERACTIONS)
        }
        this.availableTiles2.clear()
    }

    internal fun tileClickEvent(tile: TileData) {

        // 1. Check if there is an ability ready, and if the dice match...
        if (preparedAbility != null && tile === selectedTile2) {
            val countedDice =
                tileMap.gameLoop.viewModel.localPlayerDice.value.viewModel.countSelectedDice(preparedAbility!!.dieType)

            val requiredDice = Pair(
                first = preparedAbility!!.template.alignedCost,
                second = preparedAbility!!.template.scatteredCost
            )

            // 1.1 Check if the dice count matches...
            if (countedDiceMatch(countedDice, requiredDice)) {

                // 1.1.1 Execute ability!
                tileMap.localPlayer().executeAbility(
                    ability = preparedAbility!!.template,
                    doer = preparedAbility!!.doer,
                    target = preparedAbility!!.target,
                    consume = tileMap.gameLoop.viewModel.localPlayerDice.value.viewModel.getSelectedDice()
                )

                // 1.1.2 Next turn, update tile highlights and shoo
                resetTileHighlights()
                updateAvailableTiles()
                resetAbilityPreparation()
                return

            // 1.2 ...else just ignore the click
            } else {
                println("Not enough dice you bozo")
                return // TODO Tell the player that the dice are fucked up...
            }
        }

        resetAbilityPreparation()

        // 2. If there is no ability ready, do something else.
        selectedTile3 = null
        // If a yellow tile is selected...
        if (selectedTile1 != null) {
            // ...and you click it, deselect it
            if (tile == selectedTile1) selectedTile1 = null
            // ...and you click a white outlined tile, select that tile too + preview move
            else if (this.availableTiles2.contains(tile)) {
                selectedTile2 = tile
                prepareAbilityPreview(
                    template = selectedTile1!!.getPhoenix()!!.findFirstAvailableAbility(selectedTile2!!)!!,
                    doer = selectedTile1!!.getPhoenix()!!,
                    target = selectedTile2!!,
                )
            }
            // ...and you click an unrelated tile, deselect both tiles and see what happens
            else {
                selectedTile1 = null
                selectedTile2 = null
                tileClickEvent(tile)
            }
            // Else, if no tile is selected, and you click a yellow outlined tile
        } else if (availableTiles1.contains(tile)) {
            selectedTile1 = tile
            selectedTile2 = null
            // Else, if not tile is selected and you click an unmarked tile
        } else {
            selectedTile1 = null
            selectedTile2 = null
            selectedTile3 = tile
        }
        this.updateAvailableTiles()
    }

    private fun resetTileHighlights() {
        selectedTile1 = null
        selectedTile2 = null
        selectedTile3 = null
    }

    private fun prepareAbilityPreview(template: AbilityTemplate, doer: PhoenixMechanism, target: TileData) {
        preparedAbility = AbilityPrepEncapsule(template, doer.template.phoenixType, doer, target)

        tileMap.gameLoop.viewModel.previewMoveOnDiceStack(
            selectedTile1!!.getPhoenix()!!,
            template
        )
    }

    private fun resetAbilityPreparation() {
        preparedAbility = null

        tileMap.gameLoop.viewModel.localPlayerDice.value.viewModel.deselectAllDice()
    }

//    private fun executeAbility(triple: Triple<(Mechanism, TileData) -> Unit, Mechanism, TileData>) {
//        tileMap.localPlayer().executeAbility(triple.first, triple.second, triple.third)
//    }

    fun updateAvailableTiles() {
        // 0. Clear the current highlights
        this.clearAvailableTiles1()
        this.clearAvailableTiles2()

        // 1. Check if it's the local player's turn. If not, shoo!
        if (tileMap.localPlayer() != tileMap.turnTable.currentPlayer()) return

        // 2. Add every tile the localPIG's Phoenixes are standing on
        tileMap.turnTable.currentPlayer().team.forEach {
            if (it !is PhoenixMechanism) return@forEach
            addToAvailableTiles1(it.parentTile)
        }

        // 3. Highlight secondary tiles, if the primary one is highlighted
        if (selectedTile1 != null) {
            //  Prerequisites: The aforementioned AbilityStack
            val existingTile = selectedTile1 ?: return // If this fails, selectedTile1 is null.
            val localPhoenix = existingTile.getPhoenix() ?: return // this should already be allied only...

            val surroundings = existingTile.position.traversableSurroundings(
                mapData = tileMap,
                mechanism = DummyImmediateEffecter(existingTile),
                radius = localPhoenix.maxAbilityRange(),
            )

            surroundings.forEach {
                val targetTile = tileMap[it] ?: return@forEach
                if (localPhoenix.findFirstAvailableAbility(targetTile) != null) {
                    addToAvailableTiles2(targetTile)
                }
            }
        }
    }
}
