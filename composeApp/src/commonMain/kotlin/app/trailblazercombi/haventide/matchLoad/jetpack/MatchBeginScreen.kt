package app.trailblazercombi.haventide.matchLoad.jetpack

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import app.trailblazercombi.haventide.GlobalState
import org.jetbrains.compose.resources.ExperimentalResourceApi
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import app.trailblazercombi.haventide.netcode.*
import app.trailblazercombi.haventide.netcode.NetworkResolver.pairYang
import app.trailblazercombi.haventide.netcode.NetworkResolver.pairYin
import app.trailblazercombi.haventide.netcode.NetworkResolver.sendMessage
import app.trailblazercombi.haventide.netcode.NetworkResolver.startListening
import app.trailblazercombi.haventide.netcode.NetworkResolver.unpair
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalResourceApi::class)
@Composable
fun MatchBeginScreen(navController: NavHostController) {

//    val pairingMode by NetworkState.pairingMode.collectAsState()
//    val pairedPeer by NetworkState.pairedPeer.collectAsState()

    val pairingNow by NetworkResolver.pairingNow.collectAsState()
    val paired by NetworkResolver.paired.collectAsState()

    val gameLoop by GlobalState.currentGame.collectAsState()

    val scope = rememberCoroutineScope()

    Column {
        Button(
            enabled = !paired && !pairingNow,
            onClick = {
                scope.launch {
                    pairYin()
                    delay(1000)
                    startListening()
                }
            }) {
            Text("Pair Yin")
        }

        Button(
            enabled = !paired && !pairingNow,
            onClick = {
                scope.launch {
                    pairYang()
                    delay(1000)
                    startListening()
                }
            }) {
            Text("Pair Yang")
        }

        Button(
            enabled = paired && !pairingNow,
            onClick = {
                scope.launch {
                    sendMessage("Hello world!")
                }
            }) {
            Text("Message")
        }

        Button(
            enabled = paired || pairingNow,
            onClick = {
                scope.launch {
                    unpair()
                }
            }) {
            Text("Stop")
        }

        LaunchedEffect(gameLoop) {
            if (gameLoop == null) return@LaunchedEffect
            println("[MI] LET'S FUCKING GOO")
        }

//        Button(
//            enabled = paired && !pairingNow,
//            onClick = {
//                scope.launch {
//
//                    // Step 1: Negotiate map + rosters + starting player
//                    if (initiator ?: return@launch) {
//                        val map = "files/maps/parkingLot"
//                        val iStart = Random.nextBoolean()
//                        val localPlayer = PlaceholderPlayers.PLAYER_ONE.toProfile()
//
//                        delay(2500)
//                        sendMessage("HAVENTIDE_PLAY parkingLot ${PlaceholderPlayers.PLAYER_ONE.toProfile()} $iStart END")
//                        addParsingPattern("HAVENTIDE_PLAY_ACK") { argv ->
//                            GlobalState.latestMatchInfo.value = MatchInfo(
//                                matchMap = map,
//                                localPlayer = localPlayer,
//                                remotePlayer = PlayerProfile(argv[0]),
//                                localPlayerStarts = iStart
//                            )
//                        }
//                    } else {
//                        val localPlayer = PlaceholderPlayers.PLAYER_ONE.toProfile()
//                        addParsingPattern("HAVENTIDE_PLAY") { argv ->
//                            GlobalState.latestMatchInfo.value = MatchInfo(
//                                matchMap = argv[0],
//                                localPlayer = localPlayer,
//                                remotePlayer = PlayerProfile(argv[1]),
//                                localPlayerStarts = !(argv[2].toBoolean())
//                            )
//                            delay(300)
//                            sendMessage("HAVENTIDE_PLAY_ACK ${GlobalState.latestMatchInfo.value!!.localPlayer} END")
//                        }
//                    }
//
//                    // Step 2: Start the game!
//                }
//            }) {
//            Text("Enter Game!")
//        }
//
//        LaunchedEffect(matchInfo) {
//            val rawData = Res.readBytes(matchInfo?.matchMap ?: return@LaunchedEffect).decodeToString()
//            val gameLoop = GameLoop(
//                mapData = rawData,
//                localPlayer = matchInfo!!.localPlayer,
//                remotePlayer = matchInfo!!.remotePlayer,
//                localPlayerStarts = matchInfo!!.localPlayerStarts,
//            )
//            GlobalState.currentMatch.value = gameLoop
//            navController.navigate(AppScreens.MatchPlaying.name) {
//                popUpTo(AppScreens.MatchStart.name) { inclusive = true }
//            }
//        }
    }
}
