package app.trailblazercombi.haventide.game2.jetpack.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput

/**
 * An extension function for [Modifier].
 * It is used to handle hover inputs when a pointer is used, such as a mouse or stylus hover.
 * @param onEnter The function to execute when the cursor enters the specified [Composable].
 * @param onExit The function to execute when the cursor leaves the specified [Composable].
 * @return Raw pointer input, in case it was necessary.
 */
@Composable
fun Modifier.handleHover(onEnter: () -> Unit = {}, onExit: () -> Unit = {}): Modifier = pointerInput(Unit) {
    awaitPointerEventScope {
        while (true) {
            val event = awaitPointerEvent()
            when (event.type) {
                PointerEventType.Enter -> onEnter()
                PointerEventType.Exit -> onExit()
                else -> {}
            }
        }
    }
}
