package app.trailblazercombi.haventide.game2.jetpack.gamescreen.components.dice

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import app.trailblazercombi.haventide.game2.data.turntable.Die
import app.trailblazercombi.haventide.game2.viewModel.GameLoopViewModel
import app.trailblazercombi.haventide.resources.DieStyle
import app.trailblazercombi.haventide.resources.functions.DieHighlightState
import kotlinx.coroutines.flow.compose
import org.jetbrains.compose.resources.painterResource
import app.trailblazercombi.haventide.resources.DieStyle.DieSize as dieSize

@Composable
fun ComposableDie(viewModel: GameLoopViewModel, die: Die, onClick: () -> Unit = {}, modifier: Modifier = Modifier) {
    val dieType: Painter = painterResource(die.type.icon)

    val stateMap by viewModel.localDiceStates.collectAsState()

    val potential by stateMap.alignedStateOf(die)?.collectAsState()!!
    val selected by stateMap.selectedStateOf(die)?.collectAsState()!!

    Box(
        modifier = modifier.clickable(
            indication = LocalIndication.current,
            interactionSource = remember { MutableInteractionSource() },
            onClick = onClick
            )
            .width(dieSize)
            .height(dieSize)
            .clip(RoundedCornerShape(DieStyle.CornerRounding))
            .border(
                BorderStroke(DieStyle.OutlineThickness, DieHighlightState(potential, selected).outlineColor),
                RoundedCornerShape(DieStyle.CornerRounding)
            )
            .background(DieHighlightState(potential, selected).fillColor)
    ) {
            Image(
                painter = dieType,
                contentDescription = null,
                colorFilter = ColorFilter.tint(DieHighlightState(potential, selected).contentColor),
                modifier = modifier
                    .align(Alignment.Center)
                    .padding(DieStyle.InnerPadding)
            )
    }
}
