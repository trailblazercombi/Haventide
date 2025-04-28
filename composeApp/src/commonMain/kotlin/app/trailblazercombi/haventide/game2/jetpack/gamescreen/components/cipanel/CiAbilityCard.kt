package app.trailblazercombi.haventide.game2.jetpack.gamescreen.components.cipanel

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextAlign
import app.trailblazercombi.haventide.game2.viewModel.GameLoopViewModel
import app.trailblazercombi.haventide.resources.AbilityTemplate
import app.trailblazercombi.haventide.resources.CiStyle
import app.trailblazercombi.haventide.resources.Palette
import app.trailblazercombi.haventide.resources.Res
import app.trailblazercombi.haventide.resources.ability_card_dice_costs_short
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * This will be a represenatation of a single selected Ability.
 * - In [CiAbilitySelectDropdown], clicking it will select the ability in [GameLoopViewModel].
 * - In [CiPanel][app.trailblazercombi.haventide.game2.jetpack.gamescreen.panels.CiPanel],
 * it is the accessor to the dropdown.
 *
 * In both cases, holding it will pop out a tooltip (dialog) containing detailed information.
 */
@Composable
fun CiAbilityCard(
    ability: AbilityTemplate,
    onClick: () -> Unit = {},
    compact: () -> Boolean = { false },
    contentColor: Color = LocalContentColor.current,
    modifier: Modifier = Modifier
) {
    val painter = painterResource(ability.friendlyIcon)
    val name = stringResource(ability.friendlyName)
    val cost = stringResource(Res.string.ability_card_dice_costs_short,
        ability.alignedCost, ability.scatteredCost)

    if (!compact()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = modifier
                .fillMaxWidth()
                .height(CiStyle.AbilityCardHeight)
                .padding(CiStyle.AbilityCardPadding)
                .clickable(onClick = onClick)
        ) {
            Image(
                painter = painter, contentDescription = null,
                colorFilter = ColorFilter.tint(contentColor),
                modifier = modifier
                    .fillMaxHeight()
                    .padding(CiStyle.AbilityCardShrinkImage)
            )
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center,
                modifier = modifier.fillMaxSize()
            ) {
                Text(
                    text = name,
                    fontSize = CiStyle.AbilityCardTitleSize,
                    lineHeight = CiStyle.AbilityCardTitleSize,
                    textAlign = TextAlign.Start,
                    color = contentColor
                )
                Text(
                    text = cost,
                    fontSize = CiStyle.AbilityCardDescriptionSize,
                    lineHeight = CiStyle.AbilityCardDescriptionSize,
                    textAlign = TextAlign.Start,
                    color = contentColor
                )
            }
        }
    } else { // compact = true
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .fillMaxWidth()
                .height(CiStyle.AbilityCardHeight)
                .padding(CiStyle.AbilityCardPadding)
                .clickable(onClick = onClick)
        ) {
            Image(
                painter = painter, contentDescription = null,
                colorFilter = ColorFilter.tint(contentColor),
                modifier = modifier
                    .fillMaxHeight()
                    .padding(CiStyle.AbilityCardShrinkImage)
            )
        }
    }
}
