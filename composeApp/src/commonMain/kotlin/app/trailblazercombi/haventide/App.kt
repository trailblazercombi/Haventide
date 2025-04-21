package app.trailblazercombi.haventide

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalOf
import app.trailblazercombi.haventide.game2.data.GameLoop
import app.trailblazercombi.haventide.game2.jetpack.gamescreen.GameScreen
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import app.trailblazercombi.haventide.matchLoad.data.MatchInfo
import app.trailblazercombi.haventide.matchLoad.data.MatchState
import app.trailblazercombi.haventide.matchLoad.jetpack.MatchBeginScreen
import app.trailblazercombi.haventide.matchLoad.jetpack.MatchResultScreen
import app.trailblazercombi.haventide.resources.*
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
@Preview
fun App(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier,
) {
    val currentMatch by GlobalStateHolder.currentMatch.collectAsState()

    NavHost(
        navController = navController,
        startDestination = AppScreens.MatchStart.name,
        modifier = modifier.fillMaxSize(),
    ) {
        composable (route = AppScreens.MatchStart.name) {
            MatchBeginScreen(
                navController,
                MatchInfo(
                    matchMap = "files/maps/parkingLot",
                    localPlayer = PlaceholderPlayers.PLAYER_ONE.toProfile(),
                    remotePlayer = PlaceholderPlayers.PLAYER_TWO.toProfile(),
                    localPlayerStarts = true
                )
            )
        }
        composable (route = AppScreens.MatchPlaying.name) {
            GameScreen(
                (currentMatch as? MatchState.Ready)?.gameLoop?.viewModel ?:
                throw IllegalStateException("Cannot create a GameScreen when the GameLoop is not ready"),
                navController
            )
        }
        composable (route = AppScreens.MatchResult.name) {
            MatchResultScreen(
                (currentMatch as? MatchState.Ready)?.gameLoop?.gameResult ?:
                throw IllegalStateException("Cannot create a MatchResult screen when the match is ong"),
            )
        }
    }
}

enum class AppScreens(val title: StringResource) {
    MainMenu(Res.string.nav_main_menu),
    MatchStart(Res.string.nav_match_start),
    MatchPlaying(Res.string.nav_match_playing),
    MatchResult(Res.string.nav_match_result)
}
