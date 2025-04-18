package app.trailblazercombi.haventide.game2.jetpack.universal

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.min
import app.trailblazercombi.haventide.game.arena.GameLoopViewModel
import app.trailblazercombi.haventide.resources.ButtonSeverity
import app.trailblazercombi.haventide.resources.GameScreenDialogBoxStyle
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun RowMenuButton(
    viewModel: GameLoopViewModel,
    onClick: () -> Unit,
    severity: ButtonSeverity,
    label: StringResource,
    width: Dp,
    modifier: Modifier = Modifier
) {
    val screenWidth by viewModel.screenWidth.collectAsState()

    Button(
        onClick = onClick,
        border = BorderStroke(GameScreenDialogBoxStyle.OutlineThickness, severity.outlineColor),
        colors = ButtonDefaults.textButtonColors(
            backgroundColor = severity.fillColor,
            contentColor = severity.contentColor
        ),
        contentPadding = PaddingValues(GameScreenDialogBoxStyle.InnerPadding),
        modifier = modifier
            .padding(GameScreenDialogBoxStyle.InnerPadding)
            .width(
                min(a = (screenWidth) / 3,
                    b = width)
            )
    ) {
        Text(stringResource(label))
    }
}
