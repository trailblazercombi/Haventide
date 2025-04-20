package app.trailblazercombi.haventide.game2.viewModel

import androidx.compose.ui.unit.Dp
import kotlinx.coroutines.flow.MutableStateFlow

interface ScreenSizeProvider {
    val screenWidth: MutableStateFlow<Dp>
    val screenHeight: MutableStateFlow<Dp>
}
