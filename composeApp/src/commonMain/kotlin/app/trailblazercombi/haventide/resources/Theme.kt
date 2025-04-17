package app.trailblazercombi.haventide.resources

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Defines the colors used within the app
 */
object Palette {
    // [LATER...] TODO Remove unused colours from the Palette.
    //  The Palette got reduced since I first planned it... huh

    // Transparent
    val Glass00 = Color(0x00000000)

    val Glass10 = Color(0x1AFFFFFF)
    val Glass20 = Color(0x33FFFFFF)
    val Glass30 = Color(0x4DFFFFFF)
    val Glass40 = Color(0x66FFFFFF)
    val Glass50 = Color(0x80FFFFFF)
    val Glass60 = Color(0x99FFFFFF)
    val Glass70 = Color(0xB3FFFFFF)
    val Glass80 = Color(0xCCFFFFFF)
    val Glass90 = Color(0xE6FFFFFF)

    val Abyss10 = Color(0x1A000000)
    val Abyss20 = Color(0x33000000)
    val Abyss30 = Color(0x4D000000)
    val Abyss40 = Color(0x66000000)
    val Abyss50 = Color(0x80000000)
    val Abyss60 = Color(0x99000000)
    val Abyss70 = Color(0xB3000000)
    val Abyss80 = Color(0xCC000000)
    val Abyss90 = Color(0xE6000000)

    // Dark Primary
    val FillDarkPrimary = Color(0xFF200011)
//    val LightDarkPrimary = Color(0xFF16334D)
//    val ShadeDarkPrimary = Color(0xFF0B1E2E)

    // Dark Alternative
//    val FillDarkAlternative = Color(0xFF091622)
//    val LightDarkAlternative = Color(0xFF0D1E2E)
//    val ShadeDarkAlternative = Color(0xFF060D14)

    // Light Primary
    val FillLightPrimary = Color(0xFFCBDCEC)
//    val LightLightPrimary = Color(0xFFC4D3E2)
//    val ShadeLightPrimary = Color(0xFFA7B5C2)

    // Light Alternative
//    val FillLightAlternative = Color(0xFF747D85)
//    val LightLightAlternative = Color(0xFF828D96)
    val ShadeLightAlternative = Color(0xFF61686F)

    // Yellow Content
//    val FillYellow = Color(0xFFC48C00)

    val FillYellow = Color(0xFFFFBF00)

//    val LightYellow = Color(0xFFFFD428)
//    val ShadeYellow = Color(0xFFD5A000)

    // Blue Content
//    val FillBlue = Color(0xFF273265)
//    val LightBlue = Color(0xFF3B4B98)
//    val ShadeBlue = Color(0xFF1E264D)

    // Red Content
    val FillRed = Color(0xFFEE0000)
//    val LightRed = Color(0xFFFF3122)
//    val ShadeRed = Color(0xFFBE1601)

    // Green Content
    val FillGreen = Color(0xFF2FAA6B)
//    val LightGreen = Color(0xFF56CC84)
//    val ShadeGreen = Color(0xFF228A55)

    /*
    // Moved these to the Character Definition itself
    // Character: Ayuna
    val FillAyuna = Color(0xFFD91410)
    val LightAyuna = Color(0xFFFF1813)
    val ShadeAyuna = Color(0xFF990E0B)

    // Character: Ayumi
    val FillAyumi = Color(0xFF098432)
    val LightAyumi = Color(0xFF0BA63F)
    val ShadeAyumi = Color(0xFF066626)

    // Character: Sylvia
    val FillSylvia = Color(0xFF67406D)
    val LightSylvia = Color(0xFF9A69A3)
    val ShadeSylvia = Color(0xFF593360)

    // Character: Finnian
    val FillFinnian = Color(0xFF25B97F)
    val LightFinnian = Color(0xFF2BD995)
    val ShadeFinnian = Color(0xFF329964)

    // Character: Malachai
    val FillMalachai = Color(0xFF3B3B93)
    val LightMalachai = Color(0xFF4C4CAB)
    val ShadeMalachai = Color(0xFF272778)

    // Character: Yumio
    val FillYumio = Color(0xFF2169AD)
    val LightYumio = Color(0xFF337FC8)
    val ShadeYumio = Color(0xFF1B5993)
    */

    // Basic Colors
    val FullBlack = Color(0xFF000000)
    val FullGrey = Color(0xFF808080)
    val FullWhite = Color(0xFFFFFFFF)
}

/**
 * Defines the style of [tiles][app.trailblazercombi.haventide.game.ComposableTile]
 * and [the tile map][app.trailblazercombi.haventide.game.ComposableTileMap].
 */
object TileStyle {
    val TileSize = 128.dp
    val CornerRounding = 8.dp
    val OutlineThickness = 2.dp
    val Padding = 24.dp
}

/**
 * Defines the style of
 * [ComposablePhoenixMechanismBall][app.trailblazercombi.haventide.game.mechanisms.ComposablePhoenixMechanismBall].
 */
object PhoenixBallStyle {
    val OutlineThickness = 4.dp
    val Padding = 32.dp
    val AffiliationIconSize = 28.dp
    val AffiliationIconOuterOffsetFromBottomRight = 14.dp
    val AffiliationIconInnerPadding = 4.dp
}

object GameScreenDialogBoxStyle {
    val Elevation = 12.dp
    val LargeTextSize = 36.sp
    val LargeTextLineSeparation = 50.sp
    val TitleTextSize = 32.sp
    val ButtonTextSize = 14.sp
    val InnerPadding = 8.dp
    val GameOverInnerPadding = 32.dp
    val OuterCornerRounding = 8.dp
    val ButtonCornerRounding = 4.dp
    val OutlineThickness = 0.dp
    val StretchedDialogOffsetFromEdge = 64.dp
    val TapAnywhereLabelOffset = 128.dp
    val YesNoDialogButtonMaxWidth = 260.dp
}

object GameScreenTopBubbleStyle {
    val CornerRounding = 16.dp
    val OutlineThickness = 0.dp
    val Elevation = 12.dp
    val OffsetFromEdge = 16.dp
    val InnerOffset = 8.dp
    val BubbleHeight = 56.dp
    val DoubleBubbleHeight = 80.dp
    val MiniatureWidth = 64.dp
    val MiniatureHeight = 32.dp
    val MiniatureCornerRounding = 8.dp
    val MiniatureUltimateBorder = 8.dp
    val RoundCounterWidth = 128.dp
    val StandardButtonWidth = 256.dp
    val RoundCounterTextSize = 12.sp
    val UnifiedRoundTeamTextSize = 14.sp
    val TeamTurnTextSize = 16.sp
}

object DieStyle {
    val DieSize = 36.dp
    val InnerPadding = 4.dp
    val CornerRounding = 4.dp
    val OutlineThickness = 0.dp
    val Separation = 2.dp
    val DicePerRow = 4
}

/**
 * Keep track of click states and the associated tile colors.
 * This enum exists for the purposes of highlighting tiles.
 *
 * Please refer to the game rules to find out what each of these highlights mean.
 * @property NO_INTERACTIONS_WITH_OUTLINE The [tile][TileData] should not be highlighted unless hovered over by mouse.
 * @property CLICKED_PRIMARY The [tile][TileData] should be highlighed with yellow fill and outline.
 * @property CLICKED_SECONDARY The [tile][TileData] should be highlighted with white fill and outline.
 * @property HIGHLIGHT_PRIMARY The [tile][TileData] should be highlighted with yellow outline.
 * @property HIGHLIGHT_SECONDARY The [tile][TileData] should be highlighted with white outline.
 */
enum class UniversalColorizer(
    val fillColor: Color = Palette.Glass00,
    val outlineColor: Color = Palette.Glass00,
) {
    NO_INTERACTIONS,
    NO_INTERACTIONS_WITH_OUTLINE(outlineColor = Palette.Glass20),
    CLICKED_PRIMARY(Palette.FillYellow, Palette.FillYellow),
    CLICKED_SECONDARY(Palette.FillLightPrimary, Palette.FillLightPrimary),
    CLICKED_TERTIARY(Palette.ShadeLightAlternative, Palette.ShadeLightAlternative),
    HIGHLIGHT_PRIMARY(outlineColor = Palette.FillYellow),
    HIGHLIGHT_SECONDARY(outlineColor = Palette.FillLightPrimary),
    HOVER_GLASS(Palette.Glass10, Palette.Glass20)
}
