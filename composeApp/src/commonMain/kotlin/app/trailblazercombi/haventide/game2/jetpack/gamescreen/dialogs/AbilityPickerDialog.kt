package app.trailblazercombi.haventide.game2.jetpack.gamescreen.dialogs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.max
import app.trailblazercombi.haventide.game2.jetpack.gamescreen.components.cipanel.CiAbilityCard
import app.trailblazercombi.haventide.game2.jetpack.universal.DialogGenerics
import app.trailblazercombi.haventide.game2.viewModel.GameLoopViewModel
import app.trailblazercombi.haventide.resources.AbilityPickerStyle
import app.trailblazercombi.haventide.resources.CiStyle
import app.trailblazercombi.haventide.resources.Palette

@Composable
fun AbilityPickerDialog(viewModel: GameLoopViewModel, modifier: Modifier = Modifier) {
    val openDialog by viewModel.abilityPickerDialog.collectAsState()
    val screenWidth by viewModel.screenWidth.collectAsState()
    val activePhoenix by viewModel.currentPhoenix.collectAsState()
    val abilityListAvailable by viewModel.availableAbilities.collectAsState()

    val scrollState = rememberScrollState()

    DialogGenerics(
        openDialogState = viewModel.abilityPickerDialog,
        onDismissRequest = viewModel::hideAbilityPickerDialog,
        modifier = modifier
    )

    AnimatedVisibility(
        visible = openDialog && activePhoenix != null,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .fillMaxSize()
//                .padding(bottom = CiStyle.AbilityPickerOffsetFromBottom)
        ) {
            Column (modifier = modifier.verticalScroll(scrollState)) {
                activePhoenix?.findAllAbilities()?.forEach {
                    val valid = it in abilityListAvailable
                    Surface(
                        shape = RoundedCornerShape(0.dp),
                        color = if (valid) Palette.FillLightPrimary
                        else Palette.Abyss30.compositeOver(Palette.FullGrey),
                        modifier = modifier
                            .width(
                                min(
                                    max(
                                        screenWidth / 3,
                                        AbilityPickerStyle.MinAbilityCardWidth
                                    ),
                                    AbilityPickerStyle.MaxAbilityCardWidth
                                )
                            )
                            .padding(vertical = CiStyle.InnerPadding)
                            .clickable(enabled = valid) {
                                viewModel.processAbilityPreviewChangeEvent(it)
                                viewModel.hideAbilityPickerDialog()
                            }
                    ) {
                        CiAbilityCard(
                            ability = it,
                            contentColor = if (valid) Palette.FullBlack
                                else Palette.Abyss60.compositeOver(Palette.FullGrey),
                            compact = if (screenWidth < AbilityPickerStyle.MinAbilityCardWidth) 1 else 0
                        )
                    }
                }
            }
        }
    }
}
