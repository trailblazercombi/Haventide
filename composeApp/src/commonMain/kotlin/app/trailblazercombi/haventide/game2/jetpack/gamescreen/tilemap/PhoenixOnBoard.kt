package app.trailblazercombi.haventide.game2.jetpack.gamescreen.tilemap

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import app.trailblazercombi.haventide.Global
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.PhoenixMechanism
import app.trailblazercombi.haventide.game2.viewModel.GameLoopViewModel
import app.trailblazercombi.haventide.resources.Palette
import app.trailblazercombi.haventide.resources.PhoenixBallStyle
import app.trailblazercombi.haventide.resources.PhoenixBallStyle.AffiliationIconInnerPadding
import app.trailblazercombi.haventide.resources.PhoenixBallStyle.AffiliationIconOuterOffsetFromBottomRight
import app.trailblazercombi.haventide.resources.PhoenixBallStyle.AffiliationIconSize
import app.trailblazercombi.haventide.resources.PhoenixBallStyle.OutlineThickness
import app.trailblazercombi.haventide.resources.TileStyle
import app.trailblazercombi.haventide.resources.TileStyle.TileSize
import org.jetbrains.compose.resources.painterResource

@Composable
fun PhoenixOnBoard(phoenix: PhoenixMechanism, modifier: Modifier = Modifier) {
    val painter: Painter = painterResource(phoenix.template.profilePhoto)
    val teamIcon: Painter = painterResource(phoenix.teamIcon)

    val health by phoenix.health.collectAsState()

    Box (modifier = modifier
        .size(TileSize - TileStyle.Padding)
    ) {
        // Primary icon (photo)
        Image(
            painter = painter,
            contentDescription = null,
            modifier = modifier
                .align(Alignment.Center)
                .clip(CircleShape)
                .border(OutlineThickness, Palette.FullWhite, CircleShape)
                .size(TileSize - TileStyle.Padding - PhoenixBallStyle.Padding)
        )
        if (phoenix.teamAffiliation == Global.gameLoop.value?.localPlayer?.team) {// Allies: Health bar (circular)
            Box {
                val displayValue: Float = health.toFloat() / phoenix.maxHitPoints
                CircularProgressIndicator(
                    progress = displayValue,
                    color = Palette.FillGreen,
                    strokeWidth = PhoenixBallStyle.AllyHpIndicatorWidth,
                    modifier = modifier
                        .fillMaxSize()
                        .padding(PhoenixBallStyle.AllyHpIndicatorPadding)
                )
            }
        } else {
            // Enemies: Affiliation icon
            Box(
                modifier = modifier
                    .align(Alignment.BottomEnd)
                    .size(AffiliationIconSize + (AffiliationIconOuterOffsetFromBottomRight * 2))
                    .padding(AffiliationIconOuterOffsetFromBottomRight)
                    .background(Palette.FullWhite, CircleShape)
            ) {
                Image(
                    painter = teamIcon,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(Palette.FullBlack),
                    modifier = modifier
                        .size(AffiliationIconSize + (AffiliationIconOuterOffsetFromBottomRight * 2))
                        .padding(AffiliationIconInnerPadding)
                )
            }
        }
    }
}
