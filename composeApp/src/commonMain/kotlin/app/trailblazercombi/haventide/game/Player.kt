package app.trailblazercombi.haventide.game

import app.trailblazercombi.haventide.game.mechanisms.PhoenixInfo
import app.trailblazercombi.haventide.game.mechanisms.PhoenixMechanism
import app.trailblazercombi.haventide.game.mechanisms.Phoenixes

/**
 * Defines a Player that's playing within the game.
 *
 * Interfaces directly with [TileMapData], the chief behind the game's logic.
 */
open class PlayerInGame(val profile: PlayerProfile, allied: Boolean) {
    // TODO Find out what you'll need this to do
}

/**
 * A subtype of [PlayerInGame] denoting a player that's interfacing directly with the local client.
 */
class LocalPlayerInGame(profile: PlayerProfile): PlayerInGame(profile, true)

/**
 * A subtype of [PlayerInGame] denoting a player that's interfacing with the local cleint over a network.
 */
class RemotePlayerInGame(profile: PlayerProfile, allied: Boolean = false): PlayerInGame(profile, allied)

/**
 * A data blob containing player information.
 *
 * This denotes a player outside the game and is also a component
 * of creating [PlayerInGame].
 */
data class PlayerProfile(
    val name: String,
    val activeRoster: Set<PhoenixInfo>
    // TODO Add more info related to the profile itself
    //  Profile picture
    //  Medals
    //  Match history
    //  ELO...
) {
    /**
     * Returns a new [LocalPlayerInGame] with this [PlayerProfile].
     */
    fun toLocalPIG(): LocalPlayerInGame = LocalPlayerInGame(this)

    /**
     * Returns a new [RemotePlayerInGame] with this [PlayerProfile].
     * @param allied Whether the player is an ally or not.
     */
    fun toRemotePIG(allied: Boolean = false) = RemotePlayerInGame(this, allied)
}

// TODO Replace with an actual player profile.
//  This will do for testing purposes though.
enum class PlaceholderPlayers(
    val playerName: String,
    val member1: PhoenixInfo,
    val member2: PhoenixInfo,
    val member3: PhoenixInfo
) {
    PLAYER_ONE(
        "You",
        Phoenixes.FINNIAN.info,
        Phoenixes.YUMIO.info,
        Phoenixes.MALACHAI.info
    ),
    PLAYER_TWO(
        "Opponent",
        Phoenixes.AYUNA.info,
        Phoenixes.AYUMI.info,
        Phoenixes.SYLVIA.info
    );

    fun toProfile() = PlayerProfile(playerName, setOf(member1, member2, member3))
}
