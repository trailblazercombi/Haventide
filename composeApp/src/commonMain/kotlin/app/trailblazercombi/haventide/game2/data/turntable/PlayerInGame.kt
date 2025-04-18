package app.trailblazercombi.haventide.game2.data.turntable

import app.trailblazercombi.haventide.game.abilities.AbilityTemplate
import app.trailblazercombi.haventide.game.abilities.DiceStack
import app.trailblazercombi.haventide.game.abilities.Die

/**
 * Defines a Player that's playing within the game.
 *
 * Interfaces directly with [TileMapData], the chief behind the game's logic.
 */
open class PlayerInGame(val profile: PlayerProfile, val turnTable: TurnTable) {
    private var isActive = false

    val team = Team()
    val dice = DiceStack()

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

    open fun executeAbility(ability: AbilityTemplate, doer: Mechanism, target: TileData, consume: List<Die>) {
        ability.execution.invoke(doer, target)
        dice.consume(consume)
        turnTable.nextPlayerTurn()
    }

    fun finishRound() {
        isActive = false
        dice.discardAllDice()
    }

    fun isValidForTurn(): Boolean {
        return isActive && team.stillHasAlivePhoenixes()
    }

    open fun onTurnStart() {}

    fun onGameOver() {}
}
