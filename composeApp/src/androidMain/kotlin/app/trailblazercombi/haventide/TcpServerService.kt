package app.trailblazercombi.haventide

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.app.Service.START_STICKY
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import app.trailblazercombi.haventide.netcode.TcpClient
import app.trailblazercombi.haventide.netcode.TcpServer

class TcpServerService : Service() {
    override fun onCreate() {
        println("[TSS] Gehehehe!!! OnCreate fired!!!")
        doNotification()
    }

    override fun onDestroy() {
        println("[TSS] Womp womp!!! Destroyed!!!")
        super.onDestroy()
        TcpServer.stop()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        println("[TSS] Buehehehehehe!!! OnStartCommand fired!!!")
        TcpServer.launch()

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    /**
     * This is what handles the notification.
     * If the notification is gone, so is the entire Intent.
     */
    private fun doNotification() {
        val channelId = "game_server_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Haventide: Server is running",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Haventide")
            .setContentText("Server is running")
            .setSmallIcon(R.drawable.ic_launcher_foreground) // your icon
            .build()

        startForeground(1, notification)
    }
}
