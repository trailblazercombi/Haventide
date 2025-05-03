package app.trailblazercombi.haventide.game2.jetpack.gamescreen.tilemap

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.Mechanism
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.effecters.aoe.AoEEffecter
import app.trailblazercombi.haventide.resources.Palette
import app.trailblazercombi.haventide.resources.PhoenixBallStyle
import app.trailblazercombi.haventide.resources.TileStyle
import app.trailblazercombi.haventide.resources.TileStyle.TileSize
import org.jetbrains.compose.resources.painterResource

@Composable
fun AoEEffecterOnBoard(mechanism: Mechanism, modifier: Modifier = Modifier) {
    val painter: Painter = painterResource((mechanism as AoEEffecter).drawable)

    Box (modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Image(
            painter = painter,
            contentDescription = null,
            colorFilter = ColorFilter.tint(mechanism.teamAffiliation?.color ?: Palette.FullGrey),
            modifier = modifier.size(TileSize - TileStyle.Padding - PhoenixBallStyle.Padding)
        )
    }
}
