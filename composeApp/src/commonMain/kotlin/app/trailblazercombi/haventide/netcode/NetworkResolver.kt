package app.trailblazercombi.haventide.netcode

import app.trailblazercombi.haventide.GlobalState
import app.trailblazercombi.haventide.playerdata.PlayerProfile
import app.trailblazercombi.haventide.resources.PlaceholderPlayers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import kotlin.random.Random

///**
// * @author trailblazercombi - adapted from above
// */
//suspend fun CoroutineScope.sendBroadcast(
//    message: String,
//    port: Int = 8888
//): Unit = withContext(Dispatchers.IO) {
//    sendUdpMessage(message, InetAddress.getByName("255.255.255.255"), port)
//}
//
///**
// * @author ChatGPT - initial generation of semi-broken code
// * @author trailblazercombi - vetting, testing, adaptation to work with the project
// */
//suspend fun CoroutineScope.listenToBroadcast(
//    port: Int = 8888,
//    onPacketReceived: (packet: DatagramPacket) -> Unit = {}
//): Unit = withContext(Dispatchers.IO) {
//    val buffer = ByteArray(1024)
//    val socket = DatagramSocket(port)
//
//    val packet = DatagramPacket(buffer, buffer.size)
//    try {
//        socket.receive(packet)
//    } catch (ioe: IOException) {
//        println("Caught $ioe on NetworkSocketManager::listenToBroadcast")
//    }
//    onPacketReceived(packet)
//    socket.close()
//}
//
///**
// * @author ChatGPT - initial generation of semi-broken code
// * @author trailblazercombi - vetting, testing, adaptation to work with the project
// */
//suspend fun CoroutineScope.sendUdpMessage(
//    message: String,
//    destination: InetAddress,
//    port: Int = 8888
//): Unit = withContext(Dispatchers.IO) {
//    DatagramSocket().use { socket ->
//        socket.broadcast = true
//        val buffer = message.toByteArray()
//        val packet = DatagramPacket(buffer, buffer.size, destination, port)
//        socket.send(packet)
//    }
//}

/**
 * This object handles raw communication over the local network.
 *
 * ###### USAGE INSTRUCTIONS:
 * 0. This app only accepts having a single socket open at the same time:
 *    The one leading to the Remote Player.
 * 1. Use [openSocket] to open the socket on the specified port.
 *    Use port number `8888` for discovery broadcasting.
 * 2. Use [connect] to connect the socket to the specified recipient.
 *    For simplicity, only IPv4 addresses, please.
 * 3. Use [disconnect] to disconnect the socket, and [closeSocket] to fully close it.
 *    Breaking any of this usually results in an [IllegalAccessException].
 */
object NetworkResolver {
    private var activeSocket: DatagramSocket? = null
    private var listening = false

    private var connectInet: InetAddress? = null
    private var connectPort: Int? = null

    private var socketOpen = false
    private var socketConnected = false

    private var yinPairing = false
    private var yangPairing = false

//    private val parsingPatterns: Map<String, (List<String>) -> Unit> = mapOf()

    private const val BUFFER_SIZE = 1024
    private const val BROADCAST_PORT = 8888
    private const val MIN_PAIRING_PORT = 8667
    private const val MAX_PAIRING_PORT = 8887
    private const val LISTEN_FOR_TIMEOUT = 400
    private const val YANG_WAIT_TO_CONNECT = 1000

    private val BROADCAST_ADDRESS = InetAddress.getByName("255.255.255.255")

    /**
     * This device is currently pairing to another app
     */
    val pairingNow = MutableStateFlow(false) // <-- Observe in MatchBeginScreen

    /**
     * This device is currently paired to another device
     */
    val paired = MutableStateFlow(false) // <-- Observe in MatchBeginScreen and GameLoop

    private var playMap = "files/maps/parkingLot"
    private var localPlayer = PlaceholderPlayers.PLAYER_ONE.toProfile()
    private var remotePlayer = PlaceholderPlayers.PLAYER_ONE.toProfile()
    private var iStart = Random.nextBoolean()

    /**
     * Open the socket on a specified port.
     * @param port The specified port to create the socket on.
     */
    private suspend fun CoroutineScope.openSocket(port: Int) = withContext(Dispatchers.IO) {
        if (socketOpen) return@withContext
        activeSocket = DatagramSocket(port)
        socketOpen = true
        println("[NR] Socket open on port $port")
    }

    /**
     * Close the active socket.
     * If the socket is already closed, nothing happens.
     */
    private suspend fun CoroutineScope.closeSocket() = withContext(Dispatchers.IO) {
        if (!socketOpen) return@withContext
        activeSocket?.close()
        activeSocket = null
        socketOpen = false
        socketConnected = false
        println("[NR] Socket closed.")
    }

    /**
     * Connect to a specified peer on the other side.
     * @param address The IPv4 (Inet) address of the recipient, as [InetAddress].
     * @param port The recipient's port on the specified [InetAddress].
     */
    private suspend fun CoroutineScope.connect(address: InetAddress, port: Int) = withContext(Dispatchers.IO) {
        if (socketConnected) return@withContext
        activeSocket?.connect(address, port) ?: return@withContext
        socketConnected = true
        println("[NR] Socket connected to $address:$port")
    }

    /**
     * Disconnect from the peer on the other side.
     * @throws IllegalAccessException if the socket is closed.
     */
    private suspend fun CoroutineScope.disconnect() = withContext(Dispatchers.IO) {
        if (!socketConnected) return@withContext
        activeSocket?.disconnect() ?: return@withContext
        socketConnected = false
        println("[NR] Socket disconnected.")
    }

    /**
     * Pair to the first listening device found on the network.
     */
    suspend fun CoroutineScope.pairYin() = withContext(Dispatchers.IO) {
        if (socketOpen) {
            println("[NR] Cannot pair: Socket already open.")
            return@withContext
        }

        println("[NR -> YIN] Pairing started...")
        pairingNow.value = true
        connectPort = Random.nextInt(MIN_PAIRING_PORT, MAX_PAIRING_PORT + 1)
        openSocket(connectPort ?: return@withContext)
        socketOpen = true

        yinPairing = true
        do {
            sendBroadcastMessage("HAVENTIDE_PAIR $connectPort END")
            listenFor(
                message = "HAVENTIDE_PAIR_ACK",
                onPacketRecieved = { connectInet = it.address },
                onTimeout = { sendBroadcastMessage("HAVENTIDE_PAIR $connectPort END") }
            )
            println("[NR] Timed out!")
        } while (socketOpen && yinPairing)

        connect(connectInet ?: return@withContext, connectPort ?: return@withContext)
        socketConnected = true

        println("[NR -> YIN] Paired with $connectInet:$connectPort")
        pairingNow.value = false
        paired.value = true
    }

    /**
     * Pair to the first broadcasting device found on this network.
     */
    suspend fun CoroutineScope.pairYang() = withContext(Dispatchers.IO) {
        if (socketOpen) {
            println("[NR] Cannot pair: Socket already open.")
            return@withContext
        }

        println("[NR -> YANG] Pairing started...")
        yangPairing = true
        pairingNow.value = true

        if (yangPairing) {
            openSocket(BROADCAST_PORT)
            listenFor(message = "HAVENTIDE_PAIR", onPacketRecieved = {
                val args = String(it.data).args()
                connectInet = it.address
                connectPort = args.first().toInt()
            })
            closeSocket()
            delay(YANG_WAIT_TO_CONNECT.toLong())
        } else {
            return@withContext
        }

        if (yangPairing) {
            openSocket(connectPort ?: return@withContext)
            sendMessage("HAVENTIDE_PAIR_ACK END")
            connect(connectInet ?: return@withContext, connectPort ?: return@withContext)

            socketOpen = true
            socketConnected = true
            yangPairing = false
        } else {
            return@withContext
        }

        println("[NR -> YANG] Paired with $connectInet:$connectPort")
        pairingNow.value = false
        paired.value = true
    }

    /**
     * Unpair the devices. This only needs to be called on one of the devices.
     */
    suspend fun CoroutineScope.unpair() = withContext(Dispatchers.IO) {
        closeSocket()
        socketOpen = false
        yangPairing = false

        pairingNow.value = false
        paired.value = false
        println("Disconnected")
    }

    /**
     * Send a message to the connected peer on the other side.
     * If the socket is not connected, sends a broadcast message instead.
     */
    suspend fun CoroutineScope.sendMessage(message: String) = withContext(Dispatchers.IO) {
        if (!socketConnected) return@withContext
        try {
            activeSocket?.send(DatagramPacket(message.toByteArray(), message.length))
        } catch (ioe: IOException) {
            println("[NR] Network unreachable or disconnected")
            unpair()
        }
    }

    /**
     * Send a message to broadcast.
     */
    suspend fun CoroutineScope.sendBroadcastMessage(message: String) = withContext(Dispatchers.IO) {
        // Completely independent, uses its own socket for this...
        DatagramSocket().use { socket ->
            socket.broadcast = true
            val buffer = message.toByteArray()
            val packet = DatagramPacket(buffer, buffer.size, BROADCAST_ADDRESS, BROADCAST_PORT)
            socket.send(packet)
        }
    }

    /**
     * Listen ONCE for a packet with the specified message.
     * Once the packet is recieved, stop listening.
     *
     * This method will listen to any incoming traffic if the socket is not connected.
     * Useful for broadcast discovery.
     */
    private suspend fun CoroutineScope.listenFor(
        message: String,
        onPacketRecieved: (DatagramPacket) -> Unit,
        onTimeout: suspend () -> Unit = {}
    ) = withContext(Dispatchers.IO) {
        if (!socketOpen) return@withContext
        val packet = DatagramPacket(ByteArray(BUFFER_SIZE), BUFFER_SIZE)
        var okay = false
        while (!okay && socketOpen) {
            try {
                withTimeoutOrNull(LISTEN_FOR_TIMEOUT.toLong()) {
                    activeSocket?.receive(packet)
                } ?: onTimeout()
            } catch (ioe: IOException) {
                println("[NR -> LISTEN FOR] ${ioe.message}, I quit listening...")
                yinPairing = false
                return@withContext
            }
            if (String(packet.data).header() == message) {
                onPacketRecieved(packet)
                yinPairing = false
                okay = true
            }
        }
    }

    /**
     * Start a continous listening to the connected device.
     * If the socket is already listening, does nothing.
     *
     * __NOTE__: The socket must be connected?!
     */
    suspend fun CoroutineScope.startListening() = withContext(Dispatchers.IO) {
        if (listening || !socketOpen || !socketConnected) return@withContext

        val packet = DatagramPacket(ByteArray(BUFFER_SIZE), BUFFER_SIZE)
        listening = true
        do {
            try {
                activeSocket?.receive(packet)
                parse(String(packet.data))
            } catch (ioe: IOException) {
                println("[NR -> CONTINOUS LISTENING] ${ioe.message}, I quit listening...")
                listening = false
                return@withContext
            }
        } while (listening && socketConnected)
    }

    /**
     * Stop the continous listening to the connected device.
     * If the socket is not listening, does nothing.
     */
    suspend fun CoroutineScope.stopListening() = withContext(Dispatchers.IO) {
        listening = false
    }

    suspend fun CoroutineScope.initiatePlayRequest(localPlayer: PlayerProfile) = withContext(Dispatchers.IO) {
        println("[PR] Play request to $activeSocket initiated")
        if (!paired.value) {
            println("[PR] Play request to $activeSocket refused")
            return@withContext
        }

        playMap = "files/maps/parkingLot"
        iStart = Random.nextBoolean()

        sendMessage("HAVENTIDE_PLAY $playMap ${localPlayer.rosterAsPacket()} $iStart")
    }

    private suspend fun CoroutineScope.respondToPlayRequest(localPlayer: PlayerProfile) = withContext(Dispatchers.IO) {

    }

//    /**
//     * From now on, begin matching messages from remote device with this header.
//     */
//    fun addParsingPattern(header: String, onHeader: suspend (List<String>) -> Unit) {
//        if (parsingPatterns[header] != null) throw IllegalArgumentException("Header is already being parsed")
//    }
//
//    /**
//     * From now on, stop matching messsages from remote device with this header.
//     */
//    fun removeParsingPattern(header: String) {
//        parsingPatterns - header
//    }

    private suspend fun CoroutineScope.parse(packetData: String) {
        val header = packetData.header()
        val args = packetData.args()

        when (header) {
            "HAVENTIDE_PLAY" -> {
                playMap = args[0]
                remotePlayer = PlayerProfile(args[1])
                iStart = !(args[2].toBoolean())
                respondToPlayRequest(localPlayer)
                sendMessage("HAVENTIDE_ACK ${localPlayer.rosterAsPacket()}")
                GlobalState.createGameLoop(playMap, localPlayer, remotePlayer, iStart)
            }
            "HAVENTIDE_PLAY_ACK" -> {
                remotePlayer = PlayerProfile(args[0])
                GlobalState.createGameLoop(playMap, localPlayer, remotePlayer, iStart)
            }
            else -> println("$header: $args")
        }
    }
}
