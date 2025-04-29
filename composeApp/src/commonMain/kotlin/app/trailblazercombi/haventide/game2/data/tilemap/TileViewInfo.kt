package app.trailblazercombi.haventide.game2.data.tilemap

import app.trailblazercombi.haventide.resources.MechanismTemplate
import org.jetbrains.compose.resources.DrawableResource

/**
 * The info package for left Ci Display.
 */
sealed class TileViewInfo {
    class Empty : TileViewInfo()

    class Ally(
        val template: MechanismTemplate.Phoenix,
        val currentHp: Int,
    ): TileViewInfo()

    class Enemy(
        val template: MechanismTemplate.Phoenix
    ): TileViewInfo()

    class Mechanism(
        val icon: DrawableResource,
        val name: String,
        val decayIn: Int
    )
}
