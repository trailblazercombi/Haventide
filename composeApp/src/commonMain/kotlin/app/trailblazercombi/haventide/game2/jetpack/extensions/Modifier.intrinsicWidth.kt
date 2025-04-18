package app.trailblazercombi.haventide.game2.jetpack.extensions

import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.trailblazercombi.haventide.resources.GameScreenTopBubbleStyle

@Composable
fun Modifier.intrinsicWidth(): Modifier {
    return this.width(
        GameScreenTopBubbleStyle.InnerOffset * 4 * 2
                + GameScreenTopBubbleStyle.MiniatureWidth * 3 * 2
                + GameScreenTopBubbleStyle.RoundCounterWidth
    )
}
