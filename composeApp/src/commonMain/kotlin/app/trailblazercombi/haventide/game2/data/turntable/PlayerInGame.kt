package app.trailblazercombi.haventide.game2.data.turntable

import app.trailblazercombi.haventide.Global
import app.trailblazercombi.haventide.game2.data.tilemap.TileData
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.Mechanism
import app.trailblazercombi.haventide.playerdata.PlayerProfile
import app.trailblazercombi.haventide.resources.AbilityTemplate
import app.trailblazercombi.haventide.resources.MechanismTemplate
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.PhoenixMechanism
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.effecters.aoe.AoEEffecter
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.mfei.ModificatorHandler
import app.trailblazercombi.haventide.game2.viewModel.AbilityPreview
import app.trailblazercombi.haventide.resources.ModificatorFireType

/**
 * Defines a Player that's playing within the game.
 *
 * Interfaces directly with [TileMapData], the chief behind the game's logic.
 */
open class PlayerInGame(val profile: PlayerProfile, protected val turnTable: TurnTable) {
    /**
     * Check if the PlayerInGame was already initialized.
     * @return `true` if [initialize] was called on `this` previously.
     */
    @Suppress("MemberVisibilityCanBePrivate")
    var isInitialized = false
        private set

    private var isActive = false

    open val team = Team()
    private val dice = DiceStack()

    /**
     * Initializes the player by unpacking its [Roster][MechanismTemplate.Phoenix] into [Team].
     */
    fun initialize(vararg tiles: TileData) {
        if (isInitialized) throw IllegalStateException("PlayerInGame already initialized!")
        var i = 0
        profile.activeRoster.forEach {
            it.build(tiles[i], team)
            i++
        }
        isInitialized = true
    }

    /**
     * Call this on every start of the round.
     *
     * Sets the player status to `active` and rolls new dice.
     */
    open fun startRound() {
        isActive = true
        dice.roll(8)
    }

    /**
     * Execute an ability as this [PlayerInGame].
     *
     * __NOTE__ This method does not do any checks, it merely executes the ability.
     * @param ability The [AbilityTemplate] of the ability to be executed.
     * @param doer The [Mechanism] executing this ability. Said [Mechanism] will be pestered to deliver.
     * @param target The unfortunate [TileData] targeted by this abiilty. All [Mechanism]s on [TileData]
     *               will be affected.
     * @param consume The list of [Dice][DiceStack] to be consumed by this operation.
     */
    open fun executeAbility(ability: AbilityTemplate, doer: Mechanism, target: TileData, consume: List<Die> = listOf()) {
        ability.execution.invoke(doer, target)
        dice.consume(consume)
    }

    /**
     * Execute an ability as this [PlayerInGame].
     *
     * __NOTE__ This method does not do any checks, it merely executes the ability.
     * @param abilityPreview The data for the ability execution packed in [AbilityPreview].
     */
    open fun executeAbility(abilityPreview: AbilityPreview) {
        executeAbility(
            ability = abilityPreview.template,
            doer = abilityPreview.doer,
            target = abilityPreview.target,
            consume = abilityPreview.consume
        )
    }

    /**
     * Declare the player done for the round.
     *
     * Discard all the player's [Dice][DiceStack] and set their status to `inactive`, until the next round starts.
     */
    fun finishRound() {
        isActive = false
        team.forEach {
            if (it is ModificatorHandler) it.updateModificators(ModificatorFireType.ON_ROUND_FINISHED)
            if (it is AoEEffecter) it.onEndOfRound()
        }
        dice.discardAllDice()
    }

    /**
     * @return `true` if the player status is `active`
     * AND if the player has any alive [Phoenix][PhoenixMechanism]es.
     */
    fun isValidForTurn(): Boolean {
        return isActive && team.hasPhoenixes()
    }

    /**
     * Called automatically upon every start of the turn.
     */
    open fun onTurnStart() {}

//    /**
//     * Called automatically upon every end of the turn.
//     */
//    open fun onTurnEnd() {
//        team.forEach {
//            if (it is ModificatorHandler) it.updateModificators(ModificatorFireType.ON_TURN_FINISHED)
//        }
//    }

    /**
     * Called automatically upon the game over, regardless of the result.
     */
    open fun onGameOver() {} // TODO Make sure this is actually called...


    // EXPOSURE FUNCTIONS

    /**
     * Exposes [Team.hasPhoenixes].
     */
    fun hasPhoenixes() = team.hasPhoenixes()

    /**
     * Exposes [Team.getMembers] and filters for [PhoenixMechanism].
     */
    fun compilePhoenixes() = team.getMembers().filterIsInstance<PhoenixMechanism>()

    /**
     * Exposes [DiceStack.getDice]
     */
    protected fun getDice() = dice.getDice()

    /**
     * Finds the [Mechanism] asked for.
     */
    fun findDoer(string: String): PhoenixMechanism? {
        team.forEach { if (string == (it as? PhoenixMechanism)?.template?.gameId) return it }
        return null
    }

    override fun toString() = "Player ${profile.name}"
}
