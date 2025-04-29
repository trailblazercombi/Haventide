package app.trailblazercombi.haventide.game2.jetpack.gamescreen.components.phoenix

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.*
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.min
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.PhoenixMechanism
import app.trailblazercombi.haventide.game2.viewModel.ScreenSizeProvider
import app.trailblazercombi.haventide.resources.GameScreenTopBubbleStyle
import app.trailblazercombi.haventide.resources.ScreenSizeThresholds
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun PhoenixMiniatureStrip(
    screenSizeProvider: ScreenSizeProvider,
    allyState: MutableStateFlow<List<PhoenixMechanism>>
) {
    val screenWidth by screenSizeProvider.screenWidth.collectAsState()
    val allies by allyState.collectAsState()

    Row {
        allies.forEach {
            PhoenixMiniature(
                it,
                min(
                    a = GameScreenTopBubbleStyle.MiniatureWidth,
                    b = max(
                        a = (screenWidth
                                - (GameScreenTopBubbleStyle.OffsetFromEdge * 2)
                                - (GameScreenTopBubbleStyle.InnerOffset * 5)
                                ) / 6,
                        b = GameScreenTopBubbleStyle.MiniatureHeight
                                + (GameScreenTopBubbleStyle.InnerOffset * 2)
                        // Make sure they're always at least a square
                    )
                ),
                if (screenWidth > ScreenSizeThresholds.FloatTopStatusBarAsBubble) GameScreenTopBubbleStyle.BubbleHeight
                        - (GameScreenTopBubbleStyle.InnerOffset * 2)
                else GameScreenTopBubbleStyle.MiniatureHeight
            )
        }
    }
}
