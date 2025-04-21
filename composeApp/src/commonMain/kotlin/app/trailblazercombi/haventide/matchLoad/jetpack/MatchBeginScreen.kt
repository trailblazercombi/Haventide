package app.trailblazercombi.haventide.matchLoad.jetpack

import androidx.compose.material.Text
import androidx.compose.material.Button
import androidx.compose.runtime.*
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import app.trailblazercombi.haventide.AppScreens
import app.trailblazercombi.haventide.GlobalStateHolder
import app.trailblazercombi.haventide.game2.data.GameLoop
import app.trailblazercombi.haventide.matchLoad.data.MatchInfo
import app.trailblazercombi.haventide.matchLoad.data.MatchState
import app.trailblazercombi.haventide.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
@Composable
fun MatchBeginScreen(navController: NavHostController, matchInfo: MatchInfo) {

    Text("Loading...")

    LaunchedEffect(Unit) {
        val rawData = Res.readBytes(matchInfo.matchMap).decodeToString()
        val gameLoop = GameLoop(
            mapData = rawData,
            localPlayer = matchInfo.localPlayer,
            remotePlayer = matchInfo.remotePlayer,
            localPlayerStarts = matchInfo.localPlayerStarts,
        )
        GlobalStateHolder.currentMatch.value = MatchState.Ready(gameLoop)
        navController.navigate(AppScreens.MatchPlaying.name) {
            popUpTo(AppScreens.MatchStart.name) { inclusive = true }
        }
    }
}
