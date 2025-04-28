package app.trailblazercombi.haventide.game2.jetpack.gamescreen.panels

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.trailblazercombi.haventide.game2.viewModel.GameLoopViewModel
import app.trailblazercombi.haventide.resources.CiStyle
import app.trailblazercombi.haventide.resources.ScreenSizeThresholds

@Composable
fun BottomPanel(viewModel: GameLoopViewModel, modifier: Modifier = Modifier) {
    val screenWidth by viewModel.screenWidth.collectAsState()

    Box (
        contentAlignment = Alignment.BottomCenter,
        modifier = modifier.fillMaxSize()
    ) {
        Column (
            modifier.padding(
                if (screenWidth > ScreenSizeThresholds.FloatBottomBarAsBubble) CiStyle.OffsetFromEdge else 0.dp
            )
        ) {
            CiPanel(viewModel, modifier)
            DiceInfoPanel(viewModel, modifier)
        }
    }
}
