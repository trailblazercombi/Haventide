package app.trailblazercombi.haventide

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.NotificationCompat
import app.trailblazercombi.haventide.ContextHolder.initContext
import app.trailblazercombi.haventide.netcode.TcpServer
import kotlinx.coroutines.cancel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initContext(this)

        setContent {
            App()
        }
    }
}
