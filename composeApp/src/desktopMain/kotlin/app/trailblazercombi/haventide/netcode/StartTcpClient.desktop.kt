package app.trailblazercombi.haventide.netcode

actual fun startTcpClient(ipAddress: String) {
    TcpClient.launch(ipAddress)
}
