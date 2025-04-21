package app.trailblazercombi.haventide.matchLoad.jetpack

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import app.trailblazercombi.haventide.GlobalStateHolder
import app.trailblazercombi.haventide.matchLoad.data.MatchInfo
import app.trailblazercombi.haventide.netcode.*
import app.trailblazercombi.haventide.netcode.NetworkStatus.*
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
@Composable
fun MatchBeginScreen(navController: NavHostController, matchInfo: MatchInfo) {

    val netStat by GlobalStateHolder.networkStatus.collectAsState()
    val lastMsg by GlobalStateHolder.lastMessage.collectAsState()

    val scope = rememberCoroutineScope()

    var netAlreadyRunning by remember { mutableStateOf(false) }

    scope.launch {
        sendBroadcast(message = "Hey, let's play a game!")
        listenToBroadcast {
            GlobalStateHolder.networkStatus.value = Connected(it)
            // TODO: You have the IP address syncing now, so make use of it for purposes beyond you...
//            GlobalStateHolder.tcpListener.value = TcpListener()
//            GlobalStateHolder.tcpSpeaker.value = TcpSpeaker((netStat as? NetworkStatus.Connected)!!.peerInet4)
        }
    }

    Column {
        Text("Current network status: $netStat")
        Text("Diect network traffic status: $lastMsg")
    }

//    LaunchedEffect(Unit) {
//        val rawData = Res.readBytes(matchInfo.matchMap).decodeToString()
//        val gameLoop = GameLoop(
//            mapData = rawData,
//            localPlayer = matchInfo.localPlayer,
//            remotePlayer = matchInfo.remotePlayer,
//            localPlayerStarts = matchInfo.localPlayerStarts,
//        )
//        GlobalStateHolder.currentMatch.value = MatchState.Ready(gameLoop)
//        navController.navigate(AppScreens.MatchPlaying.name) {
//            popUpTo(AppScreens.MatchStart.name) { inclusive = true }
//        }
//    }
}
