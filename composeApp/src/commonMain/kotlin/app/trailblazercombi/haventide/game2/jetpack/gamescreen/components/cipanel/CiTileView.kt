package app.trailblazercombi.haventide.game2.jetpack.gamescreen.components.cipanel

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import app.trailblazercombi.haventide.game2.data.tilemap.TileViewInfo
import app.trailblazercombi.haventide.resources.CiStyle
import app.trailblazercombi.haventide.resources.Res
import app.trailblazercombi.haventide.resources.enemy
import app.trailblazercombi.haventide.resources.tile_info_empty
import app.trailblazercombi.haventide.resources.tile_info_enemy
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun CiTileView(
    contentColor: Color = LocalContentColor.current,
    tile: TileViewInfo,
    modifier: Modifier = Modifier,
    compact: () -> Boolean = { false }
) {
    Box(
        contentAlignment = if (tile is TileViewInfo.Empty || compact()) Alignment.Center else Alignment.CenterStart,
        modifier = modifier.fillMaxWidth().height(CiStyle.AbilityCardHeight),
    ) {
        if (tile is TileViewInfo.Empty) {
            Text (stringResource(Res.string.tile_info_empty))
        } else if (tile is TileViewInfo.Enemy) {
            Row (verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(tile.template.profilePhoto),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = modifier
                        .fillMaxHeight()
                        .aspectRatio(1f)
                        .padding(CiStyle.PhoenixCardShrinkImage)
                        .clip(RoundedCornerShape(CiStyle.PhoenixCornerRounding))
                )
                if (!compact()) {
                    Column(verticalArrangement = Arrangement.Center) {
                        Text(
                            text = stringResource(tile.template.shortName),
                            fontSize = CiStyle.TitleSize,
                            lineHeight = CiStyle.TitleSize,
                            textAlign = TextAlign.Start,
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(Res.drawable.enemy),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                colorFilter = ColorFilter.tint(contentColor),
                                modifier = modifier
                                    .height(CiStyle.CiTextIconSize)
                                    .width(CiStyle.CiTextIconSize)
                                    .padding(CiStyle.InnerPadding)
                            )
                            Text(
                                text = stringResource(Res.string.tile_info_enemy),
                                fontSize = CiStyle.DescriptionSize,
                                lineHeight = CiStyle.DescriptionSize,
                                textAlign = TextAlign.Start,
                            )
                        }
                    }
                }
            }
        } else if (tile is TileViewInfo.Ally) {
            Row (verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(tile.template.profilePhoto),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = modifier
                        .fillMaxHeight()
                        .aspectRatio(1f)
                        .padding(CiStyle.PhoenixCardShrinkImage)
                        .clip(RoundedCornerShape(CiStyle.PhoenixCornerRounding))
                )
                if (!compact()) {
                    Column(verticalArrangement = Arrangement.Center) {
                        Text(
                            text = stringResource(tile.template.shortName),
                            fontSize = CiStyle.TitleSize,
                            lineHeight = CiStyle.TitleSize,
                            textAlign = TextAlign.Start,
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(Res.drawable.enemy),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                colorFilter = ColorFilter.tint(contentColor),
                                modifier = modifier
                                    .height(CiStyle.CiTextIconSize)
                                    .width(CiStyle.CiTextIconSize)
                                    .padding(CiStyle.InnerPadding)
                            )
                            Text(
                                text = stringResource(Res.string.tile_info_enemy),
                                fontSize = CiStyle.DescriptionSize,
                                lineHeight = CiStyle.DescriptionSize,
                                textAlign = TextAlign.Start,
                            )
                        }
                    }
                }
            }
        } else {
            throw IllegalStateException("Display mode not yet implemented for $tile")
        }
    }
}
