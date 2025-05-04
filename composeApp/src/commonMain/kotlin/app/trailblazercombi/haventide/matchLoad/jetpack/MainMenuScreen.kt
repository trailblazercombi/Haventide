package app.trailblazercombi.haventide.matchLoad.jetpack

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import app.trailblazercombi.haventide.AppScreens
import app.trailblazercombi.haventide.matchLoad.jetpack.components.PhoenixSelectCard
import app.trailblazercombi.haventide.resources.ButtonSeverity
import app.trailblazercombi.haventide.resources.MainMenuStyle
import app.trailblazercombi.haventide.resources.MechanismTemplate
import app.trailblazercombi.haventide.resources.PhoenixTemplates
import app.trailblazercombi.haventide.resources.*
import kotlinx.coroutines.flow.MutableStateFlow
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

val activeRoster = MutableStateFlow(listOf<MechanismTemplate.Phoenix>())

@Composable
fun MainMenuScreen(navController: NavController, modifier: Modifier = Modifier) {
    val activeRoster by activeRoster.collectAsState()
    val scrollState = rememberLazyGridState()

    Box(contentAlignment = Alignment.Center, modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(Res.drawable.background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alignment = Alignment.BottomCenter,
            modifier = modifier.fillMaxSize()
        )

        Column {
            Box(
                contentAlignment = Alignment.TopCenter,
                modifier = modifier.fillMaxWidth().padding(top = MainMenuStyle.StartGameButtonOffsetFromEdge)
            ) {
                Text(
                    text = stringResource(Res.string.select_and_start),
                    color = Palette.FillYellow,
                    fontSize = GameScreenDialogBoxStyle.LargeTextSize,
                    textAlign = TextAlign.Center,
                    lineHeight = GameScreenDialogBoxStyle.LargeTextSize,
                    overflow = TextOverflow.Ellipsis,
                    modifier = modifier.fillMaxWidth()
                )
            }

            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = MainMenuStyle.PhoenixCardMinimumSize),
                state = scrollState,
                contentPadding = PaddingValues(MainMenuStyle.PhoenixCardSeparation / 2),
                verticalArrangement = Arrangement.Center,
                horizontalArrangement = Arrangement.Center,
                modifier = modifier.weight(1f).padding(
                    top = MainMenuStyle.PhoenixCardInnerPadding,
                    bottom = MainMenuStyle.StartGameButtonOffsetFromEdge
                        + MainMenuStyle.StartGameButtonHeight + MainMenuStyle.PhoenixCardInnerPadding)
            ) {
                items(PhoenixTemplates.entries) {
                    PhoenixSelectCard(it.template)
                }
            }
        }
            Box(
                contentAlignment = Alignment.BottomCenter,
                modifier = modifier.fillMaxSize().padding(bottom = MainMenuStyle.StartGameButtonOffsetFromEdge)
            ) {
                Button(
                    shape = RoundedCornerShape(MainMenuStyle.StartGameButtonRounding),
                    enabled = activeRoster.size == 3,
                    onClick = { navController.navigate(AppScreens.MatchStart.name) },
                    border = BorderStroke(
                        MainMenuStyle.StartGameButtonOutlineThickness, ButtonSeverity.PREFERRED.outlineColor
                    ),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = ButtonSeverity.PREFERRED.fillColor,
                        contentColor = ButtonSeverity.PREFERRED.contentColor,
                    ),
                    modifier = modifier.width(MainMenuStyle.StartGameButtonWidth)
                        .height(MainMenuStyle.StartGameButtonHeight),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = stringResource(Res.string.start),
                        fontSize = MainMenuStyle.StartGameButtonTextSize,
                    )
                }
            }
        }
    }

fun toggle(phoenix: MechanismTemplate.Phoenix) {
    val mutableRoster = activeRoster.value.toMutableList()
    activeRoster.value = mutableRoster.let {
        if (phoenix in it) it.remove(phoenix)
        else it.add(phoenix)
        it.toList()
    }
}
