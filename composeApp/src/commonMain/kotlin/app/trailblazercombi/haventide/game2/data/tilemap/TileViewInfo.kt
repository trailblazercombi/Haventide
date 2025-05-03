package app.trailblazercombi.haventide.game2.data.tilemap

import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.PhoenixMechanism
import app.trailblazercombi.haventide.resources.MechanismTemplate
import kotlinx.coroutines.flow.MutableStateFlow
import org.jetbrains.compose.resources.DrawableResource

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
        mechanism: Mechanism
    ): TileViewInfo()

    abstract class TileViewInfoThatHoldsMechanismTemplateForPhoenix(
        val template: MechanismTemplate.Phoenix,
    ): TileViewInfo()
}
