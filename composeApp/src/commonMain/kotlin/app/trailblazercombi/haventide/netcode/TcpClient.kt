package app.trailblazercombi.haventide.netcode

import app.trailblazercombi.haventide.Global
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
import java.net.Socket
import java.net.SocketException

/**
 * This is the primary spokesperson for this app's instance.
 * It co-operates with [Handshaker] to send messages to remote [TcpServer],
 * and accepts replies from said server that say `ACK`.
 */
object TcpClient {

    private var input: InputStream? = null
    private var reader: BufferedReader? = null

    private var output: OutputStream? = null
    private var writer: PrintWriter? = null

    private var socket: Socket? = null
    private val socketScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var clientJob: Job? = null

    /**
     * Check if the [TcpClient] is turned on.
     */
    var isOpen = false
        private set

    /**
     * Check if the [TcpClient] is paired to a peer ([TcpServer])
     */
    val paired = MutableStateFlow(false)

    /**
     * The latest message recieved from remote[TcpServer].
     */
    val latestMessage = MutableStateFlow<String>("IDLE")

    @JvmStatic
    private fun main(args: Array<String>) {
        try {
            isOpen = true

            // Connect to the server at localhost (use the server's IP in a real network setup)
            socket = Socket(args.first(), 12345)
            println("Connected to the server!")

            // Create input and output streams for communication
            output = socket?.getOutputStream() ?: return
            writer = PrintWriter(output!!, true)

            input = socket?.getInputStream() ?: return
            reader = BufferedReader(InputStreamReader(input!!))

            // Send a message to the server
            writer?.println("MOSHI_GO ${NetPairing.localInet()}")

            // Receive a response from the server
            val serverMessage = reader?.readLine()
            latestMessage.value = serverMessage.toString()
            println("[TCP] Server responded with $serverMessage")
            paired.value = true

        } catch (e: IOException) {
            println("[TCP] Stopping client due to ${e.printStackTrace()}")
            stop()
        }
    }

    /**
     * @param ipAddress The IPv4 address of the REMOTE SERVER.
     */
    fun launch(ipAddress: String) {
        if (isOpen) return
        println("[TCP] Starting TCP Client")
        clientJob = socketScope.launch(Dispatchers.IO) {
            main(arrayOf(ipAddress))
        }
    }

    fun sendToRemoteServer(message: String) {
        socketScope.launch(Dispatchers.IO) {
            println("Sending $message to remote server...")
            try {
                writer?.println(message) ?: println("[TCP] Writer does not exist")
                val response = reader?.readLine() ?: println("[TCP] Reader does not exist")
                onMessageRecieved(response.toString())
                latestMessage.value = response.toString()
            } catch (se: SocketException) {
                System.err.println("[TCP] Stopping client due to ${se.printStackTrace()}")
                stop()
            }
        }
    }

    fun stop() {
        println("[TCP] Stopping TCP Client...")

        // If there is a game ongoing, forfeit it
        val loop = Global.gameLoop.value
        if (loop?.gameIsOver == false) { loop.localPlayerDisconnected() }

        // Close the socket
        val stopJob = socketScope.launch(Dispatchers.IO) {
            socket?.close()
            paired.value = false
            socket = null
            isOpen = false
            println("[TCP] Socket closed")
        }

        // Cancel the coroutine
        stopJob.invokeOnCompletion {
            println("[TCP] Cancelling Socket Scope Job")
            clientJob?.cancel()
            clientJob = null
        }
        println("[TCP] TCP Client stopped.")
    }

    /**
     * Handles messages recieved by the client from REMOTE SERVERS.
     */
    private fun onMessageRecieved(message: String) {
        println("Server responded: $message")
        val args = message.split(' ')
//        assert(args[0] == "ACK")
//        when (args[1]) {
//            else -> println("[TCP-OMR] Message received: $message")
//        }
    }
}
