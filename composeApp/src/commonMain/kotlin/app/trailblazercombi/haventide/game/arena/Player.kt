package app.trailblazercombi.haventide.game.arena

import app.trailblazercombi.haventide.game.abilities.DiceStack
import app.trailblazercombi.haventide.game.mechanisms.MechanismTemplate
import app.trailblazercombi.haventide.game.mechanisms.Phoenixes

/**
 * Defines a Player that's playing within the game.
 *
 * Interfaces directly with [TileMapData], the chief behind the game's logic.
 */
open class PlayerInGame(private val profile: PlayerProfile) {
    private var isActive = false

    val team = Team()

    private val dice = DiceStack()

    fun addRoster(vararg tiles: TileData) {
        val tileSet = tiles.toList()
        var i = 0
        profile.activeRoster.forEach {
            println("Buhehe! $it for $team")
            it.build(tileSet[i], team)
            i++
        }
    }

    fun startRound() {
        isActive = true
        dice.roll(8)
    }

    fun finishRound() {
        isActive = false
        dice.discardAll()
    }

    fun isValidForTurn(): Boolean {
        return isActive && team.stillHasAlivePhoenixes()
    }
}

/**
 * A data blob containing player information.
 *
 * This denotes a player outside the game and is also a component
 * of creating [PlayerInGame].
 */
data class PlayerProfile(
    val name: String,
    val activeRoster: Set<MechanismTemplate.Phoenix>
    // [LATER...] TODO Add more info related to the profile itself
    //  Profile picture
    //  Medals
    //  Match history
    //  ELO...
) {
    /**
     * Returns a new [PlayerInGame] with this [PlayerProfile].
     */
    fun toPlayerInGame(): PlayerInGame = PlayerInGame(this)
}

// [LATER...] TODO Replace with an actual player profile.
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
