package app.trailblazercombi.haventide.game.arena

import app.trailblazercombi.haventide.game.abilities.DiceStack
import app.trailblazercombi.haventide.game.mechanisms.Mechanism
import app.trailblazercombi.haventide.game.mechanisms.MechanismTemplate
import app.trailblazercombi.haventide.game.mechanisms.Phoenixes

/**
 * Defines a Player that's playing within the game.
 *
 * Interfaces directly with [TileMapData], the chief behind the game's logic.
 */
open class PlayerInGame(val profile: PlayerProfile, val turnTable: TurnTable) {
    private var isActive = false

    val team = Team()

    private val dice = DiceStack()

    fun addRoster(vararg tiles: TileData) {
        val tileSet = tiles.toList()
        var i = 0
        profile.activeRoster.forEach {

            it.build(tileSet[i], team)
            i++
        }
    }

    fun startRound() {
        isActive = true
        dice.roll(8)
    }

    open fun executeAbility(ability: (Mechanism, TileData) -> Unit, doer: Mechanism, target: TileData) {
        ability.invoke(doer, target)
        turnTable.nextPlayerTurn()
    }

    fun finishRound() {
        isActive = false
        dice.discardAll()
    }

    fun isValidForTurn(): Boolean {
        return isActive && team.stillHasAlivePhoenixes()
    }

    open fun onTurnStart() {}

    fun onGameOver() {}
}

class LocalPlayerInGame(profile: PlayerProfile, turnTable: TurnTable) : PlayerInGame(profile, turnTable) {
    override fun executeAbility(ability: (Mechanism, TileData) -> Unit, doer: Mechanism, target: TileData) {
        super.executeAbility(ability, doer, target)
        // [NETWORK] TODO Make sure this propagates
    }
}

class RemotePlayerInGame(profile: PlayerProfile, turnTable: TurnTable) : PlayerInGame(profile, turnTable) {
    override fun executeAbility(ability: (Mechanism, TileData) -> Unit, doer: Mechanism, target: TileData) {
        // [NETWORK] TODO Figure out what this needs to do...
    }

    override fun onTurnStart() {
        // [NETWORK] FIXME Remove this later, placeholder only
        turnTable.nextPlayerTurn()
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
    // [MENUS] TODO Add more info related to the profile itself
    //  Profile picture
    //  Medals
    //  Match history
    //  ELO...
) {
    /**
     * Returns a new [PlayerInGame] with this [PlayerProfile].
     */
    fun toPlayerInGame(turnTable: TurnTable, local: Boolean = false): PlayerInGame {
        if (local) return LocalPlayerInGame(this, turnTable)
        else return RemotePlayerInGame(this, turnTable)
    }
}

// [MENUS] TODO Replace with an actual player profile.
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
