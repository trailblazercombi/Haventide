package app.trailblazercombi.haventide.game2.data.turntable

import app.trailblazercombi.haventide.Global
import app.trailblazercombi.haventide.game2.data.tilemap.TileData
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.Mechanism
import app.trailblazercombi.haventide.playerdata.PlayerProfile
import app.trailblazercombi.haventide.resources.AbilityTemplate
import app.trailblazercombi.haventide.resources.Palette
import app.trailblazercombi.haventide.resources.Res
import app.trailblazercombi.haventide.resources.ally

class LocalPlayerInGame(profile: PlayerProfile, turnTable: TurnTable) : PlayerInGame(profile, turnTable) {
    override val team = Team(icon = Res.drawable.ally, color = Palette.FillGreen)

    override fun executeAbility(ability: AbilityTemplate, doer: Mechanism, target: TileData, consume: List<Die>) {
        super.executeAbility(ability, doer, target, consume)
        Global.gameLoop.value!!.pushDiceChanges(getDice())
    }

    override fun onTurnStart() {
        super.onTurnStart()
        println("[LocalPIG] On Turn Start!")
        Global.gameLoop.value!!.viewModel.updateTileHighlights()
    }

    override fun startRound() {
        super.startRound()
        println("[LocalPIG] Start Round!")
        Global.gameLoop.value!!.pushDiceChanges(getDice())
        Global.gameLoop.value!!.viewModel.showStartRoundDialog()
    }

    //    /**
//     * Pushes Dice Updates to the TurnTable, then to the ViewModel.
//     */
//    private fun pushLocalPlayerDiceStackToViewModel() {
//        turnTable.pushLocalPlayerDiceStackToViewModel(getDice())
//    }

    override fun toString() = "Local ${super.toString()}"
}
