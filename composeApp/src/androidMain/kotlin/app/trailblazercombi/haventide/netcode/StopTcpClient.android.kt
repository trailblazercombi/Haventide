package app.trailblazercombi.haventide.netcode

import android.content.Intent
import app.trailblazercombi.haventide.ContextHolder
import app.trailblazercombi.haventide.TcpClientService

actual fun stopTcpClient() {
    val context = ContextHolder.applicationContext
    val intent = Intent(
        context,
        TcpClientService::class.java
    )
    context.stopService(intent)
}
