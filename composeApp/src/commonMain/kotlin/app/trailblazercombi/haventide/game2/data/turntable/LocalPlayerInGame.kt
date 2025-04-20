package app.trailblazercombi.haventide.game2.data.turntable

import app.trailblazercombi.haventide.game2.data.tilemap.TileData
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.Mechanism
import app.trailblazercombi.haventide.playerdata.PlayerProfile
import app.trailblazercombi.haventide.resources.AbilityTemplate
import app.trailblazercombi.haventide.resources.Res
import app.trailblazercombi.haventide.resources.ally

class LocalPlayerInGame(profile: PlayerProfile, turnTable: TurnTable) : PlayerInGame(profile, turnTable) {
    override val team = Team(icon = Res.drawable.ally)

    override fun executeAbility(ability: AbilityTemplate, doer: Mechanism, target: TileData, consume: List<Die>) {
        super.executeAbility(ability, doer, target, consume)
        pushLocalPlayerDiceStackToViewModel()
        // [NETWORK] TODO Make sure this propagates to the opponent's client
    }

    override fun onTurnStart() {
        super.onTurnStart()
        pushLocalPlayerDiceStackToViewModel()
    }

    /**
     * Pushes Dice Updates to the TurnTable, then to the ViewModel.
     */
    private fun pushLocalPlayerDiceStackToViewModel() {
        turnTable.pushLocalPlayerDiceStackToViewModel(getDice())
    }
}
