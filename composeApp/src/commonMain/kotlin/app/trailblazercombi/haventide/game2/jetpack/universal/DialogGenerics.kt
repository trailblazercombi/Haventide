package app.trailblazercombi.haventide.game2.jetpack.universal

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import app.trailblazercombi.haventide.resources.Palette
import kotlinx.coroutines.flow.StateFlow

@Composable
fun DialogGenerics(
    openDialogState: StateFlow<Boolean>,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    val openDialog by openDialogState.collectAsState()

    DismissHandling { onDismissRequest() }
    AnimatedVisibility(openDialog, enter = fadeIn(), exit = fadeOut()) {
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = modifier
                .fillMaxSize()
                .background(Palette.Abyss80)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    onDismissRequest()
                }
        ) {}
    }
}
