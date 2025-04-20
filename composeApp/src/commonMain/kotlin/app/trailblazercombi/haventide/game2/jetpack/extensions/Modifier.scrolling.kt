package app.trailblazercombi.haventide.game2.jetpack.extensions

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Handles 2D app.trailblazercombi.haventide.game2.jetpack.scrolling on TileMap.
 * @param horizontalScrollState The scroll state for horizontal scroll.
 * @param verticalScrollState The scroll state for vertical scroll.
 */
@Composable
fun Modifier.scrolling(horizontalScrollState: ScrollState, verticalScrollState: ScrollState): Modifier {
    return this
        .horizontalScroll(horizontalScrollState)
        .verticalScroll(verticalScrollState)
}
