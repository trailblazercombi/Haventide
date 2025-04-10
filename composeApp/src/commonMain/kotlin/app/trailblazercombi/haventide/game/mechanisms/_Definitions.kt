package app.trailblazercombi.haventide.game.mechanisms

import androidx.compose.ui.graphics.Color
import app.trailblazercombi.haventide.game.modificators.ModificatorFactory
import app.trailblazercombi.haventide.game.arena.Team
import app.trailblazercombi.haventide.game.arena.TileData
import app.trailblazercombi.haventide.resources.*

/**
 * This is a definition of all the Phoenixes' data blobs.
 * Use to create new instances of [PhoenixMechanism].
 * @see [MechanismTemplate.Phoenix]
 */
enum class Phoenixes(val info: MechanismTemplate.Phoenix) {
    AYUNA(
        MechanismTemplate.Phoenix(
        fullName = Res.string.phoenix_ayuna_long_name,
        shortName = Res.string.phoenix_ayuna_short_name,
        accentColor = Color(0xFFD91410),
        profilePhoto = Res.drawable.Ayuna
    )),
    AYUMI(
        MechanismTemplate.Phoenix(
        fullName = Res.string.phoenix_ayumi_long_name,
        shortName = Res.string.phoenix_ayumi_short_name,
        accentColor = Color(0xFF098432),
        profilePhoto = Res.drawable.Ayumi
    )),
    SYLVIA(
        MechanismTemplate.Phoenix(
        fullName = Res.string.phoenix_sylvia_long_name,
        shortName = Res.string.phoenix_sylvia_short_name,
        accentColor = Color(0xFF67406D),
        profilePhoto = Res.drawable.Sylvia
    )),
    MALACHAI(
        MechanismTemplate.Phoenix(
        fullName = Res.string.phoenix_malachai_long_name,
        shortName = Res.string.phoenix_malachai_short_name,
        accentColor = Color(0xFF3B3B93),
        profilePhoto = Res.drawable.Malachai
    )),
    FINNIAN(
        MechanismTemplate.Phoenix(
        fullName = Res.string.phoenix_finnian_long_name,
        shortName = Res.string.phoenix_finnian_short_name,
        accentColor = Color(0xFF25B97F),
        profilePhoto = Res.drawable.Finnian
    )),
    YUMIO(
        MechanismTemplate.Phoenix(
        fullName = Res.string.phoenix_yumio_long_name,
        shortName = Res.string.phoenix_yumio_short_name,
        accentColor = Color(0xFF2169AD),
        profilePhoto = Res.drawable.Yumio
    )),
    TORRENT(
        MechanismTemplate.Phoenix(
        fullName = Res.string.phoenix_torrent_long_name,
        shortName = Res.string.phoenix_torrent_short_name,
        accentColor = Color(0xFF465376),
        profilePhoto = Res.drawable.Torrent
    )),

    ; fun toPhoenix(parent: TileData, team: Team): PhoenixMechanism {
        return PhoenixMechanism(parentTile = parent, info = this.info, teamAffiliation = team)
    }
}

object ImmidiateEffecters {
    enum class DamageInvokers(val damage: Int) {

    }

    enum class HealingInvokers(val healing: Int) {

    }

    enum class ModificatorInvokers(val invokable: ModificatorFactory) {

    }
}

object AoEEffecters {
    enum class DamageInvokers(val damage: Int) {

    }

    enum class HealingInvokers(val healing: Int) {

    }

    enum class ModificatorInvokers(val invokable: ModificatorFactory) {

    }
}
