package app.trailblazercombi.haventide.game.mechanisms

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import app.trailblazercombi.haventide.game.arena.Team
import app.trailblazercombi.haventide.game.arena.TileData
import app.trailblazercombi.haventide.game.modificators.Modificator
import app.trailblazercombi.haventide.resources.*
import org.jetbrains.compose.resources.painterResource
import app.trailblazercombi.haventide.resources.PhoenixBallStyle.OutlineThickness as ballOutline
import app.trailblazercombi.haventide.resources.PhoenixBallStyle.Padding as ballPadding
import app.trailblazercombi.haventide.resources.TileStyle.Padding as tilePadding
import app.trailblazercombi.haventide.resources.TileStyle.TileSize as tileSize

class PhoenixMechanism(
    parentTile: TileData,
    val info: MechanismTemplate.Phoenix,
    override val modificators: MutableList<Modificator> = mutableListOf(),
    teamAffiliation: Team
) : Mechanism(parentTile, teamAffiliation), ModificatorHandler, HitPointsHandler {
    override val maxHitPoints = info.maxHitPoints
    override var currentHitPoints = maxHitPoints

// METHOD OVERRIDES

    override fun onZeroHitPoints() {
        parentTile.removeMechanism(this)
    }

    override fun vetoTraversal(mechanism: Mechanism): Boolean {
        if (mechanism !is PhoenixMechanism) return true
        if (mechanism.teamAffiliation != teamAffiliation) return true
        return false
    }
}

@Composable
fun ComposablePhoenixMechanismBall(phoenix: PhoenixMechanism, modifier: Modifier = Modifier) {
    val painter: Painter = painterResource(phoenix.info.profilePhoto)

    Box (modifier = modifier
            .size(tileSize - tilePadding)
    ) {
        Image(
            painter = painter,
            contentDescription = null,
            modifier = modifier
                .align(Alignment.Center)
                .clip(CircleShape)
                .border(ballOutline, Palette.FullWhite, CircleShape)
                .size(tileSize - tilePadding - ballPadding)
        )
        // Affiliation icon
        Box (
            modifier = modifier
                .align(Alignment.BottomEnd)
                .size(teamIconSize + (teamIconPadding * 2))
                .padding(teamIconPadding)
                .background(Palette.FullWhite, CircleShape)
        ) {
            Image(
                painter = teamIcon,
                contentDescription = null,
                colorFilter = ColorFilter.tint(Palette.FullBlack),
                modifier = modifier
                    .size(teamIconSize + (teamIconPadding * 2))
                    .padding(teamIconInnerPadding)
            )
        }
        // TODO actual health points, actual energy (ally only)
    }
}
