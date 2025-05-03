package app.trailblazercombi.haventide.game2.jetpack.gamescreen.components.cipanel

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import app.trailblazercombi.haventide.game2.viewModel.GameLoopViewModel
import app.trailblazercombi.haventide.resources.AbilityTemplate
import app.trailblazercombi.haventide.resources.CiStyle
import app.trailblazercombi.haventide.resources.Res
import app.trailblazercombi.haventide.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * This will be a represenatation of a single selected Ability.
 * - In [CiAbilitySelectDropdown], clicking it will select the ability in [GameLoopViewModel].
 * - In [CiPanel][app.trailblazercombi.haventide.game2.jetpack.gamescreen.panels.CiPanel],
 * it is the accessor to the dropdown.
 *
 * In both cases, holding it will pop out a tooltip (dialog) containing detailed information.
 * @param compact The compactness level of this Composable.
 * Pass values between 0 - 3. Negative values are the same as compact = 0. Higher values are the same as compact = 3.
 */
@Composable
fun CiAbilityCard(
    ability: AbilityTemplate,
    compact: Int = 0,
    /*
     * COMPACT MODES:
     * 0 = display icon, name, description, statistics
     * 1 = display icon, name, description
     * 2 = display icon, name
     * 3 = display icon
     */
    contentColor: Color = LocalContentColor.current,
    modifier: Modifier = Modifier,
) {
    val painter = painterResource(ability.friendlyIcon)
    val name = stringResource(ability.friendlyName)
    val description = stringResource(ability.friendlyDescription).let {
        if (compact > 0) { it.split('\n').first() } else it
    }

    val costAlignedLabel = stringResource(Res.string.ability_card_cost_aligned)
    val costAligned = stringResource(Res.string.ability_card_cost_format, ability.alignedCost)

    val costScatteredLabel = stringResource(Res.string.ability_card_cost_scattered)
    val costScattered = stringResource(Res.string.ability_card_cost_format, ability.scatteredCost)

    val rangeLabel = stringResource(Res.string.ability_card_range)
    val range = stringResource(Res.string.ability_card_range_format, "%.2f".format(ability.range))

    val validTargetsLabel = stringResource(Res.string.ability_card_target_type)
    val validTargets = stringResource(ability.targetType.label)

    if (compact < 3) { // compact is 0, 1 or 2
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Start,
            modifier = modifier
                .fillMaxWidth()
//                .then(if (compact > 0) Modifier.height(CiStyle.AbilityCardHeight)
//                else Modifier.height(CiStyle.AbilityCardLargeHeight))
                .padding(CiStyle.AbilityCardPadding)
        ) {
            Image(
                painter = painter, contentDescription = null,
                colorFilter = ColorFilter.tint(contentColor),
                modifier = modifier
                    .padding(CiStyle.AbilityCardShrinkImage)
                    .height(CiStyle.AbilityCardImageSize)
            )
            Spacer(modifier.width(CiStyle.GenericInternalSeparation))
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center,
//                modifier = modifier.fillMaxSize()
            ) {
                Text(
                    text = name,
                    fontSize = CiStyle.TitleSize,
                    fontWeight = FontWeight.Bold,
                    lineHeight = CiStyle.TitleSize,
                    textAlign = TextAlign.Start,
                    color = contentColor
                )
                if (compact < 2) { // compact is 0 or 1
                    Spacer(modifier.height(CiStyle.GenericInternalSeparation))
                    Text(
                        text = description,
                        fontSize = CiStyle.DescriptionSize,
                        lineHeight = CiStyle.DescriptionLineHeightCompact,
                        textAlign = TextAlign.Start,
                        color = contentColor
                    )
                }
                if (compact < 1) { // compact = 0
                    Spacer(modifier.height(CiStyle.GenericInternalSeparation))
                    Row {
                        Text(
                            text = """
                                $rangeLabel
                                $validTargetsLabel
                                $costAlignedLabel
                                $costScatteredLabel
                            """.trimIndent(),
                            fontSize = CiStyle.DescriptionSize,
                            fontWeight = FontWeight.Bold,
                            lineHeight = CiStyle.DescriptionLineHeight,
                            textAlign = TextAlign.Start,
                            color = contentColor
                        )
                        Spacer(modifier.height(CiStyle.GenericInternalSeparation))
                        Text(
                            text = """
                                $range
                                $validTargets
                                $costAligned
                                $costScattered
                            """.trimIndent(),
                            fontSize = CiStyle.DescriptionSize,
                            lineHeight = CiStyle.DescriptionLineHeight,
                            textAlign = TextAlign.Start,
                            color = contentColor
                        )
                    }
                }
            }
        }
    } else { // compact = 3+
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .fillMaxWidth()
//                .height(CiStyle.AbilityCardHeight)
                .padding(CiStyle.AbilityCardPadding)
        ) {
            Image(
                painter = painter, contentDescription = null,
                colorFilter = ColorFilter.tint(contentColor),
                modifier = modifier
                    .padding(CiStyle.AbilityCardShrinkImage)
                    .height(CiStyle.AbilityCardImageSize)
            )
        }
    }
}
