package app.trailblazercombi.haventide.matchLoad.jetpack

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import app.trailblazercombi.haventide.AppScreens
import app.trailblazercombi.haventide.Global
import app.trailblazercombi.haventide.matchLoad.jetpack.components.PairingField
import app.trailblazercombi.haventide.netcode.*
import app.trailblazercombi.haventide.resources.ButtonSeverity
import app.trailblazercombi.haventide.resources.MainMenuStyle
import app.trailblazercombi.haventide.resources.MatchBeginStyle
import app.trailblazercombi.haventide.resources.Palette
import app.trailblazercombi.haventide.resources.Res
import app.trailblazercombi.haventide.resources.background
import app.trailblazercombi.haventide.resources.diagnostic_cannot_pair_to_itself
import app.trailblazercombi.haventide.resources.start_button_cancel
import app.trailblazercombi.haventide.resources.start_button_pair
import app.trailblazercombi.haventide.resources.start_button_start
import app.trailblazercombi.haventide.resources.start_pair_foreign
import app.trailblazercombi.haventide.resources.start_pair_local
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.random.Random

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

    var screenWidth by remember { mutableStateOf(0.dp) }
    var screenHeight by remember { mutableStateOf(0.dp) }

    val thisActiveRoster by activeRoster.collectAsState()

    // This just figures out the current screen size...
    BoxWithConstraints(Modifier.fillMaxSize()) {
        LaunchedEffect(maxWidth, maxHeight) {
            screenWidth = maxWidth
            screenHeight = maxHeight
            println("[GS WiM] $maxWidth x $maxHeight")
        }
    }

    Box(contentAlignment = Alignment.Center, modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(Res.drawable.background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alignment = Alignment.BottomCenter,
            modifier = modifier.fillMaxSize()
        )

        Column(modifier.padding(MatchBeginStyle.PaddingFromScreenEdge)) {
            Surface(
                shape = RoundedCornerShape(MainMenuStyle.PhoenixCardOuterRounding),
                color = Palette.Abyss60,
                border = BorderStroke(0.dp, Palette.Abyss80),
                contentColor = Palette.FullWhite,
                modifier = modifier.padding(MatchBeginStyle.PaddingFromScreenEdge)
            ) {
                Column {
                    PairingField(
                        enabled = false,
                        value = NetPairing.inetToCode(NetPairing.localInet()),
                        onValueChange = {},
                        contentColor = Palette.FullWhite,
                        label = stringResource(Res.string.start_pair_local),
                        modifier = modifier.fillMaxWidth()
                    )
                    PairingField(
                        enabled = !paired,
                        value = inputCode.value,
                        onValueChange = { inputCode.value = it },
                        contentColor = Palette.FillYellow,
                        label = stringResource(Res.string.start_pair_foreign),
                        modifier = modifier.fillMaxWidth()
                    )
                }
            }
        }
    }

    Box(
        contentAlignment = Alignment.TopStart,
        modifier = modifier.padding(MatchBeginStyle.PaddingFromScreenEdge)
    ) {
        Button(
            enabled = true,
            onClick = {
                stopTcpClient()
                stopTcpServer()
                navController.popBackStack()
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = ButtonSeverity.NEUTRAL.fillColor,
                contentColor = ButtonSeverity.NEUTRAL.contentColor,
            ),
            border = BorderStroke(
                MainMenuStyle.StartGameButtonOutlineThickness, ButtonSeverity.NEUTRAL.outlineColor
            )
        ) {
            Text(stringResource(Res.string.start_button_cancel))
        }
    }

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = modifier.fillMaxSize().padding(bottom = MainMenuStyle.StartGameButtonOffsetFromEdge)
    ) {
        if (!paired) {
            Button(
                onClick = {
                    val inetAddress = NetPairing.codeToInet(inputCode.value)
                    if (inetAddress == NetPairing.localInet()) {
                        diagnosticMessage = Res.string.diagnostic_cannot_pair_to_itself
                        return@Button
                    }
                    startTcpClient(inetAddress)
                },
                shape = RoundedCornerShape(MainMenuStyle.StartGameButtonRounding),
                border = BorderStroke(
                    MainMenuStyle.StartGameButtonOutlineThickness, ButtonSeverity.NEUTRAL.outlineColor
                ),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = ButtonSeverity.NEUTRAL.fillColor,
                    contentColor = ButtonSeverity.NEUTRAL.contentColor,
                ),
                modifier = modifier.width(MainMenuStyle.StartGameButtonWidth)
                    .height(MainMenuStyle.StartGameButtonHeight),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = stringResource(Res.string.start_button_pair),
                    fontSize = MainMenuStyle.StartGameButtonTextSize
                )
            }
        } else if (!gameStarting) {
            Button(
                onClick = {
                    createGameJob = createGameScope.launch {
                        val mapName = Handshaker.randomMapName()
                        val mePlayer = Global.localPlayer
                        mePlayer.activeRoster = thisActiveRoster
                        val meStart = Random.nextBoolean()
                        TcpClient.sendToRemoteServer("GEEMU_START $mapName ${mePlayer.rosterAsPacket()} $meStart")
                        waitingJob = Handshaker.requestGameFromLocal(mapName, mePlayer, meStart)
                    }
                },
                shape = RoundedCornerShape(MainMenuStyle.StartGameButtonRounding),
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
                    text = stringResource(Res.string.start_button_start,),
                    fontSize = MainMenuStyle.StartGameButtonTextSize
                )
            }
        }
    }

    LaunchedEffect(gameLoop) {
        if (gameLoop != null) navController.navigate(AppScreens.GameScreen.name) {
            popUpTo(AppScreens.MatchStart.name) { inclusive = true }
        }
        diagnosticMessage = null
    }

    LaunchedEffect(serverRunning) {
        if (!serverRunning) startTcpServer()
    }
}
