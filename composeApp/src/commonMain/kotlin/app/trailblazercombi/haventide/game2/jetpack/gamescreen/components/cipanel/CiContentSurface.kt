package app.trailblazercombi.haventide.game2.jetpack.gamescreen.components.cipanel

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import app.trailblazercombi.haventide.game2.viewModel.ScreenSizeProvider
import app.trailblazercombi.haventide.resources.CiStyle
import app.trailblazercombi.haventide.resources.GameScreenTopBubbleStyle
import app.trailblazercombi.haventide.resources.Palette

@Composable
fun CiContentSurface(
    screenSizeProvider: ScreenSizeProvider,
    toDisplay: () -> Boolean = { true },
    toRoundTopLeft: () -> Boolean = { false },
    toRoundTopRight: () -> Boolean = { false },
    fillColor: Color = GameScreenTopBubbleStyle.FillColorModifier,
    outlineColor: Color = GameScreenTopBubbleStyle.OutlineColorModifier,
    contentColor: Color = Palette.FullWhite,
    modifier: Modifier = Modifier,
    enableClick: Boolean = false,
    onClick: () -> Unit = {},
    content: @Composable BoxScope.() -> Unit
) {
    val screenWidth by screenSizeProvider.screenWidth.collectAsState()

    if (toDisplay()) {
        Box (
            modifier = modifier.width(
                min(
                    screenWidth / 3,
                    CiStyle.MaxAbilityCardWidth,
                )
            )
        ) {
            Surface(
                shape = RoundedCornerShape(
                    topStart = if (toRoundTopLeft()) CiStyle.BubbleModeCornerRounding else 0.dp,
                    topEnd = if (toRoundTopRight()) CiStyle.BubbleModeCornerRounding else 0.dp,
                ),
                color = fillColor,
                contentColor = contentColor,
                border = BorderStroke(
                    GameScreenTopBubbleStyle.OutlineThickness,
                    outlineColor
                ),
                modifier = modifier.clickable(
                    enabled = enableClick,
                    onClick = onClick
                )
            ) {
                content()
            }
        }
    }
}
