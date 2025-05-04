package app.trailblazercombi.haventide

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
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
import org.jetbrains.compose.resources.painterResource

@Composable
@Preview
fun App(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier,
) {
    val gameLoop by Global.gameLoop.collectAsState()

    Box(contentAlignment = Alignment.Center, modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(Res.drawable.background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alignment = Alignment.BottomCenter,
            modifier = modifier.fillMaxSize()
        )
        NavHost(
            navController = navController,
            startDestination = AppScreens.MainMenu.name,
            modifier = modifier.fillMaxSize(),
        ) {
            composable(route = AppScreens.MainMenu.name) {
                MainMenuScreen(navController)
            }
            composable(route = AppScreens.MatchStart.name) {
                MatchBeginScreen(navController)
            }
            composable(route = AppScreens.GameScreen.name) {
                val viewModel = gameLoop?.viewModel ?: return@composable
                GameScreen(viewModel, navController)
            }
        }
    }
}

enum class AppScreens(val title: StringResource) {
    MainMenu(Res.string.nav_main_menu),
    MatchStart(Res.string.nav_match_start),
    GameScreen(Res.string.nav_match_playing)
}
