package app.trailblazercombi.haventide.game2.jetpack.universal

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun ScreenSizeFinder(screenWidth: MutableStateFlow<Dp>, screenHeight: MutableStateFlow<Dp>) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        screenWidth.value = this.maxWidth
        screenHeight.value = this.maxHeight
        println("${screenWidth.value} x ${screenHeight.value}") // FIXME Remove println
    }
}
