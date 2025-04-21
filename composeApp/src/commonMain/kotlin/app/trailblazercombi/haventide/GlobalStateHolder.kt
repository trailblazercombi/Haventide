package app.trailblazercombi.haventide

import app.trailblazercombi.haventide.matchLoad.data.MatchState
import kotlinx.coroutines.flow.MutableStateFlow

object GlobalStateHolder {
    val currentMatch: MutableStateFlow<MatchState> = MutableStateFlow(MatchState.None)
}
