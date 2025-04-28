package app.trailblazercombi.haventide.matchLoad.jetpack

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import app.trailblazercombi.haventide.AppScreens
import app.trailblazercombi.haventide.Global
import app.trailblazercombi.haventide.netcode.*
import app.trailblazercombi.haventide.resources.Res
import app.trailblazercombi.haventide.resources.diagnostic_cannot_pair_to_itself
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import kotlin.random.Random

//import app.trailblazercombi.haventide.netcode.NetworkResolver.pairYang
//import app.trailblazercombi.haventide.netcode.NetworkResolver.pairYin
//import app.trailblazercombi.haventide.netcode.NetworkResolver.sendMessage
//import app.trailblazercombi.haventide.netcode.NetworkResolver.startListening
//import app.trailblazercombi.haventide.netcode.NetworkResolver.unpair


@OptIn(ExperimentalResourceApi::class)
@Composable
fun MatchBeginScreen(navController: NavHostController, modifier: Modifier = Modifier) {

    val gameLoop by Global.gameLoop.collectAsState()
    val paired by TcpClient.paired.collectAsState()
    val gameStarting by Handshaker.gameIsRequested.collectAsState()

    val latestMsg by TcpServer.latestMessage.collectAsState()
    val latestServer by TcpClient.latestMessage.collectAsState()
    val serverRunning by TcpServer.serverRunning.collectAsState()

    val inputCode = remember { mutableStateOf("") }
    val message = remember { mutableStateOf("") }

    val createGameScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    var createGameJob: Job? = null
    var waitingJob: Job? = null

    var diagnosticMessage: StringResource? by remember { mutableStateOf(null) }

    Column (modifier.padding(32.dp)) {
        TextField(
            enabled = !paired,
            value = inputCode.value,
            onValueChange = { inputCode.value = it },
            label = { Text("Enter pairing code") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            enabled = !paired,
            value = NetPairing.inetToCode(NetPairing.localInet()),
            onValueChange = {},
            label = { Text("Your pairing code") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            enabled = !paired,
            onClick = {
                val inetAddress = NetPairing.codeToInet(inputCode.value)
                if (inetAddress == NetPairing.localInet()) {
                    diagnosticMessage = Res.string.diagnostic_cannot_pair_to_itself
                    return@Button
                }
                startTcpClient(inetAddress)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Pair!")
        }

        Button(
            enabled = paired && !gameStarting,
            onClick = {
                createGameJob = createGameScope.launch {
                    val mapName = Handshaker.randomMapName()
                    val mePlayer = Global.localPlayer
                    val meStart = Random.nextBoolean()
                    TcpClient.sendToRemoteServer("GEEMU_START $mapName ${mePlayer.rosterAsPacket()} $meStart")
                    waitingJob = Handshaker.requestGameFromLocal(mapName, mePlayer, meStart)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Start Game!")
        }

        LaunchedEffect(gameLoop) {
            if (gameLoop != null) navController.navigate(AppScreens.GameScreen.name) {
                popUpTo(AppScreens.MatchStart.name) { inclusive = true }
            }
            diagnosticMessage = null
        }

        Button(
            enabled = !serverRunning,
            onClick = { startTcpServer() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Start Server")
        }

        Text(text = if (diagnosticMessage == null) "" else stringResource(diagnosticMessage!!), modifier = modifier.fillMaxWidth())
    }
}
