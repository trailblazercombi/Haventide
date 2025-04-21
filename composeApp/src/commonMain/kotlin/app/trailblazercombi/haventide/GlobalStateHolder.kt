package app.trailblazercombi.haventide

import app.trailblazercombi.haventide.matchLoad.data.MatchState
import app.trailblazercombi.haventide.netcode.NetworkStatus
import app.trailblazercombi.haventide.netcode.TcpListener
import app.trailblazercombi.haventide.netcode.TcpSpeaker
import kotlinx.coroutines.flow.MutableStateFlow

object GlobalStateHolder {
    val currentMatch: MutableStateFlow<MatchState> = MutableStateFlow(MatchState.None)

    val networkStatus: MutableStateFlow<NetworkStatus> = MutableStateFlow(NetworkStatus.Disconnected)
//    val tcpListener: MutableStateFlow<TcpListener?> = MutableStateFlow(null)
//    val tcpSpeaker: MutableStateFlow<TcpSpeaker?> = MutableStateFlow(null)

    val lastMessage: MutableStateFlow<String> = MutableStateFlow("(No message yet...)")
}
