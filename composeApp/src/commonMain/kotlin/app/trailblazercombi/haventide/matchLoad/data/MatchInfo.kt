package app.trailblazercombi.haventide.matchLoad.data

import app.trailblazercombi.haventide.playerdata.PlayerProfile

data class MatchInfo(
    val matchMap: String,
    val localPlayer: PlayerProfile,
    val remotePlayer: PlayerProfile,
    val localPlayerStarts: Boolean
    // If false, remote player is player 1. If true, local player is player 1.
)
