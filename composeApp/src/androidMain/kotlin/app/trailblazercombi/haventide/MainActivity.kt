package app.trailblazercombi.haventide

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.NotificationManagerCompat
import app.trailblazercombi.haventide.game.arena.GameLoop
import app.trailblazercombi.haventide.game.arena.PlaceholderPlayers

class MainActivity : ComponentActivity() {
    val gameLoop = GameLoop(
        PlaceholderPlayers.PLAYER_ONE.toProfile(),
        PlaceholderPlayers.PLAYER_TWO.toProfile()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App(gameLoop)
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

    override fun onDestroy() {
        super.onDestroy()
        // Hell do I know what kind of cleanup will I need to perform
    }
}

//@Preview
//@Composable
//fun AppAndroidPreview() {
//
//}
