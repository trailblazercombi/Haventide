package app.trailblazercombi.haventide.game2.jetpack.gamescreen.panels

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import app.trailblazercombi.haventide.game2.data.tilemap.TileViewInfo
import app.trailblazercombi.haventide.game2.jetpack.gamescreen.components.cipanel.CiAbilityCard
import app.trailblazercombi.haventide.game2.jetpack.gamescreen.components.cipanel.CiContentSurface
import app.trailblazercombi.haventide.game2.jetpack.gamescreen.components.cipanel.CiTileView
import app.trailblazercombi.haventide.game2.viewModel.GameLoopViewModel
import app.trailblazercombi.haventide.resources.GameScreenTopBubbleStyle
import app.trailblazercombi.haventide.resources.Palette
import app.trailblazercombi.haventide.resources.ScreenSizeThresholds

/**
 * This is the Contextual Information Panel (CIPanel).
 */
@Composable
fun CiPanel(viewModel: GameLoopViewModel, modifier: Modifier = Modifier) {
    val screenWidth by viewModel.screenWidth.collectAsState()
    val abilityPreview by viewModel.abilityPreview.collectAsState()

    val leftCiInfo by viewModel.leftCiInfo.collectAsState()
    val rightCiInfo by viewModel.rightCiInfo.collectAsState()

    Row {
        CiContentSurface(
            screenSizeProvider = viewModel,
            toDisplay = { true },
            toRoundTopLeft = { screenWidth > ScreenSizeThresholds.FloatBottomBarAsBubble },
            toRoundTopRight = { abilityPreview == null },
            fillColor = GameScreenTopBubbleStyle.FillColorModifier,
            outlineColor = GameScreenTopBubbleStyle.OutlineColorModifier,
            enableClick = leftCiInfo is TileViewInfo.Ally,
            onClick = viewModel::showLeftModificatorsDialog,
            contentColor = Palette.FullWhite,
            modifier = modifier
        ) {
            CiTileView(
                tile = leftCiInfo ?: return@CiContentSurface,
                compact = { screenWidth < ScreenSizeThresholds.UncompactCiPanel },
            )
        }
        CiContentSurface(
            screenSizeProvider = viewModel,
            toDisplay = { abilityPreview != null },
            toRoundTopLeft = { false },
            toRoundTopRight = { false },
            fillColor = Palette.FillLightPrimary,
            outlineColor = Palette.FillLightPrimary,
            contentColor = Palette.FullBlack,
            enableClick = true,
            onClick = { viewModel.showAbilityPickerDialog() },
            modifier = modifier
        ) {
            CiAbilityCard(
                ability = abilityPreview!!.template,
                compact =
                    if (screenWidth < ScreenSizeThresholds.UncompactCiPanel) 3
                    else if (screenWidth < ScreenSizeThresholds.LessCompactCiPanel) 2
                    else 1
            )
        }
        CiContentSurface(
            screenSizeProvider = viewModel,
            toDisplay = { abilityPreview != null },
            toRoundTopLeft = { false },
            toRoundTopRight = { screenWidth > ScreenSizeThresholds.FloatBottomBarAsBubble },
            fillColor = GameScreenTopBubbleStyle.FillColorModifier,
            outlineColor = GameScreenTopBubbleStyle.OutlineColorModifier,
            enableClick = rightCiInfo is TileViewInfo.Ally,
            onClick = viewModel::showRightModificatorsDialog,
            contentColor = Palette.FullWhite,
            modifier = modifier
        ) {
            CiTileView(
                tile = rightCiInfo ?: return@CiContentSurface,
                compact = { screenWidth < ScreenSizeThresholds.UncompactCiPanel },
            )
        }
    }
}
