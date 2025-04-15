package app.trailblazercombi.haventide.game.arena

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable

@Composable
actual fun DismissHandling(onDismiss: () -> Unit) {
    BackHandler(onBack = onDismiss)
}
