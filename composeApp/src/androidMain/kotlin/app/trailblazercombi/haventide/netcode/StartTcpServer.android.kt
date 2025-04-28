package app.trailblazercombi.haventide.netcode

import android.content.Intent
import android.os.Build
import app.trailblazercombi.haventide.ContextHolder
import app.trailblazercombi.haventide.TcpServerService

actual fun startTcpServer() {
    println("[ANDROID INTENT] Starting TcpServer...")
    val context = ContextHolder.applicationContext
    println("2 context")
    val intent = Intent(
        context,
        TcpServerService::class.java
    )
    println("3 intent")
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        context.startForegroundService(intent)
    } else {
        context.startService(intent)
    }
    println("4 launched")
}
