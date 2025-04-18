package app.trailblazercombi.haventide.game2.data.turntable

import app.trailblazercombi.haventide.game.abilities.AbilityTemplate
import app.trailblazercombi.haventide.game.abilities.Die
import app.trailblazercombi.haventide.game.arena.PlayerInGame
import app.trailblazercombi.haventide.game2.data.tilemap.TileData
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.Mechanism
import app.trailblazercombi.haventide.playerdata.PlayerProfile

class RemotePlayerInGame(profile: PlayerProfile, turnTable: TurnTable) : PlayerInGame(profile, turnTable) {
    var turnsLeft = 10

    override fun executeAbility(ability: AbilityTemplate, doer: Mechanism, target: TileData, consume: List<Die>) {
        // [NETWORK] TODO Figure out what this needs to do...
    }

    override fun onTurnStart() {
        // [NETWORK] FIXME Remove this later, placeholder only
        turnTable.endRoundAndNextPlayerTurn()
    }
}
