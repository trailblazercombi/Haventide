package app.trailblazercombi.haventide.game2.data.tilemap

import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.PhoenixMechanism
import app.trailblazercombi.haventide.resources.MechanismTemplate
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.Mechanism as classname_game2_Mechanism

/**
 * The info package for left Ci Display.
 */
sealed class TileViewInfo {
    class Empty : TileViewInfo()

    class Ally(
        val phoenix: PhoenixMechanism
    ): TileViewInfoThatHoldsMechanismTemplateForPhoenix(phoenix.template)
//        template: MechanismTemplate.Phoenix,
//        currentHp: Int,
//        val maxHp: Int,
//    ): TileViewInfoThatHoldsMechanismTemplateForPhoenix(template) {
//        val currentHp = MutableStateFlow(currentHp)
//    }

    class Enemy(
        template: MechanismTemplate.Phoenix
    ): TileViewInfoThatHoldsMechanismTemplateForPhoenix(template)

    class Mechanism(
        val icon: DrawableResource,
        val name: StringResource,
        val description: StringResource
    ): TileViewInfo()

    abstract class TileViewInfoThatHoldsMechanismTemplateForPhoenix(
        val template: MechanismTemplate.Phoenix,
    ): TileViewInfo()
}
