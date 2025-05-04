package app.trailblazercombi.haventide

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import app.trailblazercombi.haventide.game2.jetpack.gamescreen.GameScreen
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import app.trailblazercombi.haventide.matchLoad.jetpack.MainMenuScreen
import app.trailblazercombi.haventide.matchLoad.jetpack.MatchBeginScreen
//import app.trailblazercombi.haventide.matchLoad.jetpack.MatchResultScreen
import app.trailblazercombi.haventide.resources.*

@Composable
@Preview
fun App(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier,
) {
    val gameLoop by Global.gameLoop.collectAsState()

    NavHost(
        navController = navController,
        startDestination = AppScreens.MainMenu.name,
        modifier = modifier.fillMaxSize(),
    ) {
        composable (route = AppScreens.MainMenu.name) {
            MainMenuScreen(navController)
        }
        composable (route = AppScreens.MatchStart.name) {
            MatchBeginScreen(navController)
        }
        composable (route = AppScreens.GameScreen.name) {
            val viewModel = gameLoop?.viewModel ?: return@composable
            GameScreen(viewModel, navController)
        }
//        composable (route = AppScreens.MatchResult.name) {
//            MatchResultScreen(
//                gameLoop?.gameResult
//                    ?: throw IllegalStateException("Cannot create a MatchResult screen while the match is ongoing"),
//            )
//        }
    }
}

enum class AppScreens(val title: StringResource) {
    MainMenu(Res.string.nav_main_menu),
    MatchStart(Res.string.nav_match_start),
    GameScreen(Res.string.nav_match_playing),
//    MatchResult(Res.string.nav_match_result)
}
