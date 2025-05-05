package app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.effecters.aoe

import app.trailblazercombi.haventide.game2.data.tilemap.TileData
import app.trailblazercombi.haventide.game2.data.turntable.Team
import app.trailblazercombi.haventide.resources.ImmediateEffecterTemplates
import app.trailblazercombi.haventide.resources.Res
import app.trailblazercombi.haventide.resources.mechanism_aoeeffecter_life_description
import app.trailblazercombi.haventide.resources.mechanism_aoeeffecter_life_name
import app.trailblazercombi.haventide.resources.ultimate_malachai

class Life(
    parentTile: TileData, teamAffiliation: Team
) : AoEEffecter(parentTile, Res.drawable.ultimate_malachai, teamAffiliation = teamAffiliation) {
    override val icon = Res.drawable.ultimate_malachai
    override val name = Res.string.mechanism_aoeeffecter_life_name
    override val description = Res.string.mechanism_aoeeffecter_life_description

    override fun onEndOfRound() {
        ImmediateEffecterTemplates.HealingInvokerTemplates.LIFE.template.build(parentTile, teamAffiliation)
    }
}
