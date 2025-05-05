package app.trailblazercombi.haventide.matchLoad.jetpack.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import app.trailblazercombi.haventide.resources.MainMenuStyle
import app.trailblazercombi.haventide.resources.MatchBeginStyle
import app.trailblazercombi.haventide.resources.Palette

@Composable
fun PairingField(
    enabled: Boolean = true,
    value: String = "Enter...",
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    contentColor: Color = Palette.FullWhite,
    label: String,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.padding(MatchBeginStyle.InnerPadding)
    ) {
        Column {
            Text(
                text = label,
                fontSize = MatchBeginStyle.PairingInputLabelSize,
                textAlign = TextAlign.Center,
                color = Palette.FillLightPrimary,
                modifier = modifier.fillMaxWidth()
            )
            BasicTextField(
                enabled = enabled,
                value = value,
                onValueChange = onValueChange,
                singleLine = false,
                textStyle = TextStyle(
                    color = contentColor,
                    fontSize = MatchBeginStyle.PairingInputTextSize,
                    textAlign = TextAlign.Center,
                    lineHeight = MatchBeginStyle.PairingInputTextSize,
                ),
                modifier = modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
            )
        }
    }
}
