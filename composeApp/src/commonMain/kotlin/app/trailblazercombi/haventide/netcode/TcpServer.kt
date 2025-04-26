package app.trailblazercombi.haventide.netcode

import androidx.lifecycle.viewmodel.compose.viewModel
import app.trailblazercombi.haventide.Global
import app.trailblazercombi.haventide.game2.jetpack.gamescreen.GameScreen
import app.trailblazercombi.haventide.playerdata.PlayerProfile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.PrintWriter
import java.net.ServerSocket

object TcpServer {

    private var input: InputStream? = null
    private var reader: BufferedReader? = null

    private var output: OutputStream? = null
    private var writer: PrintWriter? = null

    private var serverSocket: ServerSocket? = null
    private val socketScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var serverJob: Job? = null

    val serverRunning = MutableStateFlow(false)
    val latestMessage = MutableStateFlow("IDLE")

    private var waitingScope = CoroutineScope(Dispatchers.Default)
    private var waitingJob: Job? = null

    @JvmStatic
    fun main(args: Array<String>) {
        try {
            serverRunning.value = true
            // Create a server socket that listens on port 12345
            serverSocket = ServerSocket(12345)
            println("[TCPS] Server is listening on port 12345...")

            // Wait for a connection from a client
            val socket = serverSocket?.accept() ?: return

            val address = socket.getInetAddress()
            println("[TCPS]Client connected: $address")
            // Loopback: Tell your client to connect to the other server, if it isn't already so

            // Create input and output streams for communication
            input = socket.getInputStream()
            reader = BufferedReader(InputStreamReader(input!!))
            output = socket.getOutputStream()
            writer = PrintWriter(output!!, true)

            // The entire listening loop
            while (serverRunning.value) {
//                println("[TCPS] Server listening loop...")

                val message = reader?.readLine()
                writer?.println("ACK $message")
                onMessageRecieved(message.toString())
                latestMessage.value = message.toString()

//                println("[TCPS] Server listening loop finished.")
            }

            // Close the connection
            socket.close()
            serverSocket?.close()
        } catch (e: IOException) {
            System.err.println("[TCPS] Server error: ${e.printStackTrace()}")
            stop()
        }
    }

    fun launch() {
        if (serverRunning.value) return
        println("[TCPS] Starting TCP Server...")
        serverJob = socketScope.launch(Dispatchers.IO) {
            println("[TCPS] Launching TCP Coroutine...")
            try {
                main(arrayOf())
            } catch (_: IllegalStateException) {
                TODO("The client tried to connect to itself...")
            }
            println("[TCPS] TCP Coroutine finished after @JVM Static Main.")
        }
        println("[TCP] TCP Server started.")
    }

    fun stop() {
        // Unpair the client
        println("[TCPS] Stopping TCP Server...")
        TcpClient.sendToRemoteServer("MOSHI_STOP")
        TcpClient.stop()
        println("[TCPS] Client terminated.")

        // Close the server socket
        val stopJob = socketScope.launch(Dispatchers.IO) {
            println("[TCPS] Closing socket...")
            serverSocket?.close()
            serverSocket = null
            println("[TCPS] Socket closed.")
        }
        stopJob.invokeOnCompletion {
            println("[TCPS] Cancelling Socket Scope Job")
            serverJob?.cancel()
            serverJob = null
        }
        serverRunning.value = false
        println("[TCPS] Server stopped. (${serverRunning.value})")
    }

    /**
     * Handles messages recieved by the Server from REMOTE CLIENTS.
     *
     * Terminology:
     * - もし (Moshi) - "Hello" - Pairing and pairing reply handshakes
     * - ゲーム (Geemu) - "Game" - Game initialization handshakes
     * - やった (Yatta) - "Did it" - Move reporting between peers
     */
    private fun onMessageRecieved(message: String) {
        val args = message.split(' ')
        when (args[0]) {
            "MOSHI_GO" -> TcpClient.launch(args[1])
            "MOSHI_STOP" -> {
                TcpClient.stop()
                stop()
            }
            "GEEMU_START" -> {
                waitingJob = Handshaker.requestGameFromRemote(
                    mapName = args[1],
                    remotePlayer = PlayerProfile(args = args[2]),
                    playerStarts = args[3].toBoolean()
                )
                TcpClient.sendToRemoteServer(message = "GEEMU_WEMOVE ${Global.localPlayer.rosterAsPacket()}")
                Handshaker.finishRemoteGameRequest(Global.localPlayer)
//                waitingJob!!.invokeOnCompletion {
//                    Global.navController!!.navigate(AppScreens.GameScreen.name)
//                }
            }
            "GEEMU_WEMOVE" -> { Handshaker.finishLocalGameRequest(PlayerProfile(args = args[1])) }
            // ゲームドロー Remote player offered draw
            "GEEMU_DOROWU" -> { Global.gameLoop.value?.remotePlayerOfferedDraw() }
            "DOROWU_OKE" -> { Global.gameLoop.value?.declareDraw() }
            "DOROWU_NAI" -> { Global.gameLoop.value?.remotePlayerRefusedDraw() }
            // やったやった: Template (as AbilityTemplates.instance), Doer (as PhoenixTemplates.instance), target (as x+y)
            "YATTA_YATTA" -> { Global.gameLoop.value?.remotePlayerMove(args[1], args[2], args[3]) }
            "YATTA_FINISH" -> { Global.gameLoop.value?.remotePlayerFinishedRound() }
            "YATTA_BOSSHU" -> { Global.gameLoop.value?.remotePlayerForfeited() }
            // やったドロー: Remote player disconnected due to network or device failure
            "YATTA_DOROWU" -> { Global.gameLoop.value?.remotePlayerDisconnected() }
            // TODO "Remote player shat on your draw offer"
        }
    }
}
