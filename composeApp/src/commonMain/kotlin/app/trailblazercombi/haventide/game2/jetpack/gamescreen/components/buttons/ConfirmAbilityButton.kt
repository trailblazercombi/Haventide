package app.trailblazercombi.haventide.game2.jetpack.gamescreen.components.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.trailblazercombi.haventide.game2.data.turntable.countedDiceMatch
import app.trailblazercombi.haventide.game2.viewModel.GameLoopViewModel
import app.trailblazercombi.haventide.resources.*;
import org.jetbrains.compose.resources.stringResource

@Suppress("DuplicatedCode")
@Composable
fun ConfirmAbilityButton(viewModel: GameLoopViewModel, modifier: Modifier = Modifier) {
    val screenWidth by viewModel.screenWidth.collectAsState()
    val abilityPreview by viewModel.abilityPreview.collectAsState()

    val alignedSelected: Int by viewModel.alignedSelectedDiceCount.collectAsState()
    val scatteredSelected: Int by viewModel.scatteredSelectedDiceCount.collectAsState()

    val alignedNeeded: Int = abilityPreview?.alignedCost() ?: 0
    val scatteredNeeded: Int = abilityPreview?.scatteredCost() ?: 0

    val okDiceSelection = countedDiceMatch(
        actual = alignedSelected to scatteredSelected,
        required = alignedNeeded to scatteredNeeded
    )

    Button(
        enabled = okDiceSelection,
        onClick = { viewModel.processExternalAbilityExecution() }, colors = ButtonDefaults.buttonColors(
            backgroundColor = DieStyle.ConfirmButtonSeverity.fillColor,
            contentColor = DieStyle.ConfirmButtonSeverity.contentColor,
        ), border = BorderStroke(
            DieStyle.OutlineThickness, DieStyle.ConfirmButtonSeverity.outlineColor
        ), modifier = modifier.padding(DieStyle.Separation).height(
            DieStyle.DieSize * if (screenWidth > ScreenSizeThresholds.SpreadDiceStackOnSingleLine) 1 else 2
        ).width(
            DieStyle.DieSize * if (screenWidth > ScreenSizeThresholds.SpreadDiceStackOnSingleLine) 2 else 2
        ), contentPadding = PaddingValues(0.dp),
    ) {
        Text(
            text = stringResource(abilityPreview!!.template.abilityVerb.string),
            textAlign = TextAlign.Center,
            fontSize = GameScreenDialogBoxStyle.ButtonTextSize,
            modifier = modifier.fillMaxWidth().padding(0.dp)
        )
    }
}
