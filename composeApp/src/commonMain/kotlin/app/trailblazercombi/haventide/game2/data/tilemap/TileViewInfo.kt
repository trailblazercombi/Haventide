package app.trailblazercombi.haventide.game2.data.tilemap

import app.trailblazercombi.haventide.resources.MechanismTemplate
import org.jetbrains.compose.resources.DrawableResource

/**
 * The info package for left Ci Display.
 */
sealed class TileViewInfo {
    class Empty : TileViewInfo()

    class Ally(
        template: MechanismTemplate.Phoenix,
        val currentHp: Int,
        val maxHp: Int,
    ): TileViewInfoThatHoldsMechanismTemplateForPhoenix(template)

    class Enemy(
        template: MechanismTemplate.Phoenix
    ): TileViewInfoThatHoldsMechanismTemplateForPhoenix(template)

    class Mechanism(
        val icon: DrawableResource,
        val name: String,
        val decayIn: Int
    )

    abstract class TileViewInfoThatHoldsMechanismTemplateForPhoenix(
        val template: MechanismTemplate.Phoenix,
    ): TileViewInfo()
}
