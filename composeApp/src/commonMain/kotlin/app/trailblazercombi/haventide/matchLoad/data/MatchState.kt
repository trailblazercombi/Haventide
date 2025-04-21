package app.trailblazercombi.haventide.matchLoad.data

import app.trailblazercombi.haventide.game2.data.GameLoop

sealed class MatchState {
    data object None : MatchState()
    data object Loading : MatchState()
    data class Ready(val gameLoop: GameLoop) : MatchState()
}
