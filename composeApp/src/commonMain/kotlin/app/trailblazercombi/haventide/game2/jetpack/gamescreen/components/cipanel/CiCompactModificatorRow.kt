package app.trailblazercombi.haventide.game2.jetpack.gamescreen.components.cipanel

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.PhoenixMechanism
import app.trailblazercombi.haventide.resources.CiStyle
import app.trailblazercombi.haventide.resources.ModificatorType
import app.trailblazercombi.haventide.resources.Palette
import org.jetbrains.compose.resources.painterResource

@Composable
fun CiCompactModificatorRow(phoenix: PhoenixMechanism, modifier: Modifier = Modifier) {
    val mods = phoenix.modificators

    Row {
        mods.forEach {
            Image(
                painter = painterResource(it.icon),
                contentDescription = null,
                colorFilter = ColorFilter.tint(
                    when (it.modificatorType) {
                        ModificatorType.BUFF -> Palette.FillGreen
                        ModificatorType.DEBUFF -> Palette.FillRed
                        else -> Palette.FullWhite
                    }
                ),
                modifier = modifier
                    .height(CiStyle.CiTitleIconSize)
                    .width(CiStyle.CiTitleIconSize)
                    .padding(CiStyle.InnerPadding / 2)
            )
        }
    }
}
