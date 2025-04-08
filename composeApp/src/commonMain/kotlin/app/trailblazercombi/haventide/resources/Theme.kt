package app.trailblazercombi.haventide.resources

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Defines the colors used within the app
 */
object Palette {
    // TODO Remove unused colours from the Palette.
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

    // Dark Primary
    val FillDarkPrimary = Color(0xFF102538)
    val LightDarkPrimary = Color(0xFF16334D)
    val ShadeDarkPrimary = Color(0xFF0B1E2E)

    // Dark Alternative
    val FillDarkAlternative = Color(0xFF091622)
    val LightDarkAlternative = Color(0xFF0D1E2E)
    val ShadeDarkAlternative = Color(0xFF060D14)

    // Light Primary
    val FillLightPrimary = Color(0xFFCBDCEC)
    val LightLightPrimary = Color(0xFFC4D3E2)
    val ShadeLightPrimary = Color(0xFFA7B5C2)

    // Light Alternative
    val FillLightAlternative = Color(0xFF747D85)
    val LightLightAlternative = Color(0xFF828D96)
    val ShadeLightAlternative = Color(0xFF61686F)

    // Yellow Content
    val FillYellow = Color(0xFFC48C00)
    val LightYellow = Color(0xFFFFD428)
    val ShadeYellow = Color(0xFFD5A000)

    // Blue Content
    val FillBlue = Color(0xFF273265)
    val LightBlue = Color(0xFF3B4B98)
    val ShadeBlue = Color(0xFF1E264D)

    // Red Content
    val FillRed = Color(0xFFEE0000)
    val LightRed = Color(0xFFFF3122)
    val ShadeRed = Color(0xFFBE1601)

    // Green Content
    val FillGreen = Color(0xFF2FAA6B)
    val LightGreen = Color(0xFF56CC84)
    val ShadeGreen = Color(0xFF228A55)

    // TODO Move these to the Character Definition itself
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
    val OutlineThickness = 6.dp
    val Padding = 16.dp
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
    HIGHLIGHT_PRIMARY(outlineColor = Palette.FillYellow),
    HIGHLIGHT_SECONDARY(outlineColor = Palette.FillLightPrimary),
    HOVER_GLASS(Palette.Glass10, Palette.Glass20)
}
