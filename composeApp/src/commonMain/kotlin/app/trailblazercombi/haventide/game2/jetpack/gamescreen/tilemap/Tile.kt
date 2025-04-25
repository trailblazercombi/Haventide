package app.trailblazercombi.haventide.game2.jetpack.gamescreen.tilemap

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.compositeOver
import app.trailblazercombi.haventide.game2.data.tilemap.TileData
import app.trailblazercombi.haventide.game2.jetpack.extensions.handleHover
import app.trailblazercombi.haventide.game2.viewModel.GameLoopViewModel
import app.trailblazercombi.haventide.resources.TileStyle.CornerRounding
import app.trailblazercombi.haventide.resources.TileStyle.OutlineThickness
import app.trailblazercombi.haventide.resources.TileStyle.Padding
import app.trailblazercombi.haventide.resources.TileStyle.TileSize
import app.trailblazercombi.haventide.resources.UniversalColorizer.HOVER_GLASS
import app.trailblazercombi.haventide.resources.UniversalColorizer.NO_INTERACTIONS

/**
 * This is the UI layer of [TileData].
 * @param viewModel The central [GameLoopViewModel]. Contains click states and similar.
 * @param tileData The [TileData] to be rendered. Contains intrinsic data. __DO NOT MODIFY__. Pass `null` to render a hole.
 */
@Composable
fun Tile(viewModel: GameLoopViewModel, tileData: TileData? = null, modifier: Modifier = Modifier) {
    if (tileData != null) {
        val clickState by viewModel.tileClickStateFor(tileData)!!.collectAsState()
        val highlightState by viewModel.tileHighlightStateFor(tileData)!!.collectAsState()
        var hoverState by remember { mutableStateOf(NO_INTERACTIONS) }

        Box( // Full tile scope
            contentAlignment = Alignment.Center,
            modifier = modifier
                .width(TileSize)
                .height(TileSize)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = { viewModel.processTileClickEvent(tileData) }
                )
                .handleHover(
                    onEnter = { hoverState = HOVER_GLASS },
                    onExit = { hoverState = NO_INTERACTIONS }
                )
        ) {
            Box( // The rendered outline
                modifier = Modifier
                    .size(TileSize - Padding)
                    .clip(RoundedCornerShape(CornerRounding))
                    .border(
                        OutlineThickness,
                        hoverState.outlineColor
                            .compositeOver(highlightState.outlineColor)
                            .compositeOver(clickState.outlineColor),
                        RoundedCornerShape(CornerRounding)
                    )
                    .background(hoverState.fillColor
                        .compositeOver(highlightState.fillColor)
                        .compositeOver(clickState.fillColor)
                    )
            )
            MechanismStack(tileData, modifier.align(Alignment.Center))
        }
    } else {
        Spacer(modifier.width(TileSize).height(TileSize))
    }
}
