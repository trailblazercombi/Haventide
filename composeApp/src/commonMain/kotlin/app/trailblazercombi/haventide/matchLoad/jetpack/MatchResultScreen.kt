package app.trailblazercombi.haventide.matchLoad.jetpack

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import app.trailblazercombi.haventide.AppScreens
import app.trailblazercombi.haventide.resources.GameResult
import org.jetbrains.compose.resources.stringResource

@Composable
fun MatchResultScreen(gameResult: GameResult) {
    Text(stringResource(gameResult.string))

    // TODO Close the TcpClient and Server once the game is finished
    //  (!!! do not necesseraily close them here !!!)
    //  (...but I won't stop you, I'm just a note)
    //                                       \ (\_/)
    //                                        ( •_•)
    //                                        / > \
}
