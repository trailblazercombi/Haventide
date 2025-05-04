package app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.effecters.aoe

import app.trailblazercombi.haventide.game2.data.tilemap.TileData
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.Mechanism
import app.trailblazercombi.haventide.game2.data.turntable.Team
import app.trailblazercombi.haventide.resources.ImmediateEffecterTemplates
import app.trailblazercombi.haventide.resources.Res
import app.trailblazercombi.haventide.resources.mechanism_aoeeffecter_spike_description
import app.trailblazercombi.haventide.resources.mechanism_aoeeffecter_spike_name
import app.trailblazercombi.haventide.resources.ultimate_sylvia

class SpikePrimary(parentTile: TileData, teamAffiliation: Team) :
    AoEEffecter(parentTile, Res.drawable.ultimate_sylvia,
    teamAffiliation = teamAffiliation) {

    override fun vetoTilemateAddition(tilemate: Mechanism): Boolean {
        return tilemate.teamAffiliation == teamAffiliation
    }

    override fun onStepOnto(mechanism: Mechanism) {
        ImmediateEffecterTemplates.MechanismSummonerTemplates
            .MANIFEST_THORNS.template.build(parentTile, null)
    }

    override val icon = Res.drawable.ultimate_sylvia
    override val name = Res.string.mechanism_aoeeffecter_spike_name
    override val description = Res.string.mechanism_aoeeffecter_spike_description
}
