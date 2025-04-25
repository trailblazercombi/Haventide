package app.trailblazercombi.haventide

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import app.trailblazercombi.haventide.netcode.TcpServer
import kotlinx.coroutines.cancel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            App()
        }
    }

    override fun onPause() {
        super.onPause()
        // Send a notification that the game is still running
        // (if in game)
    }

    override fun onResume() {
        super.onResume()
        // Dismiss the notification
        // (if there is one)
    }

    override fun onStop() {
        super.onStop()

        TcpServer.stop()
        // TODO This is where all clean-up goes!!!
    }
}
