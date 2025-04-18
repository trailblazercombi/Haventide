package app.trailblazercombi.haventide.game2.data.turntable

import app.trailblazercombi.haventide.game.abilities.AbilityTemplate
import app.trailblazercombi.haventide.game.abilities.Die
import app.trailblazercombi.haventide.game.arena.PlayerInGame

class LocalPlayerInGame(profile: PlayerProfile, turnTable: TurnTable) : PlayerInGame(profile, turnTable) {
    override fun executeAbility(ability: AbilityTemplate, doer: Mechanism, target: TileData, consume: List<Die>) {
        super.executeAbility(ability, doer, target, consume)
        // [NETWORK] TODO Make sure this propagates to the opponent's client
    }
}
