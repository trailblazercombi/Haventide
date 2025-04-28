package app.trailblazercombi.haventide.netcode

import android.content.Intent
import android.os.Build
import app.trailblazercombi.haventide.ContextHolder
import app.trailblazercombi.haventide.TcpClientService

actual fun startTcpClient(ipAddress: String) {
    println("[ANDROID INTENT] Starting TcpClient...")
    val context = ContextHolder.applicationContext
    val intent = Intent(
        context,
        TcpClientService::class.java
    ).apply {
        putExtra("inet", ipAddress)
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        context.startForegroundService(intent)
    } else {
        context.startService(intent)
    }
}
