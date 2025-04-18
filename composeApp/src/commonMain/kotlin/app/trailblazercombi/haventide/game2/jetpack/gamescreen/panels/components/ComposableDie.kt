package app.trailblazercombi.haventide.game2.jetpack.gamescreen.panels.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import app.trailblazercombi.haventide.game.abilities.Die
import app.trailblazercombi.haventide.resources.DieStyle
import org.jetbrains.compose.resources.painterResource

@Composable
fun ComposableDie(die: Die, onClick: () -> Unit = {}, modifier: Modifier = Modifier) {
    val dieType: Painter = painterResource(die.type.icon)
    val dieSize = DieStyle.DieSize

    val potential by die.potential.collectAsState()
    val selected by die.selected.collectAsState()

    Box(
        modifier = modifier
            .width(dieSize)
            .height(dieSize)
    ) {
        Surface(
            shape = RoundedCornerShape(DieStyle.CornerRounding),
            color = DieHighlightState(potential, selected).fillColor,
            border = BorderStroke(DieStyle.OutlineThickness, DieHighlightState(potential, selected).outlineColor),
            modifier = modifier.clickable(
                indication = LocalIndication.current,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            )
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
}
