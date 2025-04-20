package app.trailblazercombi.haventide.game2.jetpack.universal

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import app.trailblazercombi.haventide.resources.ButtonSeverity
import app.trailblazercombi.haventide.resources.GameScreenDialogBoxStyle
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun RowMenuButton(
    onClick: () -> Unit,
    label: StringResource,
    severity: ButtonSeverity = ButtonSeverity.NEUTRAL,
    modifier: Modifier = Modifier,
    width: Dp
) {
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
            .width(width)
    ) {
        Text(stringResource(label))
    }
}
