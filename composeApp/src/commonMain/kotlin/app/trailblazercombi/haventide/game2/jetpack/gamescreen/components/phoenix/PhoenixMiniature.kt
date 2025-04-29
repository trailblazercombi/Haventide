package app.trailblazercombi.haventide.game2.jetpack.gamescreen.components.phoenix

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import app.trailblazercombi.haventide.game2.data.tilemap.mechanisms.PhoenixMechanism
import app.trailblazercombi.haventide.resources.GameScreenTopBubbleStyle
import app.trailblazercombi.haventide.resources.Palette
import org.jetbrains.compose.resources.painterResource

@Composable
fun PhoenixMiniature(
    phoenix: PhoenixMechanism,
    width: Dp = GameScreenTopBubbleStyle.MiniatureWidth,
    height: Dp = GameScreenTopBubbleStyle.MiniatureHeight,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(width, height)
//            .also {
//                if (phoenix.ultimateReady()) it.border(
//                    BorderStroke(GameScreenTopBubbleStyle.MiniatureUltimateBorder, Palette.FillYellow),
//                )
//            }
        // .padding(GameScreenTopBubbleStyle.InnerOffset)
    ) {
        Image(
            painter = painterResource(phoenix.template.profilePhoto),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier
                .padding(GameScreenTopBubbleStyle.InnerOffset, 0.dp)
                .clip(RoundedCornerShape(GameScreenTopBubbleStyle.MiniatureCornerRounding))
        )
    }
}
