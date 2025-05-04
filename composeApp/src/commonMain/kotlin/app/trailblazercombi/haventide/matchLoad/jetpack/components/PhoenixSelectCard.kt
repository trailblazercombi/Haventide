package app.trailblazercombi.haventide.matchLoad.jetpack.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import app.trailblazercombi.haventide.game2.jetpack.gamescreen.components.cipanel.CiAbilityCard
import app.trailblazercombi.haventide.matchLoad.jetpack.MainMenuScreen
import app.trailblazercombi.haventide.matchLoad.jetpack.activeRoster
import app.trailblazercombi.haventide.matchLoad.jetpack.toggle
import app.trailblazercombi.haventide.resources.CiStyle
import app.trailblazercombi.haventide.resources.GameScreenDialogBoxStyle
import app.trailblazercombi.haventide.resources.MainMenuStyle
import app.trailblazercombi.haventide.resources.MechanismTemplate
import app.trailblazercombi.haventide.resources.Palette
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun PhoenixSelectCard(template: MechanismTemplate.Phoenix, modifier: Modifier = Modifier) {
    val activeRoster by activeRoster.collectAsState()
    var selected by remember { mutableStateOf(false) }
    var enabled by remember { mutableStateOf(true) }

    LaunchedEffect(activeRoster) {
        selected = template in activeRoster
        enabled = activeRoster.size < 4 && template !in activeRoster
    }

    val fillColor = if (selected) Palette.Glass60 else if (enabled) Palette.Abyss60 else Palette.Abyss60.compositeOver(Palette.Glass60)
    val borderColor = if (selected) Palette.FillLightPrimary else if (enabled) Palette.FillLightPrimary else Palette.FullGrey
    val contentColor = if (selected) Palette.FullBlack else if (enabled) Palette.FullWhite else Palette.Abyss60.compositeOver(Palette.FullGrey)

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.padding(MainMenuStyle.PhoenixCardSeparation / 2)
    ) {
        Surface (
            shape = RoundedCornerShape(MainMenuStyle.PhoenixCardOuterRounding),
            border = BorderStroke(MainMenuStyle.PhoenixCardOutlineThickness, borderColor),
            color = fillColor,
            contentColor = contentColor,
            modifier = modifier.fillMaxSize().clickable {
                toggle(template)
            }
        ) {
            Column {
                Row (verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(template.profilePhoto),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = modifier
                            .height(MainMenuStyle.PhoenixCardPhotoSize)
                            .aspectRatio(1f)
                            .padding(MainMenuStyle.PhoenixCardInnerPadding)
                            .clip(RoundedCornerShape(MainMenuStyle.PhoenixCardInnerRounding))
                    )
                    Column {
                        Text(
                            text = stringResource(template.fullName),
                            fontSize = MainMenuStyle.PhoenixCardTextSizeName,
                            modifier = modifier.padding(
                                start = MainMenuStyle.PhoenixCardInnerPadding,
                                top = MainMenuStyle.PhoenixCardInnerPadding,
                                end = MainMenuStyle.PhoenixCardInnerPadding,
                                bottom = 0.dp
                            )
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = modifier.padding(
                                start = MainMenuStyle.PhoenixCardInnerPadding,
                                top = 0.dp,
                                end = MainMenuStyle.PhoenixCardInnerPadding,
                                bottom = MainMenuStyle.PhoenixCardInnerPadding
                            )
                        ) {
                            Image(
                                painter = painterResource(template.phoenixType.icon),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                colorFilter = ColorFilter.tint(contentColor),
                                modifier = modifier
                                    .height(CiStyle.CiTitleIconSize)
                                    .width(CiStyle.CiTitleIconSize)
                                    .padding(CiStyle.InnerPadding / 2)
                            )
                            Text(
                                text = stringResource(template.phoenixType.title),
                                fontSize = MainMenuStyle.PhoenixCardTextSizeDescription,
                            )
                        }
                    }
                }
                listOf(template.abilityInnate, template.abilityUltimate).forEach {
                    CiAbilityCard(it, compact = 1)
                }
            }
        }
    }
}
