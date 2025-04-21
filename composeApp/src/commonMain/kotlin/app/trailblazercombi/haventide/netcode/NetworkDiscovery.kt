package app.trailblazercombi.haventide.netcode

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

/**
 * @author ChatGPT - initial generation of semi-broken code
 * @author trailblazercombi - vetting, testing, adaptation to work with the project
 */
suspend fun CoroutineScope.sendBroadcast(
    message: String,
    port: Int = 8888
): Unit = withContext(Dispatchers.IO) {
    DatagramSocket().use { socket ->
        socket.broadcast = true
        val buffer = message.toByteArray()
        val address = InetAddress.getByName("255.255.255.255")
        val packet = DatagramPacket(buffer, buffer.size, address, port)
        socket.send(packet)
    }
}

/**
 * @author ChatGPT - initial generation of semi-broken code
 * @author trailblazercombi - vetting, testing, adaptation to work with the project
 */
suspend fun CoroutineScope.listenToBroadcast(
    port: Int = 8888,
    onPacketReceived: (address: InetAddress) -> Unit = {}
): Unit = withContext(Dispatchers.IO) {
    val buffer = ByteArray(1024)
    val socket = DatagramSocket(port)

    val packet = DatagramPacket(buffer, buffer.size)
    try {
        socket.receive(packet)
    } catch (ioe: IOException) {
        println(ioe.printStackTrace())
    }
    onPacketReceived(packet.address)
    socket.close()
}
