package app.trailblazercombi.haventide.game2.jetpack.gamescreen.components.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.trailblazercombi.haventide.game2.viewModel.GameLoopViewModel
import app.trailblazercombi.haventide.resources.*
import app.trailblazercombi.haventide.resources.DieStyle.CornerRounding
import org.jetbrains.compose.resources.stringResource

@Composable
fun EndRoundButton(viewModel: GameLoopViewModel, modifier: Modifier = Modifier) {
    val screenWidth by viewModel.screenWidth.collectAsState()

    val localPlayerTurn by viewModel.localPlayerTurn.collectAsState()

    Button(
        shape = RoundedCornerShape(CornerRounding),
        enabled = localPlayerTurn,
        onClick = { viewModel.showEndRoundDialog() }, colors = ButtonDefaults.buttonColors(
            backgroundColor = DieStyle.EndRoundButtonSeverity.fillColor,
            contentColor = DieStyle.EndRoundButtonSeverity.contentColor,
        ), border = BorderStroke(
            DieStyle.OutlineThickness, DieStyle.EndRoundButtonSeverity.outlineColor
        ), modifier = modifier.padding(DieStyle.Separation).height(
            DieStyle.DieSize * (if (screenWidth > ScreenSizeThresholds.SpreadDiceStackOnSingleLine) 1 else 2)
        ).width(
            DieStyle.DieSize * (if (screenWidth > ScreenSizeThresholds.SpreadDiceStackOnSingleLine) 4 else 3)
                    + DieStyle.Separation + 3.dp
        ), contentPadding = PaddingValues(0.dp),
    ) {
        Text(
            text = stringResource(Res.string.game_button_end_round),
            textAlign = TextAlign.Center,
            fontSize = GameScreenDialogBoxStyle.ButtonTextSize,
            modifier = modifier.fillMaxWidth().padding(0.dp)
        )
    }
}
