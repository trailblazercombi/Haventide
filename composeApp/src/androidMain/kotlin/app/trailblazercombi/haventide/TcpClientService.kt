package app.trailblazercombi.haventide

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import app.trailblazercombi.haventide.netcode.TcpClient
import app.trailblazercombi.haventide.netcode.TcpServer

/**
 * This class handles Android's intents and prevents the device from
 * killing the game if the device goes to sleep or the user switches to
 * other tasks.
 *
 * This is a separate [Intent] from the main app that runs in Foreground Mode.
 *
 * __NOTE:__ Android-only. Windows doesn't really care, and as such,
 * the Windows / Desktop client has no trouble being minimized or suspended.
 *
 * @author Written in collaboration with ChatGPT. All comments by @trailblazercombi
 */
class TcpClientService : Service() {
    override fun onCreate() {
        doNotification()
    }

    override fun onDestroy() {
        super.onDestroy()
        TcpClient.stop()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        println("[TCS] Muwahahahahaw!!! Online!!!")
        val hostIp = intent?.getStringExtra("inet")
            ?: throw NullPointerException("[TCPCS] Something is null!!! " +
                    "intent: $intent, hostIp: ${intent?.getStringExtra("inet")}")
        TcpClient.launch(hostIp)

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
        val channelId = "game_client_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Haventide: Client is running",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Haventide")
            .setContentText("Client is running")
            .setSmallIcon(R.drawable.ic_launcher_foreground) // your icon
            .build()

        startForeground(1, notification)
    }
}
