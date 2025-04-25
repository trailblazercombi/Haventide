package app.trailblazercombi.haventide.game2.data.turntable

import app.trailblazercombi.haventide.Global
import app.trailblazercombi.haventide.game2.data.tilemap.TileData
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.Mechanism
import app.trailblazercombi.haventide.playerdata.PlayerProfile
import app.trailblazercombi.haventide.resources.AbilityTemplate

class RemotePlayerInGame(profile: PlayerProfile, turnTable: TurnTable) : PlayerInGame(profile, turnTable) {
    override fun executeAbility(ability: AbilityTemplate, doer: Mechanism, target: TileData, consume: List<Die>) {
        super.executeAbility(ability, doer, target, consume)
        // [NETWORK] TODO Figure out what this needs to do...
    }

    override fun onTurnStart() {}

    override fun toString() = "Remote ${super.toString()}"
}
