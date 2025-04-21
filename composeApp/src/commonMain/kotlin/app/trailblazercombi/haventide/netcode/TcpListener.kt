package app.trailblazercombi.haventide.netcode

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.*
import java.net.Socket
import java.net.ServerSocket

/**
 * @author ChatGPT - initial generation of semi-broken code
 * @author trailblazercombi - vetting, testing, adaptation to work with the project
 */
class TcpListener(
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default),
    private val port: Int = 9999
) {
    private val serverSocket = ServerSocket(port)

    fun start(onMessageReceived: (Socket, String) -> Unit) {
        scope.launch(Dispatchers.IO) {
            println("Server listening on $port...")
            while (true) {
                val client = serverSocket.accept()
                println("New client connected: ${client.inetAddress.hostAddress}")
                handleClient(client, onMessageReceived)
            }
        }
    }

    private fun handleClient(socket: Socket, onMessageReceived: (Socket, String) -> Unit) {
        scope.launch(Dispatchers.IO) {
            val reader = BufferedReader(InputStreamReader(socket.getInputStream()))
            val writer = BufferedWriter(OutputStreamWriter(socket.getOutputStream()))

            try {
                while (true) {
                    val line = reader.readLine() ?: break
                    onMessageReceived(socket, line)
                }
            } catch (e: IOException) {
                println("Client disconnected: ${socket.inetAddress.hostAddress}")
            } finally {
                socket.close()
            }
        }
    }

    fun stop() {
        serverSocket.close()
    }
}
