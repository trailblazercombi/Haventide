package app.trailblazercombi.haventide.game2.jetpack.gamescreen.dialogs

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import app.trailblazercombi.haventide.AppScreens
import app.trailblazercombi.haventide.Global
import app.trailblazercombi.haventide.game2.jetpack.gamescreen.components.misc.TapToDismissLabel
import app.trailblazercombi.haventide.game2.jetpack.universal.DialogGenerics
import app.trailblazercombi.haventide.game2.viewModel.GameLoopViewModel
import app.trailblazercombi.haventide.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun GameOverDialog(
    viewModel: GameLoopViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val openDialog by viewModel.gameOverDialog.collectAsState()
    val gameResult by viewModel.gameResult.collectAsState()

    val screenWidth by viewModel.screenWidth.collectAsState()

    DialogGenerics(
        openDialogState = viewModel.gameOverDialog,
        onDismissRequest = {
            if (openDialog) {
                Global.gameLoop.value = null
                navController.navigate(AppScreens.MainMenu.name) {
                    popUpTo(AppScreens.GameScreen.name) { inclusive = true }
                    // TODO Make the back button on Android work
                    //  and the Escape key on Windows too...
                }
            }
        },
    )

    AnimatedVisibility(openDialog, enter = scaleIn(), exit = scaleOut()) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .fillMaxSize()
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = modifier
            ) {
                Surface(
                    color = Palette.Abyss90.compositeOver(gameResult.color),
                    shape = RoundedCornerShape(
                        if (screenWidth > ScreenSizeThresholds.StopStretchingGameOverDialogToScreenEdge)
                            GameScreenDialogBoxStyle.OuterCornerRounding
                        else 0.dp
                    ),
                    contentColor = Palette.FullWhite,
                    border = BorderStroke(GameScreenDialogBoxStyle.OutlineThickness, gameResult.color),
                    elevation = GameScreenDialogBoxStyle.Elevation,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = if (screenWidth >
                                ScreenSizeThresholds.StopStretchingGameOverDialogToScreenEdge)
                                screenWidth / 4
//                            GameScreenDialogBoxStyle.StretchedDialogOffsetFromEdge
                            else 0.dp,
                            vertical = 0.dp
                        )
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = modifier
                            .padding(GameScreenDialogBoxStyle.GameOverInnerPadding),
                    ) {
                        Text(
                            text = stringResource(resource = gameResult.string),
                            color = Palette.FullWhite,
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = GameScreenDialogBoxStyle.LargeTextSize,
                                lineHeight = GameScreenDialogBoxStyle.LargeTextLineSeparation
                            ),
                            textAlign = TextAlign.Center,
                            modifier = modifier
                                .fillMaxWidth()
                        )
                    }
                }
            }
        }
    }

    TapToDismissLabel(viewModel.gameOverDialog)
}
