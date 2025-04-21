package app.trailblazercombi.haventide.netcode

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.launch
import java.io.*
import java.net.InetAddress
import java.net.Socket

/**
 * @author ChatGPT - initial generation of semi-broken code
 * @author trailblazercombi - vetting, testing, adaptation to work with the project
 */
class TcpSpeaker(
    private val serverAddress: InetAddress,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default),
    private val port: Int = 9999
) {
    private lateinit var socket: Socket
    private lateinit var reader: BufferedReader
    private lateinit var writer: BufferedWriter

    suspend fun connect(onMessageReceived: (String) -> Unit) = withContext(Dispatchers.IO) {
        socket = Socket(serverAddress, port)
        reader = BufferedReader(InputStreamReader(socket.getInputStream()))
        writer = BufferedWriter(OutputStreamWriter(socket.getOutputStream()))

        println("Connected to server at ${serverAddress.hostAddress}")

        // Start reading loop
        scope.launch {
            try {
                while (true) {
                    val message = reader.readLine() ?: break
                    onMessageReceived(message)
                }
            } catch (_: IOException) {
                println("Disconnected from server")
            } finally {
                socket.close()
            }
        }
    }

    suspend fun send(message: String) = withContext(Dispatchers.IO) {
        writer.write("$message\n")
        writer.flush()
    }

    fun disconnect() {
        socket.close()
    }
}
