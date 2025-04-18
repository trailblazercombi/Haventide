package app.trailblazercombi.haventide.game2.jetpack.gamescreen.panels

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.trailblazercombi.haventide.game.arena.GameLoopViewModel
import app.trailblazercombi.haventide.game2.jetpack.extensions.intrinsicWidth
import app.trailblazercombi.haventide.game2.jetpack.gamescreen.panels.components.PhoenixMiniatureStrip
import app.trailblazercombi.haventide.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun GameStatusPanel(viewModel: GameLoopViewModel, modifier: Modifier = Modifier) {
    val roundCount by viewModel.roundCount.collectAsState()
    val backdropColor by viewModel.backdropColor.collectAsState()

    val screenWidth by viewModel.screenWidth.collectAsState()

    val localPlayerTurn by viewModel.localPlayerTurn.collectAsState()
    val allies by viewModel.alliedPhoenixEntries.collectAsState()
    val enemies by viewModel.enemyPhoenixEntries.collectAsState()

    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = modifier
            .padding(
                if (screenWidth > ScreenSizeThresholds.FloatTopStatusBarAsBubble) GameScreenTopBubbleStyle.OffsetFromEdge
                else 0.dp
            )
            .fillMaxSize()
    ) {
        Surface(
            shape = if (screenWidth > ScreenSizeThresholds.FloatTopStatusBarAsBubble) RoundedCornerShape(
                GameScreenTopBubbleStyle.CornerRounding
            ) else RoundedCornerShape(0.dp),
            color = GameScreenTopBubbleStyle.FillColorModifier.compositeOver(backdropColor),
            contentColor = Palette.FullWhite,
            border = BorderStroke(
                GameScreenTopBubbleStyle.OutlineThickness,
                GameScreenTopBubbleStyle.OutlineColorModifier.compositeOver(backdropColor)
            ),
            elevation = GameScreenTopBubbleStyle.Elevation,
            modifier = modifier
                .clickable(
                    indication = LocalIndication.current,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = { viewModel.pauseMenuDialog.value = true }
                )
                .intrinsicWidth()
                .height(
                    if (screenWidth >
                        ScreenSizeThresholds.FloatTopStatusBarAsBubble) GameScreenTopBubbleStyle.BubbleHeight
                    else GameScreenTopBubbleStyle.DoubleBubbleHeight,
                )
        ) {
            Box( // ALLIES
                contentAlignment = Alignment.BottomStart,
                modifier = modifier
                    .fillMaxHeight()
                    .padding(0.dp, GameScreenTopBubbleStyle.InnerOffset)
            ) {
                PhoenixMiniatureStrip(allies, viewModel)
            }
            Box( // ENEMIES
                contentAlignment = Alignment.BottomEnd,
                modifier = modifier
                    .fillMaxHeight()
                    .padding(0.dp, GameScreenTopBubbleStyle.InnerOffset)
            ) {
                PhoenixMiniatureStrip(enemies, viewModel)
            }
            // HERE STARTS THE ROUND COUNTER / TEAM TO MOVE DISPLAY
            if (screenWidth > ScreenSizeThresholds.FloatTopStatusBarAsBubble) {
                Column(
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = stringResource(Res.string.game_turn_state_round_counter, roundCount),
                        textAlign = TextAlign.Center,
                        fontSize = GameScreenTopBubbleStyle.RoundCounterTextSize,
                        lineHeight = GameScreenTopBubbleStyle.RoundCounterTextSize,
                        color = Palette.FullWhite,
                        modifier = modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(
                                GameScreenTopBubbleStyle.InnerOffset,
                                GameScreenTopBubbleStyle.InnerOffset / 2,
                                GameScreenTopBubbleStyle.InnerOffset,
                                0.dp
                            )
                    )
                    Text(
                        text = stringResource(localPlayerTurn.string, localPlayerTurn.retrieve()),
                        textAlign = TextAlign.Center,
                        fontSize = GameScreenTopBubbleStyle.TeamTurnTextSize,
                        lineHeight = GameScreenTopBubbleStyle.TeamTurnTextSize,
                        color = Palette.FullWhite,
                        modifier = modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(
                                start = GameScreenTopBubbleStyle.InnerOffset,
                                top = 0.dp,
                                end = GameScreenTopBubbleStyle.InnerOffset,
                                bottom = GameScreenTopBubbleStyle.InnerOffset / 2,
                            )
                    )
                }
            } else {
                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = stringResource(Res.string.game_turn_state_round_counter, roundCount) +
                                " / ${stringResource(localPlayerTurn.string, localPlayerTurn.retrieve())}",
                        textAlign = TextAlign.Center,
                        fontSize = GameScreenTopBubbleStyle.UnifiedRoundTeamTextSize,
                        lineHeight = GameScreenTopBubbleStyle.UnifiedRoundTeamTextSize,
                        color = Palette.FullWhite,
                        modifier = modifier
                            .padding(0.dp, GameScreenTopBubbleStyle.InnerOffset)
                    )
                }
            }
        }
    }
}
