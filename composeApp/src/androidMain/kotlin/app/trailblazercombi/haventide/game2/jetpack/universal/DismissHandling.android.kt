package app.trailblazercombi.haventide.game2.jetpack.universal

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable

@Composable
actual fun DismissHandling(onDismiss: () -> Unit) {
    BackHandler(onBack = onDismiss)
}
