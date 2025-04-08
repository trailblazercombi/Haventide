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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import app.trailblazercombi.haventide.game.*
import app.trailblazercombi.haventide.resources.*
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import app.trailblazercombi.haventide.resources.PhoenixBallStyle.OutlineThickness as ballOutline
import app.trailblazercombi.haventide.resources.PhoenixBallStyle.Padding as ballPadding
import app.trailblazercombi.haventide.resources.TileStyle.Padding as tilePadding
import app.trailblazercombi.haventide.resources.TileStyle.TileSize as tileSize

class Phoenix(
    parentTile: TileData,
    val info: PhoenixInfo,
    override val modificators: MutableList<Modificator> = mutableListOf(),
) : Mechanism(parentTile, MechanismType.PHOENIX), ModificatorHandler, HitPointsHandler {
    override val maxHitPoints = info.maxHitPoints
    override var currentHitPoints = maxHitPoints

// METHOD OVERRIDES

    override fun onZeroHitPoints() {
        parentTile.removeMechanism(this)
    }

    override fun vetoTraversal(mechanism: Mechanism): Boolean {
        // TODO Team affiliation, allow movements for team members
        return super.vetoTraversal(mechanism)
    }
}

/**
 * This is a data blob that contians all data
 * a Phoenix needs to function.
 * @see Phoenix
 */
data class PhoenixInfo(
    val fullName: StringResource,
    val shortName: StringResource,
    val accentColor: Color,
    val profilePhoto: DrawableResource,
    val maxHitPoints: Int = 120,
    // TODO Abilities
    //  Lore pages
    //  Et cetera
)

/**
 * This is a definition of all the Phoenixes' data blobs.
 * Use to create new instances of [Phoenix].
 */
enum class Phoenixes(val info: PhoenixInfo) {
    AYUNA(PhoenixInfo(
        fullName = Res.string.phoenix_ayuna_long_name,
        shortName = Res.string.phoenix_ayuna_short_name,
        accentColor = Color(0xFFD91410),
        profilePhoto = Res.drawable.Ayuna
    )),
    AYUMI(PhoenixInfo(
        fullName = Res.string.phoenix_ayumi_long_name,
        shortName = Res.string.phoenix_ayumi_short_name,
        accentColor = Color(0xFF098432),
        profilePhoto = Res.drawable.Ayumi
    )),
    SYLVIA(PhoenixInfo(
        fullName = Res.string.phoenix_sylvia_long_name,
        shortName = Res.string.phoenix_sylvia_short_name,
        accentColor = Color(0xFF67406D),
        profilePhoto = Res.drawable.Sylvia
    )),
    MALACHAI(PhoenixInfo(
        fullName = Res.string.phoenix_malachai_long_name,
        shortName = Res.string.phoenix_malachai_short_name,
        accentColor = Color(0xFF3B3B93),
        profilePhoto = Res.drawable.Malachai
    )),
    FINNIAN(PhoenixInfo(
        fullName = Res.string.phoenix_finnian_long_name,
        shortName = Res.string.phoenix_finnian_short_name,
        accentColor = Color(0xFF25B97F),
        profilePhoto = Res.drawable.Finnian
    )),
    YUMIO(PhoenixInfo(
        fullName = Res.string.phoenix_yumio_long_name,
        shortName = Res.string.phoenix_yumio_short_name,
        accentColor = Color(0xFF2169AD),
        profilePhoto = Res.drawable.Yumio
    )),
    TORRENT(PhoenixInfo(
        fullName = Res.string.phoenix_torrent_long_name,
        shortName = Res.string.phoenix_torrent_short_name,
        accentColor = Color(0xFF465376),
        profilePhoto = Res.drawable.Torrent
    )),

    ; fun toPhoenix(parent: TileData): Phoenix {
        return Phoenix(parent, this.info)
    }
}

@Composable
fun ComposablePhoenixMechanismBall(phoenix: Phoenix, modifier: Modifier = Modifier) {
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
        // TODO Team affiliation icon, actual health points, actual energy (ally only)
    }
}
