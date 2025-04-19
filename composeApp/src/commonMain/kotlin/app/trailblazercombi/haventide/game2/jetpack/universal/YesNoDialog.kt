package app.trailblazercombi.haventide.game2.jetpack.universal

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.trailblazercombi.haventide.resources.ButtonSeverity
import app.trailblazercombi.haventide.resources.GameScreenDialogBoxStyle
import app.trailblazercombi.haventide.resources.Palette
import app.trailblazercombi.haventide.resources.ScreenSizeThresholds
import kotlinx.coroutines.flow.StateFlow
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun YesNoDialog(
    openDialogState: StateFlow<Boolean>,
    title: StringResource,
    acceptLabel: StringResource,
    declineLabel: StringResource,
    onAccept: () -> Unit,
    onDecline: () -> Unit,
    onDismissRequest: () -> Unit = onDecline,
    acceptSeverity: ButtonSeverity = ButtonSeverity.PREFERRED,
    declineSeverity: ButtonSeverity = ButtonSeverity.NEUTRAL,
    modifier: Modifier = Modifier
) {
    val openDialog by openDialogState.collectAsState()
    val screenWidth = rememberScreenSize().first

    DialogGenerics(
        openDialogState = openDialogState,
        onDismissRequest = onDismissRequest,
        modifier = modifier
    )

    AnimatedVisibility(visible = openDialog, enter = scaleIn(), exit = scaleOut()) {
        Box (
            contentAlignment = Alignment.Center,
            modifier = modifier.fillMaxSize()
        ) {
            Surface(
                color = Palette.Glass00,
                shape = RoundedCornerShape(if (screenWidth > ScreenSizeThresholds.StopStretchingYesNoDialogToScreenEdge)
                    GameScreenDialogBoxStyle.OuterCornerRounding else 0.dp
                ),
                contentColor = Palette.FullWhite,
//                border = BorderStroke(
//                    GameScreenDialogBoxStyle.OutlineThickness,
//                    Palette.Abyss90.compositeOver(Palette.FillLightPrimary)
//                ),
//                elevation = GameScreenDialogBoxStyle.Elevation,
                modifier = modifier.padding(
                    horizontal = if (screenWidth > ScreenSizeThresholds.StopStretchingYesNoDialogToScreenEdge)
                        GameScreenDialogBoxStyle.StretchedDialogOffsetFromEdge else 0.dp,
                    vertical = 0.dp
                )
            ) {
                Column (horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(title),
                        textAlign = TextAlign.Center,
                        fontSize = GameScreenDialogBoxStyle.TitleTextSize,
                        lineHeight = GameScreenDialogBoxStyle.TitleTextSize,
                        modifier = modifier
                            .padding(GameScreenDialogBoxStyle.InnerPadding)
                    )
                    if (screenWidth > ScreenSizeThresholds.StopStackingYesNoDialogVertically) {
                        Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                            RowMenuButton(
                                onClick = onDecline,
                                label = declineLabel,
                                width = GameScreenDialogBoxStyle.YesNoDialogButtonMaxWidth,
                                severity = declineSeverity,
                                modifier = modifier
                            )
                            RowMenuButton(
                                onClick = onAccept,
                                label = acceptLabel,
                                width = GameScreenDialogBoxStyle.YesNoDialogButtonMaxWidth,
                                severity = acceptSeverity,
                                modifier = modifier
                            )
                        }
                    } else {
                        Column (
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = modifier.padding(GameScreenDialogBoxStyle.InnerPadding)
                        ) {
                            MenuButton(onAccept, acceptLabel, acceptSeverity)
                            MenuButton(onDecline, declineLabel, declineSeverity)
                        }
                    }
                }
            }
        }
    }
}
