package app.trailblazercombi.haventide.game.arena

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.Key

@Composable
actual fun DismissHandling(onDismiss: () -> Unit) {
    Modifier.onPreviewKeyEvent { keyEvent ->
        if (keyEvent.key == Key.Escape) {
            onDismiss()
            true
        } else false
    }
    // FIXME This does not work as intended... but oh well
}
