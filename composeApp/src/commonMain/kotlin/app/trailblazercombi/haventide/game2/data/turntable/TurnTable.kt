package app.trailblazercombi.haventide.game2.data.turntable

import app.trailblazercombi.haventide.game2.data.GameLoop

/**
 * This class holds the current state of the game's turns.
 *
 * It manages whose turn it is, as well as who goes next.
 * It also manages end of players' turns using [nextPlayerTurn] and [endRoundAndNextPlayerTurn].
 */
class TurnTable(private val gameLoop: GameLoop) {
    /**
     * Check if the TurnTable was already initialized.
     * @return `true` if [initialize] was called on `this` previously.
     */
    @Suppress("MemberVisibilityCanBePrivate")
    var isInitialized = false
        private set

    private val thisTurnArray: MutableList<PlayerInGame> = mutableListOf()
    private val nextTurnArray: MutableList<PlayerInGame> = mutableListOf()
    private var currentPlayerIndex = 0

    /**
     * Initialize the turn table by filling in the player profiles.
     *
     * __NOTE__: This action is irreversible!!!
     * Procure a new [TurnTable] if you need an uninitialized one.
     * @param players Variable amount of [PlayerInGame] playing the current match.
     * @throws IllegalStateException If called on a previously initialized [TurnTable].
     */
    fun initialize(vararg players: PlayerInGame) {
        if (isInitialized) throw IllegalStateException("TurnTable is already initialized")
        thisTurnArray.addAll(players)
        thisTurnArray.forEach { it.startRound() }
        isInitialized = true
    }

    /**
     * @return The reference to the player whose turn it currently is.
     */
    fun currentPlayer(): PlayerInGame = thisTurnArray[currentPlayerIndex]

    /**
     * @return An immutable list, listing all [PlayerInGame].
     * __NOTE__: The [PlayerInGame] themselves are mutable!!!
     */
    fun allPlayers(): List<PlayerInGame> = thisTurnArray.toList()

    /**
     * End the current [PlayerInGame]'s turn and start the next [PlayerInGame]'s turn.
     *
     * This should ideally be called only by the [GameLoop] and via pestering the ViewModel for clicks.
     * @return The [PlayerInGame] whose turn is next.
     */
    fun nextPlayerTurn(): PlayerInGame {
        var result: PlayerInGame
        do {
            currentPlayerIndex = (currentPlayerIndex + 1) % thisTurnArray.size
            result = thisTurnArray[currentPlayerIndex]
            if (!result.hasPhoenixes()) {
                thisTurnArray.remove(result)
                result.onGameOver()
                if (this.thisTurnArray.isEmpty()) gameLoop.declareDraw()
                else if (this.thisTurnArray.size == 1) gameLoop.declareWinner(this.thisTurnArray[0])
            }
        } while (!result.isValidForTurn())
        result.onTurnStart()
        return result
    }

    /**
     * End the current [PlayerInGame]'s turn and start the next [PlayerInGame]'s turn.
     * Also, end the current [PlayerInGame]'s entire round -> add them to the next round roster,
     * and discard their dice.
     *
     * This should ideally be called only by the [GameLoop] and via pestering the ViewModel for clicks.
     * @return The [PlayerInGame] whose turn is next.
     */
    fun endRoundAndNextPlayerTurn(): PlayerInGame {
        val player = thisTurnArray[currentPlayerIndex]
        player.finishRound()
        thisTurnArray.remove(player)
        nextTurnArray.add(player)
        if (thisTurnArray.isEmpty()) return nextRound().let { currentPlayer() }
        return nextPlayerTurn()
    }

    private fun nextRound() {
        thisTurnArray.clear()
        thisTurnArray.addAll(nextTurnArray)
        nextTurnArray.clear()
        currentPlayerIndex = 0
        thisTurnArray.forEach { it.startRound() }
    }

    override fun toString(): String {
        return "TurnTable for $gameLoop: " +
            if (isInitialized) "$thisTurnArray -> $nextTurnArray, current player ${currentPlayer()}"
            else "Not initialized"
    }
}
