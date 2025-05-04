package app.trailblazercombi.haventide.game2.jetpack.gamescreen.components.cipanel

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.trailblazercombi.haventide.game2.data.tilemap.TileViewInfo
import app.trailblazercombi.haventide.game2.data.tilemap.modificators.Modificator
import app.trailblazercombi.haventide.resources.CiStyle
import app.trailblazercombi.haventide.resources.ModificatorType
import app.trailblazercombi.haventide.resources.ModificatorViewStyle
import app.trailblazercombi.haventide.resources.Palette
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun CiModificatorView(
    modificator: Modificator,
    modifier: Modifier = Modifier
) {
    val accentColor = modificator.modificatorType.let {
        if (it == ModificatorType.BUFF) Palette.FillGreen
        else if (it == ModificatorType.DEBUFF) Palette.FillRed
        else Palette.FullGrey
    }

    Row(modifier, verticalAlignment = Alignment.Top) {
        Image(
            painter = painterResource(modificator.icon),
            contentDescription = null,
            colorFilter = ColorFilter.tint(accentColor),
            modifier = modifier
                .padding(ModificatorViewStyle.VerticalSpacing)
                .height(ModificatorViewStyle.IconSize)
                .width(ModificatorViewStyle.IconSize)
        )
        Column {
            Text(
                text = stringResource(modificator.name),
                fontSize = CiStyle.TitleSize,
                fontWeight = FontWeight.Bold,
                lineHeight = CiStyle.TitleSize,
                textAlign = TextAlign.Start,
                color = accentColor
            )
            Text(
                text = stringResource(modificator.description),
                fontSize = CiStyle.DescriptionSize,
                lineHeight = CiStyle.DescriptionLineHeight,
                textAlign = TextAlign.Start,
                color = Palette.FullWhite
            )
        }
    }
}
