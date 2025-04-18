package app.trailblazercombi.haventide.game2.jetpack.gamescreen.panels.components

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.min
import app.trailblazercombi.haventide.game.arena.GameLoopViewModel
import app.trailblazercombi.haventide.game.mechanisms.PhoenixMechanism
import app.trailblazercombi.haventide.resources.GameScreenTopBubbleStyle
import app.trailblazercombi.haventide.resources.ScreenSizeThresholds

@Composable
fun PhoenixMiniatureStrip(
    allies: List<PhoenixMechanism>,
    viewModel: GameLoopViewModel
) {
    val screenWidth by viewModel.screenWidth.collectAsState()

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
