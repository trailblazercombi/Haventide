package app.trailblazercombi.haventide.game2.jetpack.universal

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun rememberScreenSize(): Pair<Dp, Dp> {
    var size by remember { mutableStateOf(Pair(0.dp, 0.dp)) }

    BoxWithConstraints(Modifier.fillMaxSize()) {
        LaunchedEffect(maxWidth, maxHeight) {
            size = maxWidth to maxHeight
            println("WxH ### $maxWidth x $maxHeight")
        }
    }

    return size
}
