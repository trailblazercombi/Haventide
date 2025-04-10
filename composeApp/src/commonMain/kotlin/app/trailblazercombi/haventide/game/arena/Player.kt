package app.trailblazercombi.haventide.game.arena

import app.trailblazercombi.haventide.game.mechanisms.MechanismTemplate
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
    val activeRoster: Set<MechanismTemplate.Phoenix>
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
    private val playerName: String,
    private val member1: MechanismTemplate.Phoenix,
    private val member2: MechanismTemplate.Phoenix,
    private val member3: MechanismTemplate.Phoenix
) {
    PLAYER_ONE(
        "You",
        Phoenixes.FINNIAN.template,
        Phoenixes.YUMIO.template,
        Phoenixes.MALACHAI.template
    ),
    PLAYER_TWO(
        "Opponent",
        Phoenixes.AYUNA.template,
        Phoenixes.AYUMI.template,
        Phoenixes.SYLVIA.template
    );

    fun toProfile() = PlayerProfile(playerName, setOf(member1, member2, member3))
}
