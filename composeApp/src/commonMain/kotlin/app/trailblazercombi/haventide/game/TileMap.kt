package app.trailblazercombi.haventide.game

import androidx.compose.foundation.*
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
import androidx.compose.ui.unit.dp
import app.trailblazercombi.haventide.game.ClickStates.*
import app.trailblazercombi.haventide.res.Palette

val tileSize = 128.dp
val tilePadding = 22.dp
val tileCornerRounding = 16.dp
val tileOutlineThickness = 8.dp

// TODO This will change based on the map itself.
val gameBackgroundColor = Palette.FillDarkPrimary
/**
 * Keep track of click states and the associated tile colors.
 * This enum exists for the purposes of highlighting tiles.
 *
 * Please refer to the game rules to find out what each of these highlights mean.
 * @property NOT_CLICKED The [tile][TileData] should not be highlighted unless hovered over by mouse.
 * @property CLICKED_PRIMARY The [tile][TileData] should be highlighed with yellow fill and outline.
 * @property CLICKED_SECONDARY The [tile][TileData] should be highlighted with white fill and outline.
 * @property HIGHLIGHT_PRIMARY The [tile][TileData] should be highlighted with yellow outline.
 * @property HIGHLIGHT_SECONDARY The [tile][TileData] should be highlighted with white outline.
 */
enum class ClickStates(
    val fillColor: Color = Palette.Glass00,
    val outlineColor: Color = Palette.Glass20,
    val fillHoverColor: Color = Palette.Glass20.compositeOver(fillColor),
    val outlineHoverColor: Color = fillHoverColor
) {
    NOT_CLICKED,
    CLICKED_PRIMARY(Palette.FillYellow, Palette.FillYellow),
    CLICKED_SECONDARY(Palette.FillLightPrimary, Palette.FillLightPrimary),
    HIGHLIGHT_PRIMARY(outlineColor = Palette.FillYellow),
    HIGHLIGHT_SECONDARY(outlineColor = Palette.FillLightPrimary),
}

class TileMapData(val rows: Int, val columns: Int) {

}

class TileData(val parentMap: TileMapData, private val mechanisms: MutableSet<Mechanism> = mutableSetOf()) {
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
}

@Composable
fun ComposableTileMap(
    mapData: TileMapData,
    modifier: Modifier = Modifier
) {
    // Keep the states here
    val scrollStateX = rememberScrollState()
    val scrollStateY = rememberScrollState()

    // The TileMap's background.
    Box(
        modifier = modifier.horizontalScroll(scrollStateX, true).verticalScroll(scrollStateY, true).background(gameBackgroundColor)
    ) {
        Column {
            for (y in 0 until mapData.rows) {
                Row {
                    for (x in 0 until mapData.columns) {
                        // Starting from this point, we have tile coordinates.
                        ComposableTile(null)
                    }
                }
            }
        }
    }
}

@Composable
fun ComposableTile(tileData: TileData? = null, modifier: Modifier = Modifier) {
    var clicked by remember { mutableStateOf(NOT_CLICKED) }
    var fillColor by remember { mutableStateOf(clicked.fillColor) }
    var outlineColor by remember { mutableStateOf(clicked.outlineColor) }

    // FIXME
    // if (tileData != null) {
    if (false) {
        // Filled Tile here
    } else {
        // Spacer(modifier.width(tileSize).height(tileSize))

        Box(
            // Full tile scope
            contentAlignment = Alignment.Center,
            modifier = modifier
                .width(tileSize)
                .height(tileSize)
                .handleInput(
                    onEnter = {
                        outlineColor = clicked.outlineHoverColor
                        fillColor = clicked.fillHoverColor
                    },
                    onExit = {
                        outlineColor = clicked.outlineColor
                        fillColor = clicked.fillColor
                    },
                    onPress = {
                        // TODO Send clickEvent to the Tile / aka the data layer
                        clicked = if (clicked == CLICKED_PRIMARY) NOT_CLICKED else CLICKED_PRIMARY
                        fillColor = clicked.fillHoverColor
                        outlineColor = clicked.outlineHoverColor
                    }
                )
        ) {
            Box( // The outline
                modifier = Modifier
                    .size(tileSize - tilePadding)
                    .clip(RoundedCornerShape(tileCornerRounding))
                    .border(tileOutlineThickness, outlineColor, RoundedCornerShape(tileCornerRounding))
                    .background(fillColor)
            )
        }
    }
}

/**
 * Modifier extension function for
 * standardized input handling for
 * TileMap Tiles.
 */
fun Modifier.handleInput(
    onEnter: () -> Unit = {},
    onExit: () -> Unit = {},
    onPress: () -> Unit = {},
    onRelease: () -> Unit = {},
    onScroll: () -> Unit = {},
): Modifier = pointerInput(Unit) {
    awaitPointerEventScope {
        while (true) {
            val event = awaitPointerEvent()
            when (event.type) {
                PointerEventType.Enter -> onEnter()
                PointerEventType.Exit -> onExit()
                PointerEventType.Press -> onPress()
                PointerEventType.Release -> onRelease()
                PointerEventType.Move -> onRelease()
                PointerEventType.Scroll -> onScroll()
                else -> {}
            }
        }
    }
}