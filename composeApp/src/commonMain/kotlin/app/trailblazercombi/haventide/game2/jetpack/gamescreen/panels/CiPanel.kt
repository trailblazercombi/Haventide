package app.trailblazercombi.haventide.game2.jetpack.gamescreen.panels

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.trailblazercombi.haventide.game2.jetpack.gamescreen.components.cipanel.CiAbilityCard
import app.trailblazercombi.haventide.game2.viewModel.GameLoopViewModel
import app.trailblazercombi.haventide.resources.CiStyle
import app.trailblazercombi.haventide.resources.DiceCounterStyle
import app.trailblazercombi.haventide.resources.DieStyle
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

    Box (
        contentAlignment = Alignment.BottomCenter,
        modifier = modifier
            .fillMaxSize()
            .padding(bottom =
                if (screenWidth > ScreenSizeThresholds.SpreadDiceStackOnSingleLine) CiStyle.PanelOffsetFromBottom
                else CiStyle.PanelOffsetFromBottomDouble
            )
    ) {
        if (abilityPreview != null) {
            Box (modifier = modifier.width(CiStyle.AbilityCardWidth)) {
                Surface(
                    shape = RoundedCornerShape(0.dp),
                    color = GameScreenTopBubbleStyle.FillColorModifier,
                    contentColor = Palette.FullWhite,
                    border = BorderStroke(
                        GameScreenTopBubbleStyle.OutlineThickness,
                        GameScreenTopBubbleStyle.OutlineColorModifier
                    )
                ) {
                    CiAbilityCard(abilityPreview!!.template)
                }
            }
        }
    }
}
