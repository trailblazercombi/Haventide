@file:Suppress("unused")

package app.trailblazercombi.haventide.game

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import app.trailblazercombi.haventide.game.UniversalColorizer.*
import app.trailblazercombi.haventide.res.Palette
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.math.*

val tileSize = 80.dp
val tilePadding = 16.dp
val tileCornerRounding = 4.dp
val tileOutlineThickness = 2.dp

/**
 * Keep track of click states and the associated tile colors.
 * This enum exists for the purposes of highlighting tiles.
 *
 * Please refer to the game rules to find out what each of these highlights mean.
 * @property NO_INTERACTIONS_WITH_OUTLINE The [tile][TileData] should not be highlighted unless hovered over by mouse.
 * @property CLICKED_PRIMARY The [tile][TileData] should be highlighed with yellow fill and outline.
 * @property CLICKED_SECONDARY The [tile][TileData] should be highlighted with white fill and outline.
 * @property HIGHLIGHT_PRIMARY The [tile][TileData] should be highlighted with yellow outline.
 * @property HIGHLIGHT_SECONDARY The [tile][TileData] should be highlighted with white outline.
 */
enum class UniversalColorizer(
    val fillColor: Color = Palette.Glass00,
    val outlineColor: Color = Palette.Glass00,
) {
    NO_INTERACTIONS,
    NO_INTERACTIONS_WITH_OUTLINE(outlineColor = Palette.Glass20),
    CLICKED_PRIMARY(Palette.FillYellow, Palette.FillYellow),
    CLICKED_SECONDARY(Palette.FillLightPrimary, Palette.FillLightPrimary),
    HIGHLIGHT_PRIMARY(outlineColor = Palette.FillYellow),
    HIGHLIGHT_SECONDARY(outlineColor = Palette.FillLightPrimary),
    HOVER_GLASS(Palette.Glass10, Palette.Glass20),
}

/**
 * This is a position on the map, represented by x and y coordinates.
 */
@Suppress("EqualsOrHashCode")
data class Position(val x: Int, val y: Int) {

    /**
     * Check if this position is equal to another position.
     * Uses the default [Any.equals] method.
     * @param other The [position][Position] you're trying to check against.
     * @return `true` if this position is the same as the other position.
     * Otherwise, returns `false`.
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Position) return false
        if (x != other.x) return false
        if (y != other.y) return false
        return true
    }

    override fun toString(): String {
        return "Position [$x, $y]"
    }

    fun toDpX(position: Position = this): Dp {
        return tileSize * position.x
    }

    fun toDpY(position: Position = this): Dp {
        return tileSize * position.y
    }

    /**
     * Calculates the distance from this position to another.
     * This distance is in [tiles][TileData], not pixels.
     * Useful for caluclating ability reaches and ranges.
     * @param other The other position you're trying to calculate to.
     * @return The exact distance of a direct straight line
     * between the two positions, in [tiles][TileData].
     */
    private fun distanceTo(other: Position): Double {
        val distX = abs(this.x - other.x)
        val distY = abs(this.y - other.y)
        return hypot(distX.toDouble(), distY.toDouble())
    }

    /**
     * Takes the direct line between the two positions.
     * Returns every position the line intersects.
     *
     * Note: A position here is takes as a [tile][TileData],
     * meaning a square that's __1 tile × 1 tile__ in size,
     * where the position is defined as the center of the tile[TileData]
     * and then spreads __0.5 tiles__ in either direction.
     *
     * @param other The other position you're trying to calculate to.
     * @return A [set][Set] containing all the [tiles][TileData] on the way,
     * __excluding__ [this][Position] and [other][Position].
     */
    fun trajectoryTo(other: Position): List<Position> {
        /* With help from ChatGPT.
         * Prompt:
         *     This will be maths + Kotlin.
         *     I need an algorithm that will
         *     1. eat a start and end position, defined as Position(x, y).
         *     2. return all positions en-route from start to end.
         *     Each position is a tile, defined around its middle.
         *     The tile is 1*1 in size.
         * GPT's notes:
         *     We'll use a form of Bresenham's Line Algorithm, adapted for floating points.
         *     In your case, a better fit is a Digital Differential Analyzer (DDA) algorithm
         *     or a tile stepping method similar to Amanatides and Woo (used in raycasting
         *     engines like in games).
         * The generated algorithm was adapted, reviewed and "thoroughly" tested.
         */
        val trajectory = mutableListOf<Position>()

        var currentX = this.x.toDouble() // The x-coordinate currently checked.
        var currentY = this.y.toDouble() // The y-coordinate currently checked.

        val distanceX = other.x - this.x
        val distanceY = other.y - this.y

        // The number of steps to take, incrementing by 1 by the longer side.
        val steps = maxOf(abs(distanceX), abs(distanceY))

        // Return nothing if there is no path
        if (steps == 0) return emptyList()

        // The actual steps to take, on X and Y coordinates.
        val xStep = distanceX.toDouble() / steps
        val yStep = distanceY.toDouble() / steps

        // For each step, find the closest Position and add it to the trajectory.
        for (i in 1..<steps) {
            trajectory += Position(currentX.roundToInt(), currentY.roundToInt())
            currentX += xStep
            currentY += yStep
        }

        return trajectory.distinct() // Remove duplicates in case of rounding overlap
    }

    /**
     * Returns the surroundings of this [position][Position] within a circle.
     * @param radius How far to look for the result. Default value is sqrt(2).
     * @return A [Set] of the positions immidiately surrounding [this][Position],
     * __excluding__ [this][Position].
     */
    fun surroundings(radius: Double = sqrt(2.toDouble())): Set<Position> {
        if (radius < 1.toDouble()) return emptySet()
        val result = mutableSetOf<Position>()

        val search = round(radius).toInt()
        for (y in -search .. search) {
            val realY = this.y + y
            for (x in -search .. search) {
                val realX = this.x + x
                val other = Position(realX, realY)
                if (distanceTo(other) <= radius) {
                    result.add(other)
                }
            }
        }

        result.remove(this)
        return result.toSet()
    }
}

class TileMapData {

    // FIXME Size is not supposed to be a property, but there is stuff still referencing it.
    // TODO Read from file!!!
    val columns = 13; val rows = 14
    val backdropColor = Palette.FullBlack

    private val tiles: java.util.HashMap<Position, TileData?> = HashMap()

// TILE HIGHLIGHT HANDLING
    /**
     * Describes the primary selected tile (yellow).
     * If the yellow tile had an outline before selection (was in availableTiles1),
     * the setters also generates white outline tiles.
     */
    private var selectedTile1: TileData? = null
        set(value) {
            field?.updateClickState(NO_INTERACTIONS_WITH_OUTLINE)
            field = value
            field?.updateClickState(CLICKED_PRIMARY)
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

    /* NOTE: Secondary always takes precedence.
         * Reason: when the primary is selected (clicked on a Phoenix),
         * selecting the secondary will always yield a move preview. If the move's target is an ally,
         * previewing a move targeting an ally is always more desirable than selecting another ally.
         * That is, of course, if the ally is targetable (within range and the Phoenix has an ability for that).
         */

    private fun updateAvailableTiles() {
        this.clearAvailableTiles1()
        this.clearAvailableTiles2()

        // TODO: Here goes the algorithm for highlighting primary tiles with allied Phoenixes.

        if (selectedTile1 != null) {
            // TODO: Here goes the algorithm for highlighting secondary positions based on selected Phoenix.
            for (positionNearby in selectedTile1!!.position.surroundings(2.3)) {
                val tileNearby = get(positionNearby)
                if (tileNearby != null) {
                    this.addToAvailableTiles2(tileNearby)
                }
            }
        }
    }

    /**
     * Get [a tile][TileData] from the map definition.
     * @param x The x-coordinate of the [desired tile][TileData].
     * @param y The y-coordiante of the [desired tile][TileData].
     * @return The [desired tile][TileData] if it exists at that [position][Position],
     * or `null` if the requested [position][Position] yielded no [tile][TileData].
     */
    operator fun get(x: Int, y: Int): TileData? {
        return tiles[Position(x, y)]
    }

    /**
     * Get [a tile][TileData] from the map definition.
     * @param position The [position][Position] of the [desired tile][TileData].
     * @return The [desired tile][TileData] if it exists at that [position][Position],
     * or `null` if the requested [position][Position] yielded no [tile][TileData].
     */
    operator fun get(position: Position): TileData? {
        return tiles[position]
    }

    init {
        // TODO Read from a file instead
        for (y in 0 until rows) {
            for (x in 0 until columns) {
                val position = Position(x, y)
                if (x % 2 == 0 && y % 3 == 0) tiles[position] = null
                else tiles[position] = TileData(this, position)
            }
        }
    }

    internal fun tileClickEvent(tile: TileData) {
        if (selectedTile1 != null) {
            if (tile == selectedTile1) selectedTile1 = null
            else if (this.availableTiles2.contains(tile)) selectedTile2 = tile
            else selectedTile1 = tile
        } else selectedTile1 = tile
        this.updateAvailableTiles()
    }
}

class TileData(
    // BASIC DATA
    private val parentMap: TileMapData,
    internal val position: Position,
    private val mechanisms: MutableSet<Mechanism> = mutableSetOf(),

    // STATE COLORIZERS
    internal var clickStateColorizer: MutableStateFlow<UniversalColorizer> = MutableStateFlow(NO_INTERACTIONS_WITH_OUTLINE),
    internal var highlightStateColorizer: MutableStateFlow<UniversalColorizer> = MutableStateFlow(NO_INTERACTIONS),
    internal var hoverStateColorizer: MutableStateFlow<UniversalColorizer> = MutableStateFlow(NO_INTERACTIONS)
) {
// MECHANISM ADDITION, REMOVAL AND CHECKS
    /**
     * Runs [checks][canAddMechanism] on the specified [mechanism][Mechanism].
     * If [all checks][canAddMechanism] are passed, adds the [mechanism][Mechanism] to this tile.
     *
     * @param mechanism The [mechanism][Mechanism] in question.
     */
    fun addMechanism(mechanism: Mechanism) {
        if (canAddMechanism(mechanism)) mechanisms.add(mechanism)
    }

    /**
     * Runs [checks][canAddMechanism] on the specified [mechanism][Mechanism].
     * If [all checks][canAddMechanism] are passed, removes the [mechanism][Mechanism] from this tile.
     *
     * @param mechanism The [mechanism][Mechanism] in question.
     */
    fun removeMechanism(mechanism: Mechanism) {
        if (canRemoveMechanism(mechanism)) mechanisms.remove(mechanism)
    }

    /**
     * Checks if the specified [mechanism][Mechanism] can be added to this tile.
     * @param mechanism The [mechanism][Mechanism] in question.
     * @return `true` if the [mechanism][Mechanism] can be added,
     * `false` if the [mechanism][Mechanism] is a duplicate of one already on this tile,
     * or if any of the [mechanisms][Mechanism] on this tile veto the operation.
     */
    @Suppress("MemberVisibilityCanBePrivate")
    fun canAddMechanism(mechanism: Mechanism): Boolean {
        // 1: Check if it's duplicate
        if (mechanisms.contains(mechanism)) return false
        // 2: Check if it's compatible with the current roster
        for (mechie in mechanisms) {
            if (mechie.vetoTilemateAddition(mechanism)) return false
        }
        // All checks passed: Can move mechanism
        return true
    }

    /**
     * Checks if the specified [mechanism][Mechanism] can traverse through this tile.
     * @param mechanism The [mechanism][Mechanism] in question.
     * @return `true` if the [mechanism][Mechanism] can do so,
     * `false` if any of the [mechanisms][Mechanism] on this tile veto the operation.
     */
    fun canTraverse(mechanism: Mechanism): Boolean {
        for (mechie in mechanisms) {
            if (mechie.vetoTraversal(mechanism)) return false
        }
        return true
    }

    /**
     * Checks if the specified [mechanism][Mechanism] can be removed from this tile.
     * @param mechanism The [mechanism][Mechanism] in question.
     * @return `true` if the [mechanism][Mechanism] can be removed,
     * `false` if the [mechanism][Mechanism] doesn't exist on this tile,
     * or if any of the other [mechanisms][Mechanism] on this tile veto the operation.
     */
    @Suppress("MemberVisibilityCanBePrivate")
    fun canRemoveMechanism(mechanism: Mechanism): Boolean {
        // 1: Check if it's here
        if (!mechanisms.contains(mechanism)) return false
        // 2: Check if it's not necessary for another mechanism to exist
        for (mechie in mechanisms) {
            if (mechie.vetoTilemateRemoval(mechanism)) return false
        }
        // All checks passed: Can remove mechanism
        return true
    }

    // END OF MECHANISM OPERATIONS
// CLICK PROPAGATION AND STATE HANDLING
    /**
     * Send the click event from [the user][ComposableTile] upwards, to the parent [map][TileMapData].
     * The [map][TileMapData] handles the rest.
     */
    fun tileClickEvent() {
        parentMap.tileClickEvent(this)
    }

    /**
     * Update the __click state__ of the Composable Tile.
     * This dictates the base fill and outline color.
     * @param clickStateColorizer The new color of this state, as defined by [UniversalColorizer]
     */
    internal fun updateClickState(clickStateColorizer: UniversalColorizer) {
        this.clickStateColorizer.value = clickStateColorizer
    }

    /**
     * Update the __highlight__ state of the Composable Tile.
     * This dictates the outline color, for tiles which would actually be useful to click.
     * @param highlightStateColorizer The new color of this state, as defined by [UniversalColorizer]
     */
    internal fun updateHighlightState(highlightStateColorizer: UniversalColorizer) {
        this.highlightStateColorizer.value = highlightStateColorizer
    }

    /**
     * Update the __hover__ state of the Composable Tile.
     * This dictates the fill and outline glass for pointer devices.
     * @param hoverStateColorizer The new color of this state, as defined by [UniversalColorizer]
     */
    internal fun updateHoverState(hoverStateColorizer: UniversalColorizer) {
        this.hoverStateColorizer.value = hoverStateColorizer
    }
}

@Composable
fun ComposableTileMap(mapData: TileMapData, modifier: Modifier = Modifier) {
    // Keep the states here
    val scrollStateX = rememberScrollState()
    val scrollStateY = rememberScrollState()

    // The TileMap's background.
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .horizontalScroll(scrollStateX, true)
            .verticalScroll(scrollStateY, true)
            .background(mapData.backdropColor)
    ) {
        Column {
            for (y in 0 until mapData.rows) {
                Row {
                    for (x in 0 until mapData.columns) {
                        // Starting from this point, we have tile coordinates.
                        ComposableTile(mapData[x, y])
                    }
                }
            }
        }
    }
}

@Composable
fun ComposableTile(tileData: TileData? = null, modifier: Modifier = Modifier) {
    if (tileData != null) {
        val clickState by tileData.clickStateColorizer.collectAsState()
        val highlightState by tileData.highlightStateColorizer.collectAsState()
        val hoverState by tileData.hoverStateColorizer.collectAsState()

        Box( // Full tile scope
            contentAlignment = Alignment.Center,
            modifier = modifier
                .width(tileSize)
                .height(tileSize)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = {
                        tileData.tileClickEvent()
                    }
                )
                .handleHover(
                    onEnter = {
                        tileData.updateHoverState(HOVER_GLASS)
                    },
                    onExit = {
                        tileData.updateHoverState(NO_INTERACTIONS)
                    }
                )
        ) {
            Box( // The rendered outline
                modifier = Modifier
                    .size(tileSize - tilePadding)
                    .clip(RoundedCornerShape(tileCornerRounding))
                    .border(tileOutlineThickness,
                        hoverState.outlineColor
                            .compositeOver(highlightState.outlineColor)
                            .compositeOver(clickState.outlineColor),
                        RoundedCornerShape(tileCornerRounding))
                    .background(hoverState.fillColor
                        .compositeOver(highlightState.fillColor)
                        .compositeOver(clickState.fillColor)
                    )
            )
            // TODO ComposableMechanism() // The mechanisms
        }
    } else {
        Spacer(modifier.width(tileSize).height(tileSize))
    }
}

/**
 * Modifier extension function for
 * standardized input handling for
 * TileMap Tiles.
 */
fun Modifier.handleHover(onEnter: () -> Unit = {}, onExit: () -> Unit = {}): Modifier = pointerInput(Unit) {
    awaitPointerEventScope {
        while (true) {
            val event = awaitPointerEvent()
            when (event.type) {
                PointerEventType.Enter -> onEnter()
                PointerEventType.Exit -> onExit()
                else -> {}
            }
        }
    }
}