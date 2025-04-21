package app.trailblazercombi.haventide.netcode

import java.net.InetAddress

sealed class NetworkStatus {
    data object Disconnected : NetworkStatus()
    data class Connected(val peerInet4: InetAddress, val port: Int = 8888) : NetworkStatus()
}
