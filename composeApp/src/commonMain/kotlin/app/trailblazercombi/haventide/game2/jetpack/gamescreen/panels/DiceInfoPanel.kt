package app.trailblazercombi.haventide.game2.jetpack.gamescreen.panels

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.trailblazercombi.haventide.game2.jetpack.gamescreen.components.buttons.ConfirmAbilityButton
import app.trailblazercombi.haventide.game2.jetpack.gamescreen.components.buttons.EndRoundButton
import app.trailblazercombi.haventide.game2.jetpack.gamescreen.components.dice.DiceCounter
import app.trailblazercombi.haventide.game2.jetpack.gamescreen.components.dice.StackOfDice
import app.trailblazercombi.haventide.game2.viewModel.GameLoopViewModel
import app.trailblazercombi.haventide.resources.*

@Composable
fun DiceInfoPanel(viewModel: GameLoopViewModel, modifier: Modifier = Modifier) {
    val screenWidth by viewModel.screenWidth.collectAsState()
    val abilityPreview by viewModel.abilityPreview.collectAsState()
    val leftCiInfo by viewModel.leftCiInfo.collectAsState()

//    Box (
//        contentAlignment = Alignment.BottomCenter,
//        modifier = modifier.fillMaxSize()
//    ) {
        Surface(
            shape = RoundedCornerShape(
                topStart =
                    if (leftCiInfo != null) 0.dp
                    else if (screenWidth > ScreenSizeThresholds.FloatBottomBarAsBubble) CiStyle.BubbleModeCornerRounding
                    else 0.dp,
                topEnd =
                    if (abilityPreview != null) 0.dp
                    else if (screenWidth > ScreenSizeThresholds.FloatBottomBarAsBubble) CiStyle.BubbleModeCornerRounding
                    else 0.dp,
                bottomStart =
                    if (screenWidth > ScreenSizeThresholds.FloatBottomBarAsBubble) CiStyle.BubbleModeCornerRounding
                    else 0.dp,
                bottomEnd =
                    if (screenWidth > ScreenSizeThresholds.FloatBottomBarAsBubble) CiStyle.BubbleModeCornerRounding
                    else 0.dp,
            ),
            color = GameScreenTopBubbleStyle.FillColorModifier,
            contentColor = Palette.FullWhite,
            border = BorderStroke(
                GameScreenTopBubbleStyle.OutlineThickness,
                GameScreenTopBubbleStyle.OutlineColorModifier
            ),
            elevation = GameScreenTopBubbleStyle.Elevation,
            modifier = modifier.let {
                if (screenWidth > ScreenSizeThresholds.MaxBottomBarWidth)
                    it.width(ScreenSizeThresholds.MaxBottomBarWidth)
                else it.fillMaxWidth()
            }
        ) {
            Box (
                contentAlignment = Alignment.Center,
//                contentPadding = PaddingValues(GameScreenTopBubbleStyle.InnerOffset)
            ) {
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = modifier
                        .padding(
                            horizontal = GameScreenTopBubbleStyle.InnerOffset,
                            vertical = GameScreenTopBubbleStyle.InnerOffset - DieStyle.Separation / 2
                        )
                        .fillMaxWidth()
                ) {
                    StackOfDice(
                        viewModel = viewModel,
                        diceStack = viewModel.localDiceList,
                        dicePerRow = if (screenWidth > ScreenSizeThresholds.SpreadDiceStackOnSingleLine) 8 else 4,
                        onDieClicked = viewModel::processDieClickEvent
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = modifier
                        .padding(
                            horizontal = GameScreenTopBubbleStyle.InnerOffset - DieStyle.Separation / 2,
                            vertical = GameScreenTopBubbleStyle.InnerOffset - DieStyle.Separation / 2
                        )
                        .fillMaxWidth()
                ) {
                    if (abilityPreview != null) {
                        DiceCounter(viewModel)
                        ConfirmAbilityButton(viewModel)
                    } else {
                        EndRoundButton(viewModel)
                    }
                }
            }
        }
//    }
}
