package app.trailblazercombi.haventide.netcode

import android.content.Intent
import app.trailblazercombi.haventide.ContextHolder
import app.trailblazercombi.haventide.TcpServerService

actual fun stopTcpServer() {
    val context = ContextHolder.applicationContext
    val intent = Intent(
        context,
        TcpServerService::class.java
    )
    context.stopService(intent)
}
