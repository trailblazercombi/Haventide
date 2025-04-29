package app.trailblazercombi.haventide.game2.jetpack.gamescreen.components.cipanel

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import app.trailblazercombi.haventide.game2.data.tilemap.TileViewInfo
import app.trailblazercombi.haventide.resources.CiStyle
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun CiPhoenixName(
    tile: TileViewInfo.TileViewInfoThatHoldsMechanismTemplateForPhoenix,
    contentColor: Color = LocalContentColor.current,
    modifier: Modifier = Modifier,
) {
    Row (verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = stringResource(tile.template.shortName),
            fontSize = CiStyle.TitleSize,
            lineHeight = CiStyle.DescriptionSize,
            textAlign = TextAlign.Start,
        )
        Image(
            painter = painterResource(tile.template.phoenixType.icon),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            colorFilter = ColorFilter.tint(contentColor),
            modifier = modifier
                .height(CiStyle.CiTitleIconSize)
                .width(CiStyle.CiTitleIconSize)
                .padding(CiStyle.InnerPadding / 2)
        )
    }
}
