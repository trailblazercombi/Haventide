package app.trailblazercombi.haventide.game2.jetpack.universal

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import app.trailblazercombi.haventide.resources.ButtonSeverity
import app.trailblazercombi.haventide.resources.GameScreenDialogBoxStyle
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun MenuButton(
    onClick: () -> Unit,
    label: StringResource,
    severity: ButtonSeverity,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        enabled = true,
        shape = RoundedCornerShape(GameScreenDialogBoxStyle.ButtonCornerRounding),
        border = BorderStroke(
            GameScreenDialogBoxStyle.OutlineThickness,
            severity.outlineColor
        ),
        colors = ButtonDefaults.textButtonColors(
            backgroundColor = severity.fillColor,
            contentColor = severity.contentColor,
        ),
        contentPadding = PaddingValues(GameScreenDialogBoxStyle.OutlineThickness),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(label),
            fontSize = GameScreenDialogBoxStyle.ButtonTextSize,
            textAlign = TextAlign.Center,
            modifier = modifier
        )
    }
}
